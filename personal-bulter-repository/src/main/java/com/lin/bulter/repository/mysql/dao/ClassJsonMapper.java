package com.lin.bulter.repository.mysql.dao;

import com.lin.bulter.common.param.ClassJsonParam;
import com.lin.bulter.repository.mysql.entity.ClassJson;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassJsonMapper {
    
    /**
     *  按主键删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  插入一条记录
     */
    int insert(ClassJson record);

    /**
     *  按主键查询
     */
    ClassJson selectByPrimaryKey(Integer id);

    /**
     *  查询所有记录
     */
    List<ClassJson> selectAll();

    /**
     *  按主键更新
     */
    int updateByPrimaryKeySelective(ClassJson record);

    /**
     *  按条件分页查询
     */
    List<ClassJson> selectByCondition(@Param("param") ClassJsonParam classJsonParam);
}
