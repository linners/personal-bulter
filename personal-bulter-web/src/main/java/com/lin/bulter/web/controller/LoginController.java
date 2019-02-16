package com.lin.bulter.web.controller;


import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserDto;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    private UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户名密码登录
     *
     * @param request
     * @return token
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Void> login(@RequestBody UserDto loginInfo, HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        try {
            //将用户请求参数封装后，直接提交给Shiro处理
            UsernamePasswordToken token = new UsernamePasswordToken(loginInfo.getUsername(), loginInfo.getPassword());
            subject.login(token);
            //Shiro认证通过后会将user信息放到subject内，生成token并返回
            UserDto user = (UserDto) subject.getPrincipal();
            String newToken = userService.generateJwtToken(user.getUsername());
            response.setHeader("x-auth-token", newToken);

            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            // 如果校验失败，shiro会抛出异常，返回客户端失败
            logger.error("User {} login fail, Reason:{}", loginInfo.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            UserDto user = (UserDto) subject.getPrincipals().getPrimaryPrincipal();
            userService.deleteLoginInfo(user.getUsername());
        }
        SecurityUtils.getSubject().logout();
        return ResponseEntity.ok().build();
    }

}
