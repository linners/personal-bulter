package com.lin.bulter.business.autogenerator.service.impl;

import com.lin.bulter.business.autogenerator.GeneratorService;
import com.lin.bulter.business.autogenerator.model.DatabaseInfo;
import com.lin.bulter.business.autogenerator.model.TableColumn;
import com.lin.bulter.business.autogenerator.model.TableInfo;
import com.lin.bulter.common.dto.autogenerator.CurdParam;
import com.lin.bulter.common.dto.autogenerator.GenerateParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneratorServiceImpl implements GeneratorService {

    private static Logger logger = LoggerFactory.getLogger(GeneratorServiceImpl.class);

    @Autowired
    private ProjectGeneratorServiceImpl projectGeneratorService;
    @Autowired
    private CurdGeneratorServiceImpl curdGeneratorService;

    @Override
    public String generatorProject(GenerateParam param) {
        return projectGeneratorService.generatorProject(param);
    }

    @Override
    public String generatorCrud(CurdParam param) {
        return curdGeneratorService.generatorCrud(param);
    }

    @Override
    public List<DatabaseInfo> getAllDatabaseInfos(Integer dataserceId) {
        return null;
    }

    @Override
    public List<TableInfo> getTablesByDb(String dbName) {
        return null;
    }

    @Override
    public List<TableColumn> getTableColumns(String tableName) {
        return null;
    }
}
