package com.lin.bulter.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.lin.bulter.common.utils.ReflectionsUtil;
import com.lin.bulter.web.vo.MockContext;
import org.apache.http.HttpResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * 描述信息
 *
 * @author wangchenglin13
 * @date 2020/3/17 19:14
 */
@RestController
@RequestMapping("/bulter")
public class MockController {

	@PostConstruct
	public void initApp(){
		try {
			ReflectionsUtil.init("E:\\testjar", "com.jd");
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

	@PostMapping("/mock")
	public byte[] mock(@RequestBody MockContext mockContext, HttpResponse response){
		System.out.printf("param = " + JSON.toJSONString(mockContext));
		byte[] serialize = null;
		try {
			Map<String, Object> clsMap = ReflectionsUtil.getClassMethodReturnInstance(null, mockContext.getClassName(), mockContext.getMethodName(), mockContext.getArgClasses());
			String str = "{\"code\":\"0000\",\"data\":{\"data\":[{\"chineseHerbalType\":1,\"chineseHerbalTypeStr\":\"test\",\"drugStroeName\":\"tesg2\",\"processType\":2,\"processTypeStr\":\"faf\",\"venderId\":22,\"venderName\":\"阿斯蒂芬\"},{\"chineseHerbalType\":2,\"chineseHerbalTypeStr\":\"test33\",\"drugStroeName\":\"tesg32\",\"processType\":1,\"processTypeStr\":\"fafsf\",\"venderId\":234,\"venderName\":\"阿斯蒂芬第三方\"}],\"pageNo\":1,\"pageSize\":10,\"totalCount\":1,\"totalPage\":1},\"msg\":\"OK\",\"success\":true}";
			JSONObject context = JSON.parseObject(str);
			Object instance1 = ReflectionsUtil.getClassMethodReturnInstance(context, mockContext.getClassName(), mockContext.getMethodName(), mockContext.getArgClasses());

			serialize = serialize(clsMap.get("instance"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return serialize;
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
