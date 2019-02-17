package com.lin.bulter.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserDto;
import com.lin.bulter.common.utils.WebResultUtil;
import com.lin.bulter.repository.redis.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/401", method = RequestMethod.GET)
    public JSONObject handle401() {
        return WebResultUtil.renderSuccess(HttpStatus.OK.value(), "token错误");
    }

    @RequestMapping(value = "/notLogin", method = RequestMethod.GET)
    public JSONObject notLogin() {
        return WebResultUtil.renderSuccess(HttpStatus.OK.value(), "您尚未登陆");
    }

    @RequestMapping(value = "/notRole", method = RequestMethod.GET)
    public JSONObject notRole() {
        return WebResultUtil.renderSuccess(HttpStatus.OK.value(), "您没有权限");
    }

    /**
     * 用户名密码登录
     *
     * @param request
     * @return token
     */
    @PostMapping(value = "/login")
    public JSONObject login(@RequestBody UserDto loginInfo, HttpServletRequest request, HttpServletResponse response) {
        Subject subject = SecurityUtils.getSubject();
        try {
            //将用户请求参数封装后，直接提交给Shiro处理
            UsernamePasswordToken token = new UsernamePasswordToken(loginInfo.getUserName(), loginInfo.getPassWord());
            subject.login(token);
            //Shiro认证通过后会将user信息放到subject内，生成token并返回
            UserDto user = (UserDto) subject.getPrincipal();
            String newToken = userService.generateJwtToken(user.getUserName());
            response.setHeader("x-auth-token", newToken);
            return WebResultUtil.renderSuccess(HttpStatus.OK.value(), "success");
            //return ResponseEntity.status(HttpStatus.OK).build();
        } catch (AuthenticationException e) {
            // 如果校验失败，shiro会抛出异常，返回客户端失败
            logger.error("User {} login fail, Reason:{}", loginInfo.getUserName(), e.getMessage());
            return WebResultUtil.renderSuccess(HttpStatus.UNAUTHORIZED.value(), "用户名密码错误");
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            return WebResultUtil.renderSuccess(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统出错");
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @GetMapping(value = "/logout")
    public JSONObject logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            UserDto user = (UserDto) subject.getPrincipals().getPrimaryPrincipal();
            userService.deleteLoginInfo(user.getUserName());
        }
        SecurityUtils.getSubject().logout();
        return WebResultUtil.renderSuccess(HttpStatus.OK.value(), "退出登录成功");
    }

}
