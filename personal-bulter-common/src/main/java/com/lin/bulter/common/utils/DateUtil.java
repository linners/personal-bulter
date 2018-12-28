package com.lin.bulter.common.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * 日期处理工具
 */
public class DateUtil {

    public static String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static String FORMAT_DAY = "yyyy-MM-dd";

    private DateUtil() {
    }

    /**
     * 取得Integer型的当前日期
     *
     * @return yyyymmdd
     */
    public static Integer getIntToday() {
        return getIntDate(getNow());
    }

    /**
     * 根据Date获取整型日期
     *
     * @param d
     * @return yyyymmdd
     */
    public static Integer getIntDate(Date d) {
        if (d == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return getIntDate(c);
    }

    /**
     * 根据Calendar获取整型日期
     *
     * @param c
     * @return yyyymmdd
     */
    public static Integer getIntDate(Calendar c) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        return year * 10000 + month * 100 + day;
    }

    /**
     * 取得Date型的当前日期
     *
     * @return
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * 取得当前日期
     *
     * @return long 10位）
     */
    public static long getLongNow() {
        return getNow().getTime() / 1000;
    }

    /**
     * 取得当前日期
     *
     * @return Integer 10位）
     */
    public static Integer getIntegerNow() {
        return (int) (getNow().getTime() / 1000);
    }

    /**
     * 获取指定日期的整型
     *
     * @param dateStr 时间字符串，格式为：yyyy-MM-dd
     * @return
     */
    public static Integer getIntegerDate(String dateStr) {
        if (dateStr != null && !dateStr.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DAY);
            try {
                Date date = sdf.parse(dateStr);
                return getIntDate(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 获取指定日期的时间戳
     *
     * @param dateStr
     * @return
     */
    public static Integer getDateTimeStrap(String dateStr, String format) {
        if (dateStr != null && !dateStr.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            try {
                Date date = sdf.parse(dateStr);
                return (int) (date.getTime() / 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 取得当前日期
     * Integer型时间戳
     *
     * @return Integer 10位）
     */
    public static Integer getIntegerDate(Date date) {
        return (int) (date.getTime() / 1000);
    }

    /**
     * 取得当前日期
     * LONG型时间戳
     *
     * @return Integer 10位）
     */
    public static long getlongDate(Date date) {
        return date.getTime() / 1000;
    }

    /**
     * 获得自定义的时间
     *
     * @param customMonth  几月后或几月前，例如：1表示1月后，-1表示1月前
     * @param customDay    几天后或几天前，例如：1表示1天后，-1表示1天前
     * @param customHour   自定义小时
     * @param customMinute 自定义分钟数
     * @param customSecond 自定义秒数
     */
    public static Date getCustomDate(Integer customMonth, Integer customDay, Integer customHour, Integer customMinute, Integer customSecond) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(GregorianCalendar.MONTH, customMonth);
        calendar.add(GregorianCalendar.DATE, customDay);
        calendar.set(Calendar.HOUR_OF_DAY, customHour);
        calendar.set(Calendar.MINUTE, customMinute);
        calendar.set(Calendar.SECOND, customSecond);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取指定日期
     *
     * @param year  几月后或几月前，例如：1表示1月后，-1表示1月前
     * @param month 几天后或几天前，例如：1表示1天后，-1表示1天前
     * @param i
     * @return
     */
    public static Date getCustomDate(Date date, int month, int day) {
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.MONTH, month);
        calendar.add(GregorianCalendar.DATE, day);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDateTime(1515307702000L, DateUtil.FORMAT_SECOND));
    }

    /**
     * 格式化时间戳为时间字符串
     *
     * @param dateTime 时间戳，精确到  “毫秒”
     * @param format   格式化格式，默认为“yyyy-MM-dd HH:mm:ss”
     * @return
     */
    public static String formatDateTime(Long dateTime, String format) {
        String formatDefault = FORMAT_SECOND;
        Date date = new Date(dateTime);
        if (format != null && !format.equals("")) {
            formatDefault = format;
        }
        return new SimpleDateFormat(formatDefault).format(date);
    }

    /**
     * 获得当天的日期（字符串形式）
     *
     * @return
     */
    public static String getNowDateStr() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DAY);
        return sdf.format(date);
    }

    /**
     * 08：30 转为 830
     *
     * @param strs
     * @return
     */
    public static Integer getIntFromStr(String strs) {
        if (strs != null && strs.contains(":")) {
            String[] str = strs.split(":");
            return Integer.parseInt(str[0] + str[1]);
        }
        return 0;
    }

    /**
     * 830 转为 08：30
     *
     * @param date
     * @return
     */
    public static String getStrFromInt(Integer date) {
        String str = date.toString();
        String pre = str.substring(0, str.length() - 2);
        String end = str.substring(str.length() - 2, str.length());
        if (str.length() == 3) {
            return "0" + pre + ":" + end;
        } else {
            return pre + ":" + end;
        }
    }

    /**
     * 遍历两个日期，并按每天日期，将结果存入集合中
     *
     * @param startTime 单位为毫秒
     * @param endTime   单位为毫秒
     * @return
     */
    public static List<String> iterateTwoDate(long startTime, long endTime) {
        List<String> dayList = new ArrayList<String>();
        Long oneDay = 1000 * 60 * 60 * 24l;
        Long time = startTime;
        while (time <= endTime) {
            Date d = new Date(time);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            dayList.add(df.format(d));
            time += oneDay;
        }
        return dayList;
    }

    /**
     * 返回两个日期相差的天数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDayInterval(long startTime, long endTime) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(startTime);
        long startDay = c.getTimeInMillis();
        c.setTimeInMillis(endTime);
        long endDay = c.getTimeInMillis();
        return (int) ((endDay - startDay) / (1000 * 3600 * 24));
    }

    /**
     * 获得当前时间的年和月，格式为：201803
     *
     * @return
     */
    public static Integer getCurYearMonth() {
        // 获得当前月份
        LocalDate date = LocalDate.now();
        Integer monthValue = date.getMonthValue();
        String month = monthValue.toString().length() == 1 ? "0" + monthValue : monthValue.toString();
        String yearMonth = date.getYear() + "" + month;
        return Integer.parseInt(yearMonth);
    }

}
