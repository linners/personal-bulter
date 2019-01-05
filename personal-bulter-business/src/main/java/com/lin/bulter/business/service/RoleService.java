package com.lin.bulter.business.service;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.common.dto.RoleParam;
import com.lin.bulter.repository.mysql.entity.Role;

import java.util.List;

public interface RoleService {

    /**
     * 新增
     *
     * @return
     */
    Integer insertRole(Role role);

    /**
     * 修改
     *
     * @return
     */
    Integer updateRoleById(Role role);

    /**
     * 删除
     */
    Integer deleteRoleById(Integer roleId);

    /**
     * 按主键查询
     */
    Role selectRoleById(Integer roleId);

    /**
     * 查询所有
     */
    List<Role> selectAllRoles();

    /**
     * 分页查询
     */
    PageInfo<Role> selectRoleByPage(RoleParam roleParam);
}
