package com.lin.bulter.common.dto.autogenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurdParam {

    private String gitRepository;   // git 代码地址
    private String branchName;      // git branch

    private String projectName;     // 需要生成增删改查的工程名称
    private String basePackage;     // 基础包名

    private boolean vue;            // 是否生成vue前端页面

    private boolean controller;     // 是否生成controller层
    private String controllerPackage;   // controller层package

    private boolean service;        // 是否生成service层
    private String servicePackage;   // service层package

    private boolean mapper;         // 是否生成dao层
    private String mapperPackage;   // mapper层package

    private String entityPackage;   // entity层package
    private String paramPackage;    // param层package

    private boolean api;            // 是否生成api层
    private String apiPackage;     // api层package

    // 数据库相关的配置
    private List<CurdEntityInfo> dbConfig;

//    private String entityName;      // entity名称

//    private Integer datasourceId;   // 数据源id
//    private String dbName;          // 数据库名称
//    private String tableName;       // 要逆向操作的数据库表名

}
