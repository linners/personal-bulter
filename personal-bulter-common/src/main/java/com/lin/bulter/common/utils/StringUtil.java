package com.lin.bulter.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class StringUtil {

    private static StringTokenizer token;

    public static List<Integer> splitString(String str, String delim) {
        List<Integer> list = new ArrayList<Integer>();
        token = new StringTokenizer(str, delim);
        while (token.hasMoreTokens()) {
            list.add(new Integer(token.nextToken()));
        }
        return list;
    }

    public static List<String> splitStrings(String str, String delim) {
        List<String> list = new ArrayList<String>();
        token = new StringTokenizer(str, delim);
        while (token.hasMoreTokens()) {
            list.add(token.nextToken());
        }
        return list;
    }

    public static String toString(List<?> areaIds, String delim) {
        StringBuffer sb = new StringBuffer();
        if (areaIds != null) {
            Iterator<?> it = areaIds.iterator();
            while (it.hasNext()) {
                Object areaId = it.next();
                if (areaId == null) {
                    continue;
                } else if (it.hasNext()) {
                    sb.append(areaId.toString()).append(delim);
                } else {
                    sb.append(areaId.toString());
                }
            }
        }
        return sb.toString();
    }

}
