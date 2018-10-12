package net.fulugou.demo.shiro;//package net.fulugou.demo.shiro;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.ExpiredJwtException;
import net.fulugou.demo.util.ApiResponse;
import net.fulugou.demo.util.BaseUtils;
import net.fulugou.demo.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import org.apache.shiro.web.filter.AccessControlFilter;

/**
 * Created by zero on 2018/10/9.
 */
public class JWTAccessControllerFilter extends AccessControlFilter {

    private final static Logger log = LoggerFactory.getLogger(JWTAccessControllerFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String requestToken = httpRequest.getHeader("Authorization");

        if (BaseUtils.isNullOrEmpty(requestToken)) {
            httpResponse.sendRedirect("/401");
            return false;
        }

        try {
            Map<String, Object> map = JWTUtil.parserJwt(requestToken);
            JWTAuthenticationToken token = new JWTAuthenticationToken((Long) map.get("user_id"), (String) map.get("username"), (String) map.get("password"));
            // 委托给Realm进行登录
            getSubject(servletRequest, servletResponse).login(token);

            // 获取当前请求的用户
            Subject currentUser = SecurityUtils.getSubject();
            JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) currentUser.getPrincipal();

            // 将登录后的用户信息写入请求中方便之后使用
            servletRequest.setAttribute("user_id", jwtAuthenticationToken.getUserId());
            servletRequest.setAttribute("username", jwtAuthenticationToken.getUsername());

        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            httpResponse.sendRedirect("/401");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            httpResponse.sendRedirect("/401");
            return false;
        }

        return true;
    }

//    @Override
//    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse res = (HttpServletResponse) response;
//        res.setHeader("Access-control-Allow-Origin", req.getHeader("Origin"));
//        res.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,PATCH,DELETE");
//        res.setHeader("Access-Control-Allow-Headers", req.getHeader("Access-Control-Request-Headers"));
//        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
//        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
//            res.setStatus(HttpStatus.OK.value());
//            return false;
//        }
//        return true;
//    }
}
