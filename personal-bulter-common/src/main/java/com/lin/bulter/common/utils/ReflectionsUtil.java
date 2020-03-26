package com.lin.bulter.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Predicate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static com.alibaba.fastjson.serializer.SerializerFeature.WriteMapNullValue;


/**
 * 描述信息
 * queryRxTemplateDetail
 *
 * @author wangchenglin13
 * @date 2020/3/22 10:45
 */
public class ReflectionsUtil {

	private static Reflections reflections;
	private static URLClassLoader urlClassLoader;

	public static void main(String[] args) throws Exception {
		ReflectionsUtil.init("E:\\testjar");
		Class<?> classImplName = ReflectionsUtil.getClassImplName("com.jd.medicine.common.beans.Result");

		String className = "com.jd.medicine.common.beans.Result";
		Object classInstance = getClassInstance(className, "");
		String s = JSON.toJSONString(classInstance, WriteMapNullValue);
		System.out.println(s);
	}

	/**
	 * 初始化reflections对象
	 *
	 * @param filePath
	 * @throws Exception
	 */
	public static Reflections init(String filePath, String... filterPackage) throws Exception {
		URL[] classLoaderUrls = null;
		File file = new File(filePath);
		if (file.exists()) {
			File[] list = file.listFiles();
			if (list != null && list.length > 0) {
				classLoaderUrls = new URL[list.length];
				for (int i = 0; i < list.length; i++) {
					File fileTmp = list[i];
					String fileAbsolutePath = fileTmp.getAbsolutePath();
					classLoaderUrls[i] = new URL("file:" + fileAbsolutePath);
				}
			}
		}

		if (classLoaderUrls == null) {
			throw new Exception("未扫描到相应的文件");
		}

		FilterBuilder filterBuilder = new FilterBuilder();
		for (String packName : filterPackage) {
			//定义要扫描的包
			filterBuilder = filterBuilder.includePackage(packName);
		}
		//过滤器
		Predicate<String> filter = filterBuilder;

		// 获得classLoad对象
		urlClassLoader = new URLClassLoader(classLoaderUrls);

		// 定义Reflections对象，指明"包过滤器"，以及扫描器的类型，主要把是扫描器的类型
		//细分之后，得到对应的数据
		reflections = new Reflections(new ConfigurationBuilder()
				.addClassLoader(urlClassLoader)
				.filterInputsBy(filter)
				.setScanners(
						new SubTypesScanner().filterResultsBy(filter),
						new TypeAnnotationsScanner().filterResultsBy(filter),
						new FieldAnnotationsScanner().filterResultsBy(filter),
						new MethodAnnotationsScanner().filterResultsBy(filter),
						new MethodParameterScanner().filterResultsBy(filter)
				).setUrls(classLoaderUrls));
		return reflections;
	}

	/**
	 * 通过类名称查询类
	 *
	 * @param className
	 * @return
	 */
	public static Class<?> forName(String className) {
		Class<?> aClass = ReflectionUtils.forName(className, urlClassLoader);
		return aClass;
	}

