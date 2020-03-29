package com.lin.bulter.business.service;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.common.param.ClassJsonParam;
import com.lin.bulter.repository.mysql.entity.ClassJson;

import java.util.List;

public interface ClassJsonService {

    /**
     * 新增
     *
     * @return
     */
    Integer insertClassJson(ClassJson classJson);

    /**
     * 修改
     *
     * @return
     */
    Integer updateClassJsonById(ClassJson classJson);

    /**
     * 删除
     */
    Integer deleteClassJsonById(Integer classJsonId);

    /**
     * 按主键查询
     */
    ClassJson selectClassJsonById(Integer classJsonId);

    /**
     * 查询所有
     */
    List<ClassJson> selectAllClassJsons();

    /**
     * 分页查询
     */
    PageInfo<ClassJson> selectClassJsonByPage(ClassJsonParam classJsonParam);
}
