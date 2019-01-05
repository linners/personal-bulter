package com.lin.bulter.business.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserParam;
import com.lin.bulter.repository.mysql.dao.UserMapper;
import com.lin.bulter.repository.mysql.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public Integer insertUser(User user) {
        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public Integer updateUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    @Transactional
    public Integer deleteUserById(Integer userId) {
        return userMapper.deleteByPrimaryKey(userId);
    }

    @Override
    public User selectUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<User> selectAllUsers() {
        return userMapper.selectAll();
    }

    @Override
    public PageInfo<User> selectUserByPage(UserParam userParam) {
        PageInfo<User> users = PageHelper.startPage(userParam.getPage().getPageNum(), userParam.getPage().getPageSize())
                .doSelectPageInfo(() -> userMapper.selectByCondition(userParam));
        return users;
    }
}
