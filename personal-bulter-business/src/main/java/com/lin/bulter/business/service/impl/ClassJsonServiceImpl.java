package com.lin.bulter.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.ClassJsonService;
import com.lin.bulter.common.param.ClassJsonParam;
import com.lin.bulter.repository.mysql.dao.ClassJsonMapper;
import com.lin.bulter.repository.mysql.entity.ClassJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassJsonServiceImpl implements ClassJsonService {

    @Autowired
    private ClassJsonMapper classJsonMapper;

    @Override
    @Transactional
    public Integer insertClassJson(ClassJson classJson) {
        return classJsonMapper.insert(classJson);
    }

    @Override
    @Transactional
    public Integer updateClassJsonById(ClassJson classJson) {
        return classJsonMapper.updateByPrimaryKeySelective(classJson);
    }

    @Override
    @Transactional
    public Integer deleteClassJsonById(Integer classJsonId) {
        return classJsonMapper.deleteByPrimaryKey(classJsonId);
    }

    @Override
    public ClassJson selectClassJsonById(Integer classJsonId) {
        return classJsonMapper.selectByPrimaryKey(classJsonId);
    }

    @Override
    public List<ClassJson> selectAllClassJsons() {
        return classJsonMapper.selectAll();
    }

    @Override
    public PageInfo<ClassJson> selectClassJsonByPage(ClassJsonParam classJsonParam) {
        PageInfo<ClassJson> classJsons = PageHelper.startPage(classJsonParam.getPage().getPageNum(), classJsonParam.getPage().getPageSize())
                .doSelectPageInfo(() -> classJsonMapper.selectByCondition(classJsonParam));
        return classJsons;
    }
}
