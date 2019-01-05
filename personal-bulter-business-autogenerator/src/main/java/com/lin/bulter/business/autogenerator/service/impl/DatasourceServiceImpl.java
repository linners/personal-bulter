package com.lin.bulter.business.autogenerator.service.impl;

import com.lin.bulter.business.autogenerator.DatasourceService;
import com.lin.bulter.business.autogenerator.model.DatabaseInfo;
import com.lin.bulter.business.autogenerator.model.TableColumn;
import com.lin.bulter.business.autogenerator.model.TableInfo;
import com.lin.bulter.business.autogenerator.utils.SpringTemplateUtils;
import com.lin.bulter.business.autogenerator.utils.StringUtil;
import com.lin.bulter.repository.mysql.dao.DatasourceInfoMapper;
import com.lin.bulter.repository.mysql.entity.DatasourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DatasourceServiceImpl implements DatasourceService {

    @Autowired
    private DatasourceInfoMapper datasourceInfoMapper;

    @Override
    public List<DatasourceInfo> getAllDatasources() {
        return datasourceInfoMapper.selectAll();
    }

    @Override
    public DatasourceInfo getDatasourceByPrimaryKey(Integer datasourceId) {
        return datasourceInfoMapper.selectByPrimaryKey(datasourceId);
    }

    @Override
    public List<DatabaseInfo> getAllDbInfosByDatasourceId(Integer datasourceId) {
        // 查询数据源
        DatasourceInfo datasourceInfo = getDatasourceByPrimaryKey(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询所有的数据库
        String sql = "select schema_name as db_name from schemata";
        List<DatabaseInfo> databases = instance.queryList(sql, new ArrayList<>(), DatabaseInfo.class);
        return databases;
    }

    @Override
    public List<TableInfo> getAllTableInfosByDbName(Integer datasourceId, String databaseName) {
        // 查询数据源
        DatasourceInfo datasourceInfo = getDatasourceByPrimaryKey(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询所有的表
        String sql3 = "select table_name,table_comment from tables where table_schema = ?";
        List<TableInfo> tables = instance.queryList(sql3, Arrays.asList(databaseName), TableInfo.class);
        return tables;
    }

    @Override
    public List<TableColumn> getAllTableColumnsByTableName(Integer datasourceId, String databaseName, String tableName) {
        // 查询数据源
        DatasourceInfo datasourceInfo = getDatasourceByPrimaryKey(datasourceId);
        // 获得springTemplate实例
        SpringTemplateUtils instance = SpringTemplateUtils.getInstance(datasourceInfo.getDbIp(), datasourceInfo.getDbPort(), "information_schema",
                datasourceInfo.getUserName(), datasourceInfo.getPassword());
        // 查询所有的表字段信息
        String sql2 = "SELECT DISTINCT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT FROM COLUMNS WHERE table_schema = ? and table_name = ?";
        List<TableColumn> tableColumns = instance.queryList(sql2, Arrays.asList(databaseName, tableName), TableColumn.class);
        for (TableColumn column : tableColumns) {
            column.setFieldName(StringUtil.camelName(column.getColumnName()));
            column.setFieldType(StringUtil.sqlType2JavaType(column.getDataType()));
        }
        return tableColumns;
    }
}
