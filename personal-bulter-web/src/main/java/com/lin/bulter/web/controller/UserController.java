package com.lin.bulter.web.controller;

import com.github.pagehelper.PageInfo;
import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserParam;
import com.lin.bulter.repository.mysql.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bulter/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/getAllUser")
    public List<User> getAllUser() {
        List<User> result = userService.selectAllUsers();
        return result;
    }

    @PostMapping("/getUserByPage")
    public PageInfo<User> getUserByPage(UserParam param) {
        PageInfo<User> result = userService.selectUserByPage(param);
        return result;
    }
}
