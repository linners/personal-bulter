package com.lin.bulter.web.shiro.realm;

import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserDto;
import com.lin.bulter.common.utils.JwtUtils;
import com.lin.bulter.web.shiro.JWTCredentialsMatcher;
import com.lin.bulter.web.shiro.JWTToken;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义身份认证
 * 基于HMAC（ 散列消息认证码）的控制域
 */

public class JWTShiroRealm extends AuthorizingRealm {
    private final Logger log = LoggerFactory.getLogger(JWTShiroRealm.class);

    @Autowired
    protected UserService userService;

    public JWTShiroRealm(UserService userService) {
        //这里使用我们自定义的Matcher
        this.setCredentialsMatcher(new JWTCredentialsMatcher());
    }

    /**
     * 限定这个Realm只支持我们自定义的JWT Token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     * 更controller登录一样，也是获取用户的salt值，给到shiro，由shiro来调用matcher来做认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        JWTToken jwtToken = (JWTToken) authcToken;
        String token = jwtToken.getToken();

        UserDto user = userService.getJwtTokenInfo(JwtUtils.getUsername(token));
        if (user == null)
            throw new AuthenticationException("token过期，请重新登录");

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getSalt(), "jwtRealm");

        return authenticationInfo;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }
}
