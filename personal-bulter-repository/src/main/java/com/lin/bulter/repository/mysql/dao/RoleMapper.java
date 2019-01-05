package com.lin.bulter.repository.mysql.dao;

import com.lin.bulter.common.dto.RoleParam;
import com.lin.bulter.repository.mysql.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    
    /**
     *  按主键删除
     */
    int deleteByPrimaryKey(Integer id);

    /**
     *  插入一条记录
     */
    int insert(Role record);

    /**
     *  按主键查询
     */
    Role selectByPrimaryKey(Integer id);

    /**
     *  查询所有记录
     */
    List<Role> selectAll();

    /**
     *  按主键更新
     */
    int updateByPrimaryKeySelective(Role record);

    /**
     *  按条件分页查询
     */
    List<Role> selectByCondition(@Param("param") RoleParam roleParam);
}
