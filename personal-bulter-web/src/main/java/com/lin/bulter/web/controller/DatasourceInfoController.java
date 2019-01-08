package com.lin.bulter.web.controller;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.autogenerator.service.DatasourceInfoService;
import com.lin.bulter.common.dto.DatasourceInfoParam;
import com.lin.bulter.repository.mysql.entity.DatasourceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bulter/datasourceInfo")
public class DatasourceInfoController {

    @Autowired
    private DatasourceInfoService datasourceInfoService;

    /**
     * 新增
     *
     * @return
     */
    @PostMapping("/save")
    public Integer saveDatasourceInfo(@RequestBody DatasourceInfo datasourceInfo) {
        Integer result = datasourceInfoService.insertDatasourceInfo(datasourceInfo);
        return result;
    }

    /**
     * 修改
     *
     * @return
     */
    @PostMapping("/update")
    public Integer updateDatasourceInfoById(@RequestBody DatasourceInfo datasourceInfo) {
        Integer result = datasourceInfoService.updateDatasourceInfoById(datasourceInfo);
        return result;
    }

    /**
     * 删除
     *
     * @return
     */
    @PostMapping("/delete/{datasourceInfoId}")
    public Integer deleteDatasourceInfoById(@PathVariable("datasourceInfoId") Integer datasourceInfoId) {
        Integer result = datasourceInfoService.deleteDatasourceInfoById(datasourceInfoId);
        return result;
    }

    /**
     * 按主键查询
     *
     * @return
     */
    @GetMapping("/getDatasourceInfoById/{datasourceInfoId}")
    public DatasourceInfo getDatasourceInfoById(@PathVariable("datasourceInfoId") Integer datasourceInfoId) {
        DatasourceInfo result = datasourceInfoService.selectDatasourceInfoById(datasourceInfoId);
        return result;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping("/getAllDatasourceInfos")
    public List<DatasourceInfo> getAllDatasourceInfos() {
        List<DatasourceInfo> result = datasourceInfoService.selectAllDatasourceInfos();
        return result;
    }

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    @PostMapping("/getDatasourceInfoByPage")
    public PageInfo<DatasourceInfo> getDatasourceInfoByPage(DatasourceInfoParam param) {
        PageInfo<DatasourceInfo> result = datasourceInfoService.selectDatasourceInfoByPage(param);
        return result;
    }


//    /**
//     * 查询所有的数据源，包括数据库，以及表结构
//     *
//     * @return
//     */
//    @GetMapping("/getAllDatasources")
//    public List<DatasourceCascader> getAllDatasources() {
//        // datasource cascader
//        List<DatasourceCascader> datasourceCascaders = new ArrayList<>();
//        List<DatasourceDto> result = datasourceInfoService.getAllDatasources();
//        for(DatasourceDto datasourceDto : result) {
//            DatasourceCascader datasourceCascader = new DatasourceCascader();
//            datasourceCascader.setValue(datasourceDto.getDatasourceId().toString());
//            datasourceCascader.setLabel(datasourceDto.getDatasourceName());
//            List<DatabaseInfo> databaseInfoList = datasourceDto.getDatabaseInfos();
//            // db cascader
//            List<DatasourceCascader> dbCascaders = new ArrayList<>();
//            for(DatabaseInfo databaseInfo : databaseInfoList){
//                DatasourceCascader dbCascader = new DatasourceCascader();
//                dbCascader.setValue(databaseInfo.getDbName());
//                dbCascader.setLabel("数据库 "+databaseInfo.getDbName());
//                List<TableInfo> tableList = databaseInfo.getTableList();
//                // table cascader
//                List<DatasourceCascader> tableCascaders = new ArrayList<>();
//                for(TableInfo tableInfo : tableList) {
//                    DatasourceCascader tableCascader = new DatasourceCascader();
//                    tableCascader.setValue(tableInfo.getTableName());
//                    tableCascader.setLabel("表 " + tableInfo.getTableName());
//                    tableCascaders.add(tableCascader);
//                }
//                dbCascader.setChildren(tableCascaders);
//                dbCascaders.add(dbCascader);
//            }
//            datasourceCascader.setChildren(dbCascaders);
//            datasourceCascaders.add(datasourceCascader);
//        }
//        return datasourceCascaders;
//    }
//
//    /**
//     * 查询数据源下所有的数据库
//     *
//     * @param datasourceId
//     * @return
//     */
//    @GetMapping("/getAllDbInfosByDatasourceId/{datasourceId}")
//    List<DatabaseInfo> getAllDbInfosByDatasourceId(@PathVariable("datasourceId") Integer datasourceId) {
//        List<DatabaseInfo> result = datasourceInfoService.getAllDbInfosByDatasourceId(datasourceId, true);
//        return result;
//    }
//
//    /**
//     * 查询某个数据库下的所有表
//     *
//     * @param datasourceId
//     * @return
//     */
//    @PostMapping("/getAllTableInfosByDbName")
//    List<TableInfo> getAllTableInfosByDbName(@RequestBody DataSourceVo dataSourceVo) {
//        List<TableInfo> result = datasourceInfoService.getAllTableInfosByDbName(dataSourceVo.getDatasourceId(), dataSourceVo.getDatabaseName());
//        return result;
//    }
//
//    /**
//     * 查询某个表的所有字段
//     *
//     * @param datasourceId
//     * @return
//     */
//    @PostMapping("/getAllTableColumnsByTableName")
//    List<TableColumnInfo> getAllTableColumnsByTableName(@RequestBody DataSourceVo dataSourceVo) {
//        List<TableColumnInfo> result = datasourceInfoService.getAllTableColumnsByTableName(dataSourceVo.getDatasourceId(), dataSourceVo.getDatabaseName(), dataSourceVo.getTableName());
//        return result;
//    }
}
