package com.lin.bulter.web.controller;


import com.alibaba.fastjson.JSONObject;
import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserDto;
import com.lin.bulter.common.utils.HttpClientPool;
import com.lin.bulter.common.utils.WebResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/unauthorized")
    public JSONObject unauthorized() {
        return WebResultUtil.render(HttpStatus.UNAUTHORIZED.value(), "token错误", null);
    }

    @GetMapping("/noLogin/{code}")
    public JSONObject noLogin(@PathVariable("code") String code){
        String appid = "wxa3c6da3c2cf43617";
        String appsecret = "dd3eb4be234c218bbbe5e3df471faf4e";
        String granttype = "authorization_code";
        String url = "https://api.weixin.qq.com/sns/jscode2session?"+"appid="+appid+"&secret="+appsecret+"&js_code="+code+"&grant_type"+granttype;
        String result = HttpClientPool.sendGet(url);
        System.out.println(result);
        return WebResultUtil.render(HttpStatus.OK.value(), "success", result);
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
            return WebResultUtil.render(HttpStatus.OK.value(), "success", null);
        } catch (AuthenticationException e) {
            // 如果校验失败，shiro会抛出异常，返回客户端失败
            logger.error("User {} login fail, Reason:{}", loginInfo.getUserName(), e.getMessage());
            return WebResultUtil.render(HttpStatus.UNAUTHORIZED.value(), "用户名密码错误", null);
        } catch (Exception e) {
            return WebResultUtil.render(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统出错", null);
        }
    }

    /**
     * 退出登录
     *
     * @return
     */
    @RequiresRoles("admin")
    @GetMapping(value = "/logout")
    public JSONObject logout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getPrincipals() != null) {
            UserDto user = (UserDto) subject.getPrincipals().getPrimaryPrincipal();
            userService.deleteLoginInfo(user.getUserName());
        }
        SecurityUtils.getSubject().logout();
        return WebResultUtil.render(HttpStatus.OK.value(), "退出登录成功", null);
    }

}
