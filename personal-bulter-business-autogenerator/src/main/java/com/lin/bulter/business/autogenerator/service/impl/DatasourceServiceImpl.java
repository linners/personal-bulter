package com.lin.bulter.business.autogenerator.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.autogenerator.service.DatasourceInfoService;
import com.lin.bulter.business.autogenerator.utils.SpringTemplateUtils;
import com.lin.bulter.business.autogenerator.utils.StringUtil;
import com.lin.bulter.common.dto.DatasourceInfoParam;
import com.lin.bulter.common.dto.datasourceinfo.DatabaseInfo;
import com.lin.bulter.common.dto.datasourceinfo.DatasourceDto;
import com.lin.bulter.common.dto.datasourceinfo.TableColumnInfo;
import com.lin.bulter.common.dto.datasourceinfo.TableInfo;
import com.lin.bulter.repository.mysql.dao.DatasourceInfoMapper;
import com.lin.bulter.repository.mysql.entity.DatasourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DatasourceServiceImpl implements DatasourceInfoService {

    private static Logger logger = LoggerFactory.getLogger(DatasourceServiceImpl.class);

    @Autowired
    private DatasourceInfoMapper datasourceInfoMapper;

    @Override
    @Transactional
    public Integer insertDatasourceInfo(DatasourceInfo datasourceInfo) {
        return datasourceInfoMapper.insert(datasourceInfo);
    }

    @Override
    @Transactional
    public Integer updateDatasourceInfoById(DatasourceInfo datasourceInfo) {
        return datasourceInfoMapper.updateByPrimaryKeySelective(datasourceInfo);
    }

    @Override
    @Transactional
    public Integer deleteDatasourceInfoById(Integer datasourceInfoId) {
        return datasourceInfoMapper.deleteByPrimaryKey(datasourceInfoId);
    }

    @Override
    public DatasourceInfo selectDatasourceInfoById(Integer datasourceInfoId) {
        return datasourceInfoMapper.selectByPrimaryKey(datasourceInfoId);
    }

    @Override
    public List<DatasourceInfo> selectAllDatasourceInfos() {
        return datasourceInfoMapper.selectAll();
    }

    @Override
    public List<DatasourceDto> getAllDatasources() {
        List<DatasourceDto> result = new ArrayList<>();
        // 查询所有的数据源
        List<DatasourceInfo> datasourceInfos = datasourceInfoMapper.selectAll();
        for(DatasourceInfo datasourceInfo : datasourceInfos) {
            DatasourceDto datasourceDto = new DatasourceDto();
            datasourceDto.setDatasourceId(datasourceInfo.getId());
            datasourceDto.setDatasourceName(datasourceInfo.getDatasourceName());
            // 查询数据源下的所有数据库，以及关联的数据库表
            List<DatabaseInfo> allDbInfosByDatasourceId = getAllDbInfosByDatasourceId(datasourceInfo.getId(), true);
            datasourceDto.setDatabaseInfos(allDbInfosByDatasourceId);
            result.add(datasourceDto);
        }
        return result;
    }

    @Override
    public PageInfo<DatasourceInfo> selectDatasourceInfoByPage(DatasourceInfoParam datasourceInfoParam) {
        PageInfo<DatasourceInfo> datasourceInfos = PageHelper.startPage(datasourceInfoParam.getPage().getPageNum(), datasourceInfoParam.getPage().getPageSize())
                .doSelectPageInfo(() -> datasourceInfoMapper.selectByCondition(datasourceInfoParam));
        return datasourceInfos;
    }

    @Override
    public List<DatabaseInfo> getAllDbInfosByDatasourceId(Integer datasourceId,  boolean isAllInfos) {
        // 查询数据源
        DatasourceInfo datasourceInfo = selectDatasourceInfoById(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询所有的数据库
        String sql = "select schema_name as db_name from schemata";
        List<DatabaseInfo> databases = instance.queryList(sql, new ArrayList<>(), DatabaseInfo.class);
        List<DatabaseInfo> newDatabases = databases.stream().filter(item->!item.getDbName().equals("information_schema") &&
                !item.getDbName().equals("performance_schema") && !item.getDbName().equals("sys") && !item.getDbName().equals("mysql")).collect(Collectors.toList());
        if(isAllInfos){
            List<String> dbNames = newDatabases.stream().map(item -> item.getDbName()).collect(Collectors.toList());
            // 查询数据库下的所有表
            List<TableInfo> allTableInfosByDbNames = getAllTableInfosByDbNames(datasourceId, dbNames);
            // 查询数据库下所有的字段
            List<TableColumnInfo> allTableColumnsByDbNames = getAllTableColumnsByDbNames(datasourceId, dbNames);
            // tableInfo中拼接tableColumn
            Map<String, List<TableColumnInfo>> tableNameMap = allTableColumnsByDbNames.stream().collect(Collectors.groupingBy(TableColumnInfo::getOwnTableName));
            for(TableInfo tableInfo : allTableInfosByDbNames){
                tableInfo.setColumns(tableNameMap.get(tableInfo.getTableName()));
            }
            // databaseInfo拼装tableList
            Map<String, List<TableInfo>> dbNameMap = allTableInfosByDbNames.stream().collect(Collectors.groupingBy(TableInfo::getOwnDbName));
            for (DatabaseInfo dbInfo : newDatabases) {
                List<TableInfo> tableInfos = dbNameMap.get(dbInfo.getDbName());
                dbInfo.setTableList(tableInfos);
            }
        }
        return newDatabases;
    }

    @Override
    public List<TableInfo> getAllTableInfosByDbName(Integer datasourceId, String databaseName) {
        // 查询数据源
        DatasourceInfo datasourceInfo = selectDatasourceInfoById(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询数据库下的所有的表
        String sql3 = "select table_schema as own_db_name, table_name,table_comment from tables where table_schema = ?";
        List<TableInfo> tables = instance.queryList(sql3, Arrays.asList(databaseName), TableInfo.class);
        // 查询数据库下的所有字段
        List<TableColumnInfo> allTableColumnsByDbNames = getAllTableColumnsByDbNames(datasourceId, Arrays.asList(databaseName));
        // tableInfo中拼接tableColumn
        Map<String, List<TableColumnInfo>> tableNameMap = allTableColumnsByDbNames.stream().collect(Collectors.groupingBy(TableColumnInfo::getOwnTableName));
        for(TableInfo tableInfo : tables){
            tableInfo.setColumns(tableNameMap.get(tableInfo.getTableName()));
        }
        return tables;
    }

    public List<TableInfo> getAllTableInfosByDbNames(Integer datasourceId, List<String> databaseNameList) {
        String dbNames = StringUtil.transformListToStr(databaseNameList);
        // 查询数据源
        DatasourceInfo datasourceInfo = selectDatasourceInfoById(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询所有的表
        String sql3 = "select table_schema as own_db_name, table_name,table_comment from tables where table_schema in (" + dbNames + ")";
        List<TableInfo> tables = instance.queryList(sql3, Arrays.asList(), TableInfo.class);
        return tables;
    }

    @Override
    public List<TableColumnInfo> getAllTableColumnsByTableName(Integer datasourceId, String databaseName, String tableName) {
        // 查询数据源
        DatasourceInfo datasourceInfo = selectDatasourceInfoById(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询所有的表字段信息

        String sql2 = "select distinct column_name, data_type, column_comment, table_name as own_table_name from columns where table_schema = ? and table_name = ?";
        List<TableColumnInfo> tableColumns = instance.queryList(sql2, Arrays.asList(databaseName, tableName), TableColumnInfo.class);
        for (TableColumnInfo column : tableColumns) {
            column.setFieldName(StringUtil.camelName(column.getColumnName()));
            column.setFieldType(StringUtil.sqlType2JavaType(column.getDataType()));
        }
        return tableColumns;
    }

    public List<TableColumnInfo> getAllTableColumnsByDbNames(Integer datasourceId, List<String> databaseNameList) {
        String dbNames = StringUtil.transformListToStr(databaseNameList);
        // 查询数据源
        DatasourceInfo datasourceInfo = selectDatasourceInfoById(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询所有的表字段信息
        String sql2 = "select distinct column_name, table_name as own_table_name, data_type, column_comment from columns where table_schema in (" + dbNames + ")";
        List<TableColumnInfo> tableColumns = instance.queryList(sql2, Arrays.asList(), TableColumnInfo.class);
        for (TableColumnInfo column : tableColumns) {
            column.setFieldName(StringUtil.camelName(column.getColumnName()));
            column.setFieldType(StringUtil.sqlType2JavaType(column.getDataType()));
        }
        return tableColumns;
    }
}
