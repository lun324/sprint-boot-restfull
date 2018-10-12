package net.fulugou.demo.service;

import com.github.pagehelper.PageHelper;

import net.fulugou.demo.config.PageNumAndPageSizeConfig;
import net.fulugou.demo.config.PageBean;
import net.fulugou.demo.mapper.AdminUserMapper;
import net.fulugou.demo.model.AdminUser;
import net.fulugou.demo.shiro.JWTAuthenticationToken;
import net.fulugou.demo.util.ApiResponse;
import net.fulugou.demo.util.BaseUtils;
import net.fulugou.demo.util.JWTUtil;

import org.apache.shiro.SecurityUtils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Transactional
    public ResponseEntity<?> findAll() {
        PageHelper.startPage(PageNumAndPageSizeConfig.getPageNum(), PageNumAndPageSizeConfig.getPageSize());
        List<AdminUser> list = adminUserMapper.selectAll();

        ApiResponse response = new ApiResponse("200", "获取数据成功", new PageBean<AdminUser>(list));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    public ResponseEntity<?> login(String username, String password, String host)
    {
        Map<String, Object> dataMap;

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            ApiResponse response = new ApiResponse("1", "用户名或密码不能为空！");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        try {
            AdminUser findAdminUser = adminUserMapper.selectByUsername(username);

            if (BaseUtils.isNullOrEmpty(findAdminUser)) {
                ApiResponse response = new ApiResponse("1", "用户不存在");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            if (! BCrypt.checkpw(password, findAdminUser.getPassword())) {
                ApiResponse response = new ApiResponse("1", "密码错误");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            String token = JWTUtil.createJwt(findAdminUser.getId(), findAdminUser.getUsername(), findAdminUser.getPassword());

            dataMap = new HashMap<>();
            dataMap.put("token", token);

        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse response = new ApiResponse("500", "服务器错误");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ApiResponse response = new ApiResponse("200", "登录成功", dataMap);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param email
     * @return
     */
    public ResponseEntity<?> register(String username, String password, String email)
    {
        Date date = new Date();

        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(username);
        adminUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        adminUser.setEmail(email);
        adminUser.setCreatedAt(date);

        adminUserMapper.insert(adminUser);

        ApiResponse response = new ApiResponse("200", "注册成功");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
