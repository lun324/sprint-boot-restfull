package net.fulugou.demo.controller;

import com.alibaba.fastjson.JSON;
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

@RestController
public class AdminUserController {

    @Autowired
    AdminUserService adminUser;

    private final static Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(AdminUser adminUser) throws Exception{
        return this.adminUser.login(adminUser.getUsername(), adminUser.getPassword(), "123454");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(AdminUser adminUser) throws Exception{
        return this.adminUser.register(adminUser.getUsername(), adminUser.getPassword(), adminUser.getEmail());
    }

    @GetMapping("/users")
    @RequiresPermissions("user:list")
    public ResponseEntity<?> user(ServletRequest request) {

        Boolean a = SecurityUtils.getSubject().isPermitted("user:list");
        log.info(JSON.toJSONString(a));
        log.info((String) request.getAttribute("username"));
        return this.adminUser.findAll();
    }

}
