package com.lin.bulter.web.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceVo {

    private Integer datasourceId;       // 数据源ID
    private String databaseName;        // 数据库名称

    private String tableName;           // 数据库表名称
}
