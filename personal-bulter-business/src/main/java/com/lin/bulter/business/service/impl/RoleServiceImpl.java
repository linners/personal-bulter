package com.lin.bulter.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.RoleService;
import com.lin.bulter.common.dto.RoleParam;
import com.lin.bulter.repository.mysql.dao.RoleMapper;
import com.lin.bulter.repository.mysql.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    @Transactional
    public Integer insertRole(Role role) {
        return roleMapper.insert(role);
    }

    @Override
    @Transactional
    public Integer updateRoleById(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    @Transactional
    public Integer deleteRoleById(Integer roleId) {
        return roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public Role selectRoleById(Integer roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public List<Role> selectAllRoles() {
        return roleMapper.selectAll();
    }

    @Override
    public PageInfo<Role> selectRoleByPage(RoleParam roleParam) {
        PageInfo<Role> roles = PageHelper.startPage(roleParam.getPage().getPageNum(), roleParam.getPage().getPageSize())
                .doSelectPageInfo(() -> roleMapper.selectByCondition(roleParam));
        return roles;
    }
}
