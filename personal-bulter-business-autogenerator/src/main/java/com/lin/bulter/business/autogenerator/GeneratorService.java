package com.lin.bulter.business.autogenerator;

import com.lin.bulter.business.autogenerator.model.DatabaseInfo;
import com.lin.bulter.business.autogenerator.model.TableColumn;
import com.lin.bulter.business.autogenerator.model.TableInfo;
import com.lin.bulter.common.dto.autogenerator.CurdParam;
import com.lin.bulter.common.dto.autogenerator.GenerateParam;

import java.util.List;

public interface GeneratorService {

    /**
     * 自动生成工程框架
     * @return
     */
    String generatorProject(GenerateParam param);

    /**
     * 自动生成增删改查
     * @param param
     * @return
     */
    String generatorCrud(CurdParam param);

    /**
     * 获得一个数据源，下面所有的数据库
     * @return
     */
    List<DatabaseInfo> getAllDatabaseInfos(Integer datasourceId);

    /**
     * 查询某个数据库下所有的表
     * @param dbName
     * @return
     */
    List<TableInfo> getTablesByDb(String dbName);

    /**
     * 查询某个数据库表的字段明细
     * @param tableName
     * @return
     */
    List<TableColumn> getTableColumns(String tableName);
}