	/**
	 * 反射，获得某个类下某个方法的返回值实例化对象
	 *
	 * @param className
	 * @param methodName
	 * @param methodArgs 方法的参数列表（类名称）
	 */
	public static Map<String, Object> getClassMethodReturnInstance(JSONObject context, String className, String methodName, String[] methodArgs) throws Exception {
		try {
			// 反射获得类
			Class<?> aClass = forName(className);
			Method method = null;
			// 查询指定的方法
			Method[] methods = aClass.getDeclaredMethods();
			if (methods != null && methods.length > 0) {
				for (Method methodTmp : methods) {
					if (methodTmp.getName().equals(methodName)) {
						Class<?>[] parameterTypes = methodTmp.getParameterTypes();
						if (parameterTypes == null || parameterTypes.length == 0) {
							method = methodTmp;
						} else {
							boolean flag = true;
							for (int i = 0; i < parameterTypes.length; i++) {
								flag = parameterTypes[i].getName().equals(methodArgs[i]);
							}
							if (flag) {
								method = methodTmp;
							}
						}
					}
				}
			}
			if (method != null) {
				Type genericReturnType = method.getGenericReturnType();
				// 返回值类型
				String typeName = genericReturnType.getTypeName();
				// 获得返回值泛型
				ReturnCls returnCls = parseRetrunTyp(typeName);
				// 获得类实例
				Map<String, Object> clsMap = null;
				if (context == null) {
					clsMap = getClassInstance(returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
				} else {
					clsMap = getClassInstance(context, returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
				}
				for (Class cls : method.getParameterTypes()) {
					String name = cls.getName();
					// 获得返回值泛型
					ReturnCls returnCls1 = parseRetrunTyp(name);
					// 获得类实例
					Map<String, Object> clsMap2 = getClassInstance(returnCls1.getReturnClsName(), returnCls1.getParameterizedTypeClsName());
					System.out.println(JSON.toJSONString(clsMap2, WriteMapNullValue));
				}
				return clsMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 反射，获得某个类下某个方法的返回值实例化对象
	 *
	 * @param className
	 * @param methodName
	 */
	public static Object getClassMethodReturnInstance(String className, String methodName, Class[] methodArgs) throws Exception {
		try {
			// 反射获得类
			Class<?> aClass = forName(className);
			// 查询指定的方法
			Method method = aClass.getDeclaredMethod(methodName, methodArgs);
			if (method != null) {
				Type genericReturnType = method.getGenericReturnType();
				// 返回值类型
				String typeName = genericReturnType.getTypeName();
				// 获得返回值泛型
				ReturnCls returnCls = parseRetrunTyp(typeName);
				// 获得类实例
				Object classInstance = getClassInstance(returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
				for (Class cls : methodArgs) {
					String name = cls.getName();
					// 获得返回值泛型
					ReturnCls returnCls1 = parseRetrunTyp(name);
					// 获得类实例
					Object classInstance1 = getClassInstance(returnCls1.getReturnClsName(), returnCls1.getParameterizedTypeClsName());
					System.out.println(JSON.toJSONString(classInstance1, WriteMapNullValue));
				}
				return classInstance;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得类的实现类
	 *
	 * @param className
	 * @return
	 */
	public static Class<?> getClassImplName(String className) throws Exception {
		// 反射获得类
		Class<?> aClass = forName(className);
		// 如果是接口，则随机获得一个实现类
		if (aClass.isInterface()) {
			Set<Class<?>> monitorClasses = reflections.getSubTypesOf((Class<Object>) aClass);
			List<Class<?>> list = new ArrayList<>(monitorClasses);
			if (CollectionUtils.isEmpty(list)) {
				throw new Exception("类[" + className + "]没找到实现类");
			}
			aClass = list.get(0);
		}
		return aClass;
	}

	/**
	 * 解析返回类型
	 *
	 * @param returnTypeStr
	 * @return
	 */
	private static ReturnCls parseRetrunTyp(String returnTypeStr) throws Exception {
		// 组装返回结果
		ReturnCls returnCls = new ReturnCls();
		if (StringUtils.isNotBlank(returnTypeStr) && returnTypeStr.startsWith("<") && returnTypeStr.endsWith(">")) {
			returnTypeStr = returnTypeStr.substring(1, returnTypeStr.lastIndexOf(">"));
		}
		returnCls.setReturnClsName(returnTypeStr);
		if (StringUtils.isNotBlank(returnTypeStr) && returnTypeStr.contains("<") && returnTypeStr.contains(">")) {
			String returnClsName = returnTypeStr.substring(0, returnTypeStr.indexOf("<"));
			String parameterizedTypeClsName = returnTypeStr.substring(returnTypeStr.indexOf("<") + 1, returnTypeStr.lastIndexOf(">"));
			returnCls.setReturnClsName(returnClsName);
			returnCls.setParameterizedTypeClsName(parameterizedTypeClsName);
		}
		return returnCls;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class ReturnCls {
		/**
		 * 返回类型，类名称
		 */
		private String returnClsName;
		/**
		 * 返回类型的泛型类型，类名称
		 */
		private String parameterizedTypeClsName;
	}

	@Data
	@NoArgsConstructor
	static class FieldObj {

		/**
		 * 字段类型
		 */
		private String fieldType;
		/**
		 * 字段名称
		 */
		private String fieldName;
		/**
		 * 字段描述
		 */
		private String fieldDesc;
		/**
		 * 子Field
		 */
		private List<FieldObj> fieldObjs;

		public FieldObj(String fieldType, String fieldName, String fieldDesc) {
			this.fieldType = fieldType;
			this.fieldName = fieldName;
			this.fieldDesc = fieldDesc;
		}
	}

	/**
	 * 反射，获得类的实例化对象
	 *
	 * @param className          类对象
	 * @param parameterizedTypes 类的泛型类型
	 * @return
	 */
	public static Map<String, Object> getClassInstance(String className, String... parameterizedTypes) throws Exception {
		// map对象
		Map<String, Object> classMap = new HashMap<>();
		// 反射获得类(若接口，则返回实现类）
		Class<?> aClass = getClassImplName(className);
		Map<String, String> parameterizedTypeMap = new HashMap<>();
		TypeVariable<? extends Class<?>>[] typeParameters = aClass.getTypeParameters();
		if (typeParameters != null && typeParameters.length > 0) {
			for (int i = 0; i < typeParameters.length; i++) {
				TypeVariable typeVariable = typeParameters[i];
				parameterizedTypeMap.put(typeVariable.getName(), i < parameterizedTypes.length ? parameterizedTypes[i] : "");
			}
		}
		Constructor<?>[] constructors = aClass.getConstructors();
		Object beanObj = new Object();
		try {
			beanObj = aClass.newInstance();
		} catch (Exception e) {
			if (constructors.length > 0) {
				Constructor<?> constructor = constructors[0];
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				Object[] params = new Object[parameterTypes.length];
				for (int i = 0; i < parameterTypes.length; i++) {
					//Class<?> clsTmp = parameterTypes[i];
					//if(clsTmp.getName().equals("java.lang.String")){
					params[i] = null;
					//}
				}
				beanObj = constructor.newInstance(params);
			}
		}

		classMap.put("className", className);

		System.out.println("className：" + className);
		Field[] fields = aClass.getDeclaredFields();
		Field[] supperFields = aClass.getSuperclass().getDeclaredFields();
		Field[] result = ArrayUtils.addAll(fields, supperFields);

		List<FieldObj> fieldObjList = new ArrayList<>();

		if (result != null && result.length > 0) {
			for (Field field : result) {
				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);
				String fieldName = field.getName();
				String typeName = field.getType().getName();

				// 构造field对象
				FieldObj fieldObj = new FieldObj(typeName, fieldName, "");

				String genericTypeName = field.getGenericType().getTypeName();
				String tmp = genericTypeName;
				if (genericTypeName.contains("java.util.List")) {
					tmp = genericTypeName.replaceAll("java.util.List", "")
							.replaceAll("<", "")
							.replaceAll(">", "");
				}
				String parameterizedType = parameterizedTypeMap.get(tmp);
				if (StringUtils.isNotBlank(parameterizedType)) {
					genericTypeName = parameterizedType;
				}

				if (typeName.equals("java.lang.String")) {
					field.set(beanObj, "");
				} else if (typeName.equals("java.lang.Long") || typeName.equals("long")) {
					field.set(beanObj, 0L);
				} else if (typeName.equals("java.lang.Integer") || typeName.equals("int")) {
					field.set(beanObj, 0);
				} else if (typeName.equals("java.util.Date")) {
					field.set(beanObj, new Date());
				} else if (typeName.equals("java.lang.Boolean") || typeName.equals("boolean")) {
					field.set(beanObj, true);
				} else if (typeName.equals("java.math.BigDecimal")) {
					field.set(beanObj, BigDecimal.ZERO);
				} else if (typeName.equals("java.lang.Short") || typeName.equals("short")) {
					field.set(beanObj, (short) 0);
				} else if (typeName.equals("java.lang.Byte") || typeName.equals("byte")) {
					field.set(beanObj, (byte) 1);
				} else if (typeName.equals("java.lang.Float") || typeName.equals("float")) {
					field.set(beanObj, 0.0f);
				} else if (typeName.equals("java.lang.Double") || typeName.equals("double")) {
					field.set(beanObj, 0.0d);
				} else if (typeName.equals("java.lang.Character") || typeName.equals("char")) {
					field.set(beanObj, (char) 0);
				} else if (typeName.equals("java.util.List")) {
					genericTypeName = genericTypeName.replaceAll(typeName, "");
					ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
					Map<String, Object> clsMap = getClassInstance(retrunTyp.getReturnClsName(), retrunTyp.getParameterizedTypeClsName());
					List<Object> list = new ArrayList<>();
					list.add(clsMap.get("instance"));
					fieldObj.setFieldObjs((List<FieldObj>) clsMap.get("field"));
					field.set(beanObj, list);
				} else if (typeName.equals("java.lang.Object")) {
					if (genericTypeName.startsWith("java.util.List")) {
						genericTypeName = genericTypeName.replaceAll("java.util.List", "");
						ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
						Map<String, Object> clsMap = getClassInstance(retrunTyp.getReturnClsName(), retrunTyp.getParameterizedTypeClsName());
						List<Object> list = new ArrayList<>();
						list.add(clsMap.get("instance"));
						fieldObj.setFieldObjs((List<FieldObj>) clsMap.get("field"));
						field.set(beanObj, list);
					} else {
						genericTypeName = genericTypeName.replaceAll(typeName, "");
						ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
						Map<String, Object> clsMap = getClassInstance(retrunTyp.getReturnClsName(), retrunTyp.getParameterizedTypeClsName());
						field.set(beanObj, clsMap.get("instance"));
						fieldObj.setFieldObjs((List<FieldObj>) clsMap.get("field"));
					}
				}
				fieldObjList.add(fieldObj);
			}
		}
		classMap.put("field", fieldObjList);
		classMap.put("instance", beanObj);
		return classMap;
	}

	/**
	 * 反射，获得类的实例化对象
	 *
	 * @param className          类对象
	 * @param parameterizedTypes 类的泛型类型
	 * @return
	 */
	public static Map<String, Object> getClassInstance(JSONObject context, String className, String... parameterizedTypes) throws Exception {
		// map对象
		Map<String, Object> classMap = new HashMap<>();
		// 反射获得类(若接口，则返回实现类）
		Class<?> aClass = getClassImplName(className);
		Map<String, String> parameterizedTypeMap = new HashMap<>();
		TypeVariable<? extends Class<?>>[] typeParameters = aClass.getTypeParameters();
		if (typeParameters != null && typeParameters.length > 0) {
			for (int i = 0; i < typeParameters.length; i++) {
				TypeVariable typeVariable = typeParameters[i];
				parameterizedTypeMap.put(typeVariable.getName(), i < parameterizedTypes.length ? parameterizedTypes[i] : "");
			}
		}
		// 通过构造方法，获取实例化对象
		Constructor<?>[] constructors = aClass.getConstructors();
		Object beanObj = new Object();
		try {
			beanObj = aClass.newInstance();
		} catch (Exception e) {
			if (constructors.length > 0) {
				Constructor<?> constructor = constructors[0];
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				Object[] params = new Object[parameterTypes.length];
				for (int i = 0; i < parameterTypes.length; i++) {
					//Class<?> clsTmp = parameterTypes[i];
					//if(clsTmp.getName().equals("java.lang.String")){
					params[i] = null;
					//}
				}
				beanObj = constructor.newInstance(params);
			}
		}

		classMap.put("className", className);

		System.out.println("className：" + className);
		// 反射获取字段，并赋值
		Field[] fields = aClass.getDeclaredFields();
		Field[] supperFields = aClass.getSuperclass().getDeclaredFields();
		Field[] result = ArrayUtils.addAll(fields, supperFields);
		List<FieldObj> fieldObjList = new ArrayList<>();
		if (result != null && result.length > 0) {
			for (Field field : result) {
				if (Modifier.isFinal(field.getModifiers())) {
					continue;
				}
				field.setAccessible(true);
				String fieldName = field.getName();
				String typeName = field.getType().getName();
				// 构造field对象
				FieldObj fieldObj = new FieldObj(typeName, fieldName, "");
				String genericTypeName = field.getGenericType().getTypeName();
				String tmp = genericTypeName;
				if (genericTypeName.contains("java.util.List")) {
					tmp = genericTypeName.replaceAll("java.util.List", "")
							.replaceAll("<", "")
							.replaceAll(">", "");
				}
				String parameterizedType = parameterizedTypeMap.get(tmp);
				if (StringUtils.isNotBlank(parameterizedType)) {
					genericTypeName = parameterizedType;
				}
				Object fieldReal = context.get(fieldName);
				if (fieldReal == null) {
					continue;
				}
				String fieldRealVal = fieldReal.toString();
				if (typeName.equals("java.lang.String")) {
					field.set(beanObj, fieldRealVal);
				} else if (typeName.equals("java.lang.Long") || typeName.equals("long")) {
					field.set(beanObj, Long.valueOf(fieldRealVal));
				} else if (typeName.equals("java.lang.Integer") || typeName.equals("int")) {
					field.set(beanObj, Integer.valueOf(fieldRealVal));
				} else if (typeName.equals("java.util.Date")) {
					field.set(beanObj, new Date());
				} else if (typeName.equals("java.lang.Boolean") || typeName.equals("boolean")) {
					field.set(beanObj, Boolean.valueOf(fieldRealVal));
				} else if (typeName.equals("java.math.BigDecimal")) {
					field.set(beanObj, BigDecimal.valueOf(Long.valueOf(fieldRealVal)));
				} else if (typeName.equals("java.lang.Short") || typeName.equals("short")) {
					field.set(beanObj, Short.valueOf(fieldRealVal));
				} else if (typeName.equals("java.lang.Byte") || typeName.equals("byte")) {
					field.set(beanObj, Byte.valueOf(fieldRealVal));
				} else if (typeName.equals("java.lang.Float") || typeName.equals("float")) {
					field.set(beanObj, Float.valueOf(fieldRealVal));
				} else if (typeName.equals("java.lang.Double") || typeName.equals("double")) {
					field.set(beanObj, Double.valueOf(fieldRealVal));
				} else if (typeName.equals("java.lang.Character") || typeName.equals("char")) {
					field.set(beanObj, fieldRealVal.toCharArray());
				} else if (typeName.equals("java.util.List")) {
					genericTypeName = genericTypeName.replaceAll(typeName, "");
					ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
					List<Object> list = new ArrayList<>();
					JSONArray jsonArray = context.getJSONArray(fieldName);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						Map<String, Object> clsMap = getClassInstance(jsonObject, retrunTyp.getReturnClsName(), retrunTyp.getParameterizedTypeClsName());
						list.add(clsMap.get("instance"));
						fieldObj.setFieldObjs((List<FieldObj>) clsMap.get("field"));
					}
					field.set(beanObj, list);
				} else if (typeName.equals("java.lang.Object")) {
					if (genericTypeName.startsWith("java.util.List")) {
						genericTypeName = genericTypeName.replaceAll("java.util.List", "");
						ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
						List<Object> list = new ArrayList<>();
						JSONArray jsonArray = context.getJSONArray(fieldName);
						for (int i = 0; i < jsonArray.size(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							Map<String, Object> clsMap = getClassInstance(jsonObject, retrunTyp.getReturnClsName(), retrunTyp.getParameterizedTypeClsName());
							list.add(clsMap.get("instance"));
							fieldObj.setFieldObjs((List<FieldObj>) clsMap.get("field"));

						}
						field.set(beanObj, list);
					} else {
						genericTypeName = genericTypeName.replaceAll(typeName, "");
						ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
						Map<String, Object> clsMap = getClassInstance(context.getJSONObject(fieldName), retrunTyp.getReturnClsName(), retrunTyp.getParameterizedTypeClsName());
						field.set(beanObj, clsMap.get("instance"));
						fieldObj.setFieldObjs((List<FieldObj>) clsMap.get("field"));
					}
				}
				fieldObjList.add(fieldObj);
			}
		}
		classMap.put("field", fieldObjList);
		classMap.put("instance", beanObj);
		return classMap;
	}
}
