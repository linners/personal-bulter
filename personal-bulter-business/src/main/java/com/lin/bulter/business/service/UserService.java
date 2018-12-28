package com.lin.bulter.business.service;

import com.github.pagehelper.PageInfo;
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
    Integer updateByUserId(User user);

    /**
     * 删除
     */
    Integer deleteByUserId(Integer userId);

    /**
     * 查询所有
     */
    List<User> selectAllUsers();

    /**
     * 分页查询
     */
    PageInfo<User> selectUserByPage(UserParam userParam);
}
