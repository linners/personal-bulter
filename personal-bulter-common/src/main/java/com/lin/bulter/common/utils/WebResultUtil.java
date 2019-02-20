package com.lin.bulter.common.utils;

import com.alibaba.fastjson.JSONObject;

public class WebResultUtil {

	/**
	 * 组装返回结果
	 *
	 * @param msg
	 * @param data
	 * @return
	 */
	public static JSONObject render(Integer code, String msg, Object data) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("code", code);
		jsonObject.put("msg", msg);
		jsonObject.put("data", data);
		return jsonObject;
	}
}
