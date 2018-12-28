package com.lin.bulter.common.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;

public class CheckUtil {

    private CheckUtil() {
    }

    public static <T> boolean isNull(T t) {
        return t == null ? true : false;
    }

    public static boolean isLessThanZero(Integer num) {
        boolean result = false;
        if (num == null || num <= 0) {
            result = true;
        }
        return result;
    }

    public static <T> boolean isEmpty(List<T> list) {
        boolean result = false;
        if (list == null || list.size() == 0) {
            result = true;
        }
        return result;
    }

    public static boolean isEmpty(String para) {
        boolean result = false;
        if (para == null || para.trim().equals("")) {
            result = true;
        }
        return result;
    }

    public static boolean isJsonFormat(String jsonString) {
        boolean flag = false;
        if (jsonString != null && !jsonString.trim().equals("")) {
            try {
                JSON.parse(jsonString);
                flag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 检查对应位是否为1
     *
     * @param deliveryWay
     * @param reverseIndex 倒数的位置
     * @return
     */
    public static boolean checkType(String type, int reverseIndex) {
        boolean result = false;
        if (type != null && type.length() >= reverseIndex && reverseIndex >= 1) {
            StringBuilder builder = new StringBuilder(type);
            String flag = builder.reverse().toString().substring(reverseIndex - 1, reverseIndex);
            if (flag != null && flag.equals("1")) {
                result = true;
            }
        }
        return result;
    }

}
