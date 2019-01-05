package com.lin.bulter.web.controller;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.RoleService;
import com.lin.bulter.common.dto.RoleParam;
import com.lin.bulter.repository.mysql.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bulter/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 新增
     * @return
     */
    @PostMapping("/save")
    public Integer saveRole(@RequestBody Role role) {
        Integer result = roleService.insertRole(role);
        return result;
    }

    /**
     * 修改
     * @return
     */
    @PostMapping("/update")
    public Integer updateRoleById(@RequestBody Role role) {
        Integer result = roleService.updateRoleById(role);
        return result;
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/delete/{roleId}")
    public Integer deleteRoleById(@PathVariable("roleId") Integer roleId) {
        Integer result = roleService.deleteRoleById(roleId);
        return result;
    }

    /**
     * 按主键查询
     * @return
     */
    @GetMapping("/getRoleById/{roleId}")
    public Role getRoleById(@PathVariable("roleId") Integer roleId) {
        Role result = roleService.selectRoleById(roleId);
        return result;
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/getAllRoles")
    public List<Role> getAllRoles() {
        List<Role> result = roleService.selectAllRoles();
        return result;
    }

    /**
     * 分页查询
     * @param param
     * @return
     */
    @PostMapping("/getRoleByPage")
    public PageInfo<Role> getRoleByPage(RoleParam param) {
        PageInfo<Role> result = roleService.selectRoleByPage(param);
        return result;
    }
}
