package com.lin.bulter.web.shiro.filter;

import com.lin.bulter.business.service.UserService;
import com.lin.bulter.common.dto.UserDto;
import com.lin.bulter.common.utils.JwtUtils;
import com.lin.bulter.web.shiro.JWTToken;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class JwtAuthFilter extends AuthenticatingFilter {

    private final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final int tokenRefreshInterval = 300;
    private UserService userService;

    public JwtAuthFilter(UserService userService) {
        this.userService = userService;
        this.setLoginUrl("/login");
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) //对于OPTION请求做拦截，不做token校验
            return false;

        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) {
        this.fillCorsHeader(WebUtils.toHttp(request), WebUtils.toHttp(response));
        request.setAttribute("jwtShiroFilter.FILTERED", true);
    }

    /**
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (this.isLoginRequest(request, response))
            return true;
        Boolean afterFiltered = (Boolean) (request.getAttribute("jwtShiroFilter.FILTERED"));
        if (BooleanUtils.isTrue(afterFiltered))
            return true;

        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException e) { //not found any token
            log.error("Not found any token");
        } catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        return allowed || super.isPermissive(mappedValue);
    }

    /**
     * 这里重写了父类的方法，使用我们自己定义的Token类，提交给shiro。这个方法返回null的话会直接抛出异常，进入isAccessAllowed（）的异常处理逻辑。
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        String jwtToken = getAuthzHeader(servletRequest);
        if (StringUtils.isNotBlank(jwtToken) && !JwtUtils.isTokenExpired(jwtToken))
            return new JWTToken(jwtToken);

        return null;
    }

    /**
     * 如果这个Filter在之前isAccessAllowed（）方法中返回false,则会进入这个方法。我们这里直接返回错误的response
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(servletResponse);
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION);
        fillCorsHeader(WebUtils.toHttp(servletRequest), httpResponse);
        return false;
    }

    /**
     *  如果Shiro Login认证成功，会进入该方法，等同于用户名密码登录成功，我们这里还判断了是否要刷新Token
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        String newToken = null;
        if (token instanceof JWTToken) {
            JWTToken jwtToken = (JWTToken) token;
            UserDto user = (UserDto) subject.getPrincipal();
            boolean shouldRefresh = shouldTokenRefresh(JwtUtils.getIssuedAt(jwtToken.getToken()));
            if (shouldRefresh) {
                newToken = userService.generateJwtToken(user.getUsername());
            }
        }
        if (StringUtils.isNotBlank(newToken))
            httpResponse.setHeader("x-auth-token", newToken);

        return true;
    }

    /**
     * 如果调用shiro的login认证失败，会回调这个方法，这里我们什么都不做，因为逻辑放到了onAccessDenied（）中。
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        log.error("Validate token fail, token:{}, error:{}", token.toString(), e.getMessage());
        return false;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String header = httpRequest.getHeader("x-auth-token");
        return StringUtils.removeStart(header, "Bearer ");
    }

    protected boolean shouldTokenRefresh(Date issueAt) {
        LocalDateTime issueTime = LocalDateTime.ofInstant(issueAt.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.now().minusSeconds(tokenRefreshInterval).isAfter(issueTime);
    }

    protected void fillCorsHeader(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,HEAD");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
    }
}
