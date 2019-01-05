package com.lin.bulter.business.autogenerator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatabaseInfo {
    private String dbName;          // 数据库名字
    private List<TableInfo> tableList; // 该数据库所有的表
}
