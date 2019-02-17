package com.lin.bulter.business.service;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.common.dto.UserDto;
import com.lin.bulter.common.dto.UserParam;
import com.lin.bulter.repository.mysql.entity.User;

import java.util.List;

public interface UserService {

    /**
     * 新增
     *
     * @return
     */
    Integer insertUser(User user);

    /**
     * 修改
     *
     * @return
     */
    Integer updateUserById(User user);

    /**
     * 删除
     */
    Integer deleteUserById(Integer userId);

    /**
     * 按主键查询
     */
    User selectUserById(Integer userId);

    /**
     * 查询所有
     */
    List<User> selectAllUsers();

    /**
     * 分页查询
     */
    PageInfo<User> selectUserByPage(UserParam userParam);

    /**
     * 保存user登录信息，返回token
     */
    String generateJwtToken(String username);

    /**
     * 获取上次token生成时的salt值和登录用户信息
     * @param username
     * @return
     */
    UserDto getJwtTokenInfo(String username);

    /**
     * 清除token信息
     * @param username 登录用户名
     */
    void deleteLoginInfo(String username);

    /**
     * 获取数据库中保存的用户信息，主要是加密后的密码
     * @param userName
     * @return
     */
    UserDto getUserInfo(String userName);

    /**
     * 获取用户角色列表，强烈建议从缓存中获取
     * @param userId
     * @return
     */
    List<String> getUserRoles(Integer userId);
}
