package com.lin.bulter.common.utils;

import com.alibaba.fastjson.JSONObject;

public final class WebResultUtil {

    /**
     * 组装返回结果
     * @param code
     * @param data
     * @return
     */
    public static JSONObject renderSuccess(Integer code, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ret", 1);
        jsonObject.put("code", code);
        jsonObject.put("data", data);
        return jsonObject;
    }

    /**
     * 组装返回结果
     * @param msg
     * @param data
     * @return
     */
    public static JSONObject renderFail(String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ret", 0);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;
    }
}
