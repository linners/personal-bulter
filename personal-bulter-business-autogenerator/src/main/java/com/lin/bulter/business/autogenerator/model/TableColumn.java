package com.lin.bulter.business.autogenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表的所有字段
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableColumn {

    private String columnName;             // 数据库中，字段名称
    private String dataType;               // 数据库中，数据类型
    private String fieldName;              // entity类，字段名称
    private String fieldType;              // entity类，数据类型
    private String columnComment;          // 字段描述
}
