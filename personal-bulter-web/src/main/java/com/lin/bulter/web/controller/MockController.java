package com.lin.bulter.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.lin.bulter.business.service.ClassJsonService;
import com.lin.bulter.common.param.ClassJsonParam;
import com.lin.bulter.common.utils.ReflectionsUtil;
import com.lin.bulter.common.utils.StringUtil;
import com.lin.bulter.repository.mysql.dao.ClassJsonMapper;
import com.lin.bulter.repository.mysql.entity.ClassJson;
import com.lin.bulter.web.vo.MockContext;
import com.lin.bulter.web.vo.MockJsonVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述信息
 *
 * @author wangchenglin13
 * @date 2020/3/17 19:14
 */
@RestController
@RequestMapping("/bulter")
public class MockController {

	@Autowired
	private ClassJsonService classJsonService;
	@Autowired
	private ClassJsonMapper classJsonMapper;

	@PostConstruct
	public void initApp(){
		try {
			ReflectionsUtil.init("D:\\BuildTools\\maven\\repository");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/init")
	public String init(){
		try {
			ReflectionsUtil.init("/data/service/testjar", "com.jd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	@GetMapping("showClassMethods/{className}")
	public List<ReflectionsUtil.ClassMethodInfo> showClassMethods(@PathVariable String className){
		List<ReflectionsUtil.ClassMethodInfo> classMethods = new ArrayList<>();
		try {
			classMethods = ReflectionsUtil.getClassMethods(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classMethods;
	}

	@PostMapping("/mock")
	public byte[] mock(@RequestBody MockContext mockContext, HttpResponse response){
		System.out.printf("param = " + JSON.toJSONString(mockContext));
		byte[] serialize = null;
		try {
			//ReflectionsUtil.ClassInfo classInfo = ReflectionsUtil.getClassMethodReturnInstance(null, mockContext.getClassName(), mockContext.getMethodName(), mockContext.getArgClasses());
			//System.out.println(JSON.toJSONString(classInfo));
			//String str = "{\"code\":\"0000\",\"data\":{\"data\":[{\"chineseHerbalType\":1,\"chineseHerbalTypeStr\":\"test\",\"drugStroeName\":\"tesg2\",\"processType\":2,\"processTypeStr\":\"faf\",\"venderId\":22,\"venderName\":\"阿斯蒂芬\"},{\"chineseHerbalType\":2,\"chineseHerbalTypeStr\":\"test33\",\"drugStroeName\":\"tesg32\",\"processType\":1,\"processTypeStr\":\"fafsf\",\"venderId\":234,\"venderName\":\"阿斯蒂芬第三方\"}],\"pageNo\":1,\"pageSize\":10,\"totalCount\":1,\"totalPage\":1},\"msg\":\"OK\",\"success\":true}";
			ClassJsonParam classJsonParam = new ClassJsonParam();
			classJsonParam.setClassName(mockContext.getClassName());
			classJsonParam.setMethodName(mockContext.getMethodName());
			String[] argClassNameList = mockContext.getArgClasses();
			List<String> list = Arrays.stream(argClassNameList).collect(Collectors.toList());
			classJsonParam.setArgClassList(StringUtil.toString(list, ","));
			List<ClassJson> classJsons = classJsonMapper.selectByCondition(classJsonParam);
			if(CollectionUtils.isNotEmpty(classJsons)){
				JSONObject context = JSON.parseObject(classJsons.get(0).getJsonVal());
				ReflectionsUtil.ClassInfo classInfo1 = ReflectionsUtil.getClassMethodReturnInstance(context, mockContext.getClassName(), mockContext.getMethodName(), mockContext.getArgClasses());

				serialize = serialize(classInfo1.getClassInstance());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serialize;
	}

	@PostMapping("/mocksave")
	public Integer mocksave(@RequestBody MockJsonVo mockJsonVo){
		System.out.printf("param = " + JSON.toJSONString(mockJsonVo));
		ClassJson classJson = new ClassJson();
		classJson.setClassName(mockJsonVo.getClassName());
		classJson.setMethodName(mockJsonVo.getMethodName());
		String[] argClassNameList = mockJsonVo.getArgClassNameList();
		List<String> list = Arrays.stream(argClassNameList).collect(Collectors.toList());
		classJson.setArgClassList(StringUtil.toString(list, ","));
		String json = mockJsonVo.getJson();
		json = json.replaceAll("\n", "");
		classJson.setJsonVal(json);
		classJson.setClassName(mockJsonVo.getClassName());
		Integer integer = classJsonService.insertClassJson(classJson);
		return integer;
	}

	@PostMapping("/mockFile")
	public ReflectionsUtil.ClassInfo mockFile(@RequestBody MockContext mockContext){
		ReflectionsUtil.ClassInfo classInfo = null;
		try {
			classInfo = ReflectionsUtil.getClassMethodReturnInstance(null, mockContext.getClassName(), mockContext.getMethodName(), mockContext.getArgClasses());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classInfo;
	}

	@PostMapping("/mockshow")
	public ReflectionsUtil.ClassInfo mockshow(@RequestBody MockContext mockContext){
		ReflectionsUtil.ClassInfo classInfo = null;
		try {
			//MockContext mockContext = new MockContext();
			//mockContext.setClassName("com.jd.medicine.drug.center.sdk.service.ChineseHerbalDrugVenderConfigService");
			////mockContext.setMethodName("queryVenderProcessCostSku");
			////mockContext.setArgClasses(new String[]{"com.jd.medicine.drug.center.sdk.param.ClientInfo", "com.jd.medicine.drug.center.sdk.param.herbal.VenderProcessCostParam"});mockContext.setClassName("com.jd.medicine.drug.center.sdk.service.ChineseHerbalDrugVenderConfigService");
			//mockContext.setMethodName("queryVenderConfig");
			//mockContext.setArgClasses(new String[]{"com.jd.medicine.drug.center.sdk.param.ClientInfo", "com.jd.medicine.drug.center.sdk.param.herbal.ChineseHerbalDrugVenderConfigParam"});
			classInfo = ReflectionsUtil.getClassMethodReturnInstance(null, mockContext.getClassName(), mockContext.getMethodName(), mockContext.getArgClasses());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classInfo;
	}


	/**
	 * Hessian实现序列化
	 * @param employee
	 * @return
	 * @throws IOException
	 */
	public static byte[] serialize(Object employee){
		ByteArrayOutputStream byteArrayOutputStream = null;
		HessianOutput hessianOutput = null;
		try {
			byteArrayOutputStream = new ByteArrayOutputStream();
			// Hessian的序列化输出
			hessianOutput = new HessianOutput(byteArrayOutputStream);
			hessianOutput.writeObject(employee);
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				hessianOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Hessian实现反序列化
	 * @param employeeArray
	 * @return
	 */
	public static Object deserialize(byte[] employeeArray) {
		ByteArrayInputStream byteArrayInputStream = null;
		HessianInput hessianInput = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(employeeArray);
			// Hessian的反序列化读取对象
			hessianInput = new HessianInput(byteArrayInputStream);
			return hessianInput.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				byteArrayInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				hessianInput.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
