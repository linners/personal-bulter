package com.lin.bulter.web.controller;

import com.lin.bulter.business.service.UserService;
import com.lin.bulter.repository.mysql.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bulter/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public List<User> TestCon() {
        List<User> result = userService.selectAllUsers();
        System.out.println(result);
        return result;
    }
}
