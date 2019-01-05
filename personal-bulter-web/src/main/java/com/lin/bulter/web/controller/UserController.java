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

    /**
     * 新增
     * @return
     */
    @PostMapping("/save")
    public Integer saveUser(@RequestBody User user) {
        Integer result = userService.insertUser(user);
        return result;
    }

    /**
     * 修改
     * @return
     */
    @PostMapping("/update")
    public Integer updateUserById(@RequestBody User user) {
        Integer result = userService.updateUserById(user);
        return result;
    }

    /**
     * 删除
     * @return
     */
    @PostMapping("/delete/{userId}")
    public Integer deleteUserById(@PathVariable("userId") Integer userId) {
        Integer result = userService.deleteUserById(userId);
        return result;
    }

    /**
     * 按主键查询
     * @return
     */
    @GetMapping("/getUserById/{userId}")
    public User getUserById(@PathVariable("userId") Integer userId) {
        User result = userService.selectUserById(userId);
        return result;
    }

    /**
     * 查询所有
     * @return
     */
    @GetMapping("/getAllUsers")
    public List<User> getAllUsers() {
        List<User> result = userService.selectAllUsers();
        return result;
    }

    /**
     * 分页查询
     * @param param
     * @return
     */
    @PostMapping("/getUserByPage")
    public PageInfo<User> getUserByPage(UserParam param) {
        PageInfo<User> result = userService.selectUserByPage(param);
        return result;
    }
}
