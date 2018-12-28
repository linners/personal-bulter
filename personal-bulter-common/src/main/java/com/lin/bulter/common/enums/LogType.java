package com.lin.bulter.common.enums;

public enum LogType {
    TO_BE_REPORTED(1, "待提报"),
    TO_DATA_WAIT(11, "数据准备中"),
    TO_COST_AUDIT(12, "待费用审核"),
    TO_WAIT_AUDIT(3, "待审核");

    private Integer key;
    private String value;

    LogType(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static String getValueByKey(Integer key) {
        String value = "";
        if (key == null) {
            return value;
        }
        LogType[] enumTypes = LogType.values();
        for (LogType enumType : enumTypes) {
            if (enumType.key.equals(key)) {
                value = enumType.value;
                break;
            }
        }
        return value;
    }
}
