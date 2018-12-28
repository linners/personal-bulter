package com.lin.bulter.repository.mysql.entity;

public class TableInfo {

    private String columnName;             // 字段名称
    private String dataType;               // 数据类型
    private String columnComment;          // 字段描述

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
