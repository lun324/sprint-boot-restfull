package net.fulugou.demo.mapper;

import java.util.List;
import net.fulugou.demo.model.AdminUser;

public interface AdminUserMapper {

    int insert(AdminUser record);

    List<AdminUser> selectAll();

    AdminUser selectByUsername(String username);
}