package com.lin.bulter.business.autogenerator.service;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.common.dto.DatasourceInfoParam;
import com.lin.bulter.common.dto.datasourceinfo.DatabaseInfo;
import com.lin.bulter.common.dto.datasourceinfo.DatasourceDto;
import com.lin.bulter.common.dto.datasourceinfo.TableColumnInfo;
import com.lin.bulter.common.dto.datasourceinfo.TableInfo;
import com.lin.bulter.repository.mysql.entity.DatasourceInfo;

import java.util.List;

public interface DatasourceInfoService {

    /**
     * 新增
     *
     * @return
     */
    Integer insertDatasourceInfo(DatasourceInfo datasourceInfo);

    /**
     * 修改
     *
     * @return
     */
    Integer updateDatasourceInfoById(DatasourceInfo datasourceInfo);

    /**
     * 删除
     */
    Integer deleteDatasourceInfoById(Integer datasourceInfoId);

    /**
     * 按主键查询
     */
    DatasourceInfo selectDatasourceInfoById(Integer datasourceInfoId);

    /**
     * 查询所有
     */
    List<DatasourceInfo> selectAllDatasourceInfos();

    /**
     * 分页查询
     */
    PageInfo<DatasourceInfo> selectDatasourceInfoByPage(DatasourceInfoParam datasourceInfoParam);


    /**
     * 查询所有的数据源，包括数据库，以及表结构
     * @return
     */
    List<DatasourceDto> getAllDatasources();

    /**
     * 查询某个数据源下的所有数据库
     * @param datasourceId
     * @return
     */
    List<DatabaseInfo> getAllDbInfosByDatasourceId(Integer datasourceId, boolean isAllInfos);

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
    List<TableColumnInfo> getAllTableColumnsByTableName(Integer datasourceId, String databaseName, String tableName);
}
