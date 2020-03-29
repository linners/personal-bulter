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
		ReflectionsUtil.init("D:\\BuildTools\\maven\\repository\\com\\jd");
		Class<?> classImplName = ReflectionsUtil.getClassImplName("com.jd.medicine.common.beans.Result");

		String className = "com.jd.medicine.common.beans.Result";
		ClassInfo classInfo = getClassInstance(null, className, "com.jd.medicine.common.beans.Page<com.jd.medicine.drug.center.sdk.dto.herbal.ChineseHerbalDrugVenderConfigDTO>");
		String s = JSON.toJSONString(classInfo, WriteMapNullValue);
		System.out.println(s);

		String str = "{\"code\":\"0000\",\"data\":{\"data\":[{\"chineseHerbalType\":1,\"chineseHerbalTypeStr\":\"test\",\"drugStroeName\":\"tesg2\",\"processType\":2,\"processTypeStr\":\"faf\",\"venderId\":22,\"venderName\":\"阿斯蒂芬\"},{\"chineseHerbalType\":2,\"chineseHerbalTypeStr\":\"test33\",\"drugStroeName\":\"tesg32\",\"processType\":1,\"processTypeStr\":\"fafsf\",\"venderId\":234,\"venderName\":\"阿斯蒂芬第三方\"}],\"pageNo\":1,\"pageSize\":10,\"totalCount\":1,\"totalPage\":1},\"msg\":\"OK\",\"success\":true}";
		JSONObject jsonObject = JSON.parseObject(str);
		ClassInfo classInfo2 = getClassInstance(jsonObject, className, "com.jd.medicine.common.beans.Page<com.jd.medicine.drug.center.sdk.dto.herbal.ChineseHerbalDrugVenderConfigDTO>");
		String s2 = JSON.toJSONString(classInfo2, WriteMapNullValue);
		System.out.println(s2);
	}

	/**
	 * 初始化reflections对象
	 *
	 * @param filePath
	 * @throws Exception
	 */
	public static Reflections init(String filePath, String... filterPackage) throws Exception {
		URL[] classLoaderUrls = null;
		List<File> allJarFiles = getAllJarFiles(filePath, null);
		if (CollectionUtils.isEmpty(allJarFiles)) {
			throw new Exception("未扫描到jar文件");
		}
		classLoaderUrls = new URL[allJarFiles.size()];
		for (int i = 0; i < allJarFiles.size(); i++) {
			File fileTmp = allJarFiles.get(i);
			String fileAbsolutePath = fileTmp.getAbsolutePath();
			classLoaderUrls[i] = new URL("file:" + fileAbsolutePath);
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
	public static Class<?> forName(String className) throws Exception {
		Class<?> aClass = ReflectionUtils.forName(className, urlClassLoader);
		if (aClass == null) {
			throw new Exception("为找到相应的类");
		}
		return aClass;
	}

	/**
	 * 获得一个类下的所有方法
	 *
	 * @param className
	 * @return
	 */
	public static List<ClassMethodInfo> getClassMethods(String className) throws Exception {
		List<ClassMethodInfo> result = new ArrayList<>();
		// 反射获得类
		Class<?> aClass = forName(className);
		Method[] methods = aClass.getMethods();
		if (methods != null && methods.length > 0) {
			for (Method method : methods) {
				ClassMethodInfo classMethodInfo = new ClassMethodInfo();
				classMethodInfo.setClassName(className);
				classMethodInfo.setMethodName(method.getName());
				Class<?>[] parameterTypes = method.getParameterTypes();
				List<String> paramClassNameList = new ArrayList<>();
				if (parameterTypes != null && parameterTypes.length > 0) {
					for (Class<?> cls : parameterTypes) {
						paramClassNameList.add(cls.getName());
					}
					classMethodInfo.setParamClassNameList(paramClassNameList);
				}
				Type returnType = method.getGenericReturnType();
				if (result != null) {
					classMethodInfo.setReturnClassName(returnType.getTypeName());
				}
				result.add(classMethodInfo);
			}
		}
		return result;
	}

	/**
	 * 反射，获得某个类下某个方法的返回值实例化对象
	 *
	 * @param className
	 * @param methodName
	 * @param methodArgs 方法的参数列表（类名称）
	 */
	public static ClassInfo getClassMethodReturnInstance(JSONObject context, String className, String methodName, String[] methodArgs) throws Exception {
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
		if (method == null) {
			throw new Exception("未找到相应的方法");
		}
		Type genericReturnType = method.getGenericReturnType();
		// 返回值类型
		String typeName = genericReturnType.getTypeName();
		// 获得返回值泛型
		ReturnCls returnCls = parseRetrunTyp(typeName);
		// 获得类实例
		ClassInfo classInfo = null;
		if (context == null) {
			classInfo = getClassInstance(context, returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
		} else {
			classInfo = getClassInstance(context, returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
		}
		//for (Class cls : method.getParameterTypes()) {
		//	String name = cls.getName();
		//	// 获得返回值泛型
		//	ReturnCls returnCls1 = parseRetrunTyp(name);
		//	// 获得类实例
		//	ClassInfo classInfo2 = getClassInstance(context, returnCls1.getReturnClsName(), returnCls1.getParameterizedTypeClsName());
		//}
		return classInfo;
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
		if (aClass == null) {
			throw new Exception("未通过类名称，扫描到相关的类");
		}
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

	/**
	 * 反射，获得类的实例化对象
	 *
	 * @param className          类对象
	 * @param parameterizedTypes 类的泛型类型
	 * @return
	 */
	public static ClassInfo getClassInstance(JSONObject context, String className, String... parameterizedTypes) throws Exception {
		// map对象
		ClassInfo classInfo = new ClassInfo();
		classInfo.setClassName(className);

		// 反射获得类(若接口，则返回实现类）
		Class<?> aClass = getClassImplName(className);
		// 通过构造方法，获得类实例对象
		Object beanInstance = getClassInstaceByContructor(aClass);
		// 泛型擦除
		Map<String, String> parameterizedTypeMap = new HashMap<>();
		TypeVariable<? extends Class<?>>[] typeParameters = aClass.getTypeParameters();
		if (typeParameters != null && typeParameters.length > 0) {
			for (int i = 0; i < typeParameters.length; i++) {
				TypeVariable typeVariable = typeParameters[i];
				parameterizedTypeMap.put(typeVariable.getName(), i < parameterizedTypes.length ? parameterizedTypes[i] : "");
			}
		}
		// 类成员变量赋值
		Field[] fields = aClass.getDeclaredFields();
		Field[] supperFields = aClass.getSuperclass().getDeclaredFields();
		Field[] result = ArrayUtils.addAll(fields, supperFields);

		List<FieldObj> fieldObjList = new ArrayList<>();
		if (result != null && result.length > 0) {
			for (Field field : result) {
				// 类Field赋值
				FieldObj fieldObj = instanceField(context, beanInstance, field, parameterizedTypeMap);
				if (fieldObj == null) {
					continue;
				}
				fieldObjList.add(fieldObj);
			}
		}
		classInfo.setClassFieldList(fieldObjList);
		classInfo.setClassInstance(beanInstance);
		return classInfo;
	}

	/**
	 * 获得某个文件夹下的所有jar文件
	 *
	 * @param filePath
	 * @param resultFiles
	 * @return
	 */
	private static List<File> getAllJarFiles(String filePath, List<File> resultFiles) {
		if (resultFiles == null) {
			resultFiles = new ArrayList<>();
		}
		File sourFile = new File(filePath);
		if (sourFile.exists()) {
			File[] files = sourFile.listFiles();
			if (files != null && files.length > 0) {
				for (File fileTemp : files) {
					if (fileTemp.isDirectory()) {
						List<File> allJarFiles = getAllJarFiles(fileTemp.getAbsolutePath(), resultFiles);
					} else {
						if (fileTemp.getAbsolutePath().endsWith(".jar")) {
							resultFiles.add(fileTemp);
						}
					}
				}
			}
		}
		return resultFiles;
	}

	/**
	 * 通过类的构造方法，获得类实例对象
	 *
	 * @param aClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	private static Object getClassInstaceByContructor(Class<?> aClass) throws Exception {
		Object beanObj = new Object();
		// 获得类的所有构造方法
		try {
			// 若存在无参构造方法，直接创建实例
			beanObj = aClass.newInstance();
		} catch (Exception e) {
			// 若没有无参构造方法，则从所有的构造方法取一个，实例化
			Constructor<?>[] constructors = aClass.getDeclaredConstructors();
			if (constructors == null || constructors.length == 0) {
				throw new Exception("没有发现构造方法");
			}
			Constructor<?> constructor = constructors[0];
			constructor.setAccessible(true);
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
		return beanObj;
	}

	/**
	 * 类成员变量赋值
	 *
	 * @param beanInstance         类实例
	 * @param field                类成员变量
	 * @param parameterizedTypeMap 类泛型参数,例如：Map.put("T", "com.jd.com.Result")
	 */
	private static FieldObj instanceField(JSONObject context, Object beanInstance, Field field, Map<String, String> parameterizedTypeMap) throws Exception {
		if (Modifier.isFinal(field.getModifiers())) {
			return null;
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
		Object commonFieldVal = getCommonFieldVal(typeName, fieldName, context);

		if(commonFieldVal!=null){
			field.set(beanInstance, commonFieldVal);
		}else if (typeName.equals("java.util.List")) {
			Object fieldValTemp = getCommonFieldVal(genericTypeName, fieldName, context);
			if(fieldValTemp!=null){
				field.set(beanInstance, fieldValTemp);
			}else {
				genericTypeName = genericTypeName.replaceAll(typeName, "");
				ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
				// 获得field值
				List<Object> fieldListVal = getFieldListVal(fieldName, context, fieldObj, retrunTyp);
				field.set(beanInstance, fieldListVal);
			}
		} else if (typeName.equals("java.lang.Object")) {
			Object fieldValTemp = getCommonFieldVal(genericTypeName, fieldName, context);
			if(fieldValTemp!=null){
				field.set(beanInstance, fieldValTemp);
			}else if (genericTypeName.startsWith("java.util.List") || genericTypeName.startsWith("java.util.Set")) {
				genericTypeName = genericTypeName.replaceAll("java.util.List", "").replaceAll("java.util.Set", "");
				ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
				// 获得field值
				List<Object> fieldListVal = getFieldListVal(fieldName, context, fieldObj, retrunTyp);
				field.set(beanInstance, fieldListVal);
			} else {
				genericTypeName = genericTypeName.replaceAll(typeName, "");
				ReturnCls retrunTyp = parseRetrunTyp(genericTypeName);
				// 获得field值
				Object fieldVal = getFieldObjectVal(fieldName, context, fieldObj, retrunTyp);
				field.set(beanInstance, fieldVal);
			}
		}
		return fieldObj;
	}

	/**
	 * 获得基础类型Field值
	 * @param typeName
	 * @param fieldName
	 * @param context
	 * @return
	 */
	private static Object getCommonFieldVal(String typeName, String fieldName, JSONObject context) {
		Object fieldVal = null;
		if (typeName.equals("java.lang.String")) {
			if (context == null) {
				fieldVal = "";
			} else {
				fieldVal = context.getString(fieldName);
			}
		} else if (typeName.equals("java.lang.Long") || typeName.equals("long")) {
			if (context == null) {
				fieldVal = 0L;
			} else {
				fieldVal = context.getLongValue(fieldName);
			}
		} else if (typeName.equals("java.lang.Integer") || typeName.equals("int")) {
			if (context == null) {
				fieldVal = 0;
			} else {
				fieldVal = context.getIntValue(fieldName);
			}
		} else if (typeName.equals("java.util.Date")) {
			if (context == null) {
				fieldVal = new Date();
			} else {
				fieldVal = context.getDate(fieldName);
			}
		} else if (typeName.equals("java.lang.Boolean") || typeName.equals("boolean")) {
			if (context == null) {
				fieldVal = true;
			} else {
				fieldVal = context.getBooleanValue(fieldName);
			}
		} else if (typeName.equals("java.math.BigDecimal")) {
			if (context == null) {
				fieldVal = BigDecimal.ZERO;
			} else {
				fieldVal = context.getBigDecimal(fieldName);
			}
		} else if (typeName.equals("java.lang.Short") || typeName.equals("short")) {
			if (context == null) {
				fieldVal = (short) 0;
			} else {
				fieldVal = context.getShortValue(fieldName);
			}
		} else if (typeName.equals("java.lang.Byte") || typeName.equals("byte")) {
			if (context == null) {
				fieldVal = (byte) 0;
			} else {
				fieldVal = context.getShortValue(fieldName);
			}
		} else if (typeName.equals("java.lang.Float") || typeName.equals("float")) {
			if (context == null) {
				fieldVal = (float) 0;
			} else {
				fieldVal = context.getShortValue(fieldName);
			}
		} else if (typeName.equals("java.lang.Double") || typeName.equals("double")) {
			if (context == null) {
				fieldVal = (double) 0;
			} else {
				fieldVal = context.getShortValue(fieldName);
			}
		} else if (typeName.equals("java.lang.Character") || typeName.equals("char")) {
			fieldVal = (char) 0;
		}
		return fieldVal;
	}

	/**
	 * 获得field字段的list值
	 *
	 * @param fieldName
	 * @param context
	 * @param fieldObj
	 * @param returnCls
	 * @return
	 */
	private static List<Object> getFieldListVal(String fieldName, JSONObject context, FieldObj fieldObj, ReturnCls returnCls) throws Exception {
		List<Object> list = new ArrayList<>();
		if (context != null) {
			JSONArray jsonArray = context.getJSONArray(fieldName);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				ClassInfo classInfo = getClassInstance(jsonObject, returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
				list.add(classInfo.getClassInstance());
				fieldObj.setFieldObjs(classInfo.getClassFieldList());
			}
		} else {
			ClassInfo classInfo = getClassInstance(null, returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
			list.add(classInfo.getClassInstance());
			fieldObj.setFieldObjs(classInfo.getClassFieldList());
		}
		return list;
	}

	/**
	 * 获得field字段的Object值
	 *
	 * @param fieldName
	 * @param context
	 * @param fieldObj
	 * @param returnCls
	 * @return
	 */
	private static Object getFieldObjectVal(String fieldName, JSONObject context, FieldObj fieldObj, ReturnCls returnCls) throws Exception {
		Object fieldVal = null;
		ClassInfo classInfo = null;
		if (context != null) {
			classInfo = getClassInstance(context.getJSONObject(fieldName), returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
		} else {
			classInfo = getClassInstance(null, returnCls.getReturnClsName(), returnCls.getParameterizedTypeClsName());
		}
		fieldVal = classInfo.getClassInstance();
		fieldObj.setFieldObjs(classInfo.getClassFieldList());
		return fieldVal;
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

	@Data
	@NoArgsConstructor
	public static class ClassInfo {
		/**
		 * class名称
		 */
		private String className;
		/**
		 * class实例
		 */
		private Object classInstance;
		/**
		 * 类中包含的所有字段
		 */
		private List<FieldObj> classFieldList;
	}

	/**
	 * 类下的方法信息
	 */
	@Data
	@NoArgsConstructor
	public static class ClassMethodInfo {
		/**
		 * 类名（有包路径）
		 */
		private String className;
		/**
		 * 方法名（有包路径）
		 */
		private String methodName;
		/**
		 * 参数列表
		 */
		private List<String> paramClassNameList;
		/**
		 * 返回值列表
		 */
		private String returnClassName;

	}

}
