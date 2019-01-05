package com.lin.bulter.business.autogenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {

    private String tableName;               // 表名字
    private String tableComment;            // 表描述
    private List<TableColumn> columns;      // 所有字段信息
}
