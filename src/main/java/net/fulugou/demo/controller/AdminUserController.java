package net.fulugou.demo.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.fulugou.demo.model.AdminUser;
import net.fulugou.demo.service.AdminUserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;

@Api(value = "用户相关接口", description = "")
@RestController
public class AdminUserController {

    @Autowired
    AdminUserService adminUser;

    private final static Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @ApiOperation(value="登录接口", notes="", response = String.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", paramType = "query", defaultValue = "123456"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "query",defaultValue = "123456"),
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(AdminUser adminUser) throws Exception{
        return this.adminUser.login(adminUser.getUsername(), adminUser.getPassword(), "123454");
    }

    @ApiOperation(value="注册接口", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "账号", required = true, dataType = "string", paramType = "query", defaultValue = ""),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string", paramType = "query",defaultValue = ""),
            @ApiImplicitParam(name = "email", value = "email", required = true, dataType = "string", paramType = "query",defaultValue = "email"),
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(AdminUser adminUser) throws Exception{
        return this.adminUser.register(adminUser.getUsername(), adminUser.getPassword(), adminUser.getEmail());
    }

    @ApiOperation(value="用户列表接口", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "token", required = true, dataType = "string", paramType = "header", defaultValue = ""),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, dataType = "int", paramType = "query", defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", required = true, dataType = "int", paramType = "query",defaultValue = "10"),
    })
    @GetMapping("/users")
    @RequiresPermissions("user:list")
    public ResponseEntity<?> user(ServletRequest request) {
//        Boolean a = SecurityUtils.getSubject().isPermitted("user:list");
//        log.info(JSON.toJSONString(a));
//        log.info((String) request.getAttribute("username"));
        return this.adminUser.findAll();
    }

}
