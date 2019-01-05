package com.lin.bulter.business.autogenerator;

import com.lin.bulter.business.autogenerator.model.DatabaseInfo;
import com.lin.bulter.business.autogenerator.model.TableColumn;
import com.lin.bulter.business.autogenerator.model.TableInfo;
import com.lin.bulter.repository.mysql.entity.DatasourceInfo;

import java.util.List;

public interface DatasourceService {

    /**
     * 获得所有的数据源
     * @return
     */
    List<DatasourceInfo> getAllDatasources();

    /**
     * 通过主键，获得数据源
     * @param datasourceId
     * @return
     */
    DatasourceInfo getDatasourceByPrimaryKey(Integer datasourceId);

    /**
     * 查询某个数据源下的所有数据库
     * @param datasourceId
     * @return
     */
    List<DatabaseInfo> getAllDbInfosByDatasourceId(Integer datasourceId);

    /**
     * 查询某个数据库下的所有表
     * @param databaseName
     * @return
     */
    List<TableInfo> getAllTableInfosByDbName(Integer datasourceId, String databaseName);

    /**
     * 查询某个表的所有字段
     * @param tableName
     * @return
     */
    List<TableColumn> getAllTableColumnsByTableName(Integer datasourceId, String databaseName, String tableName);
}
