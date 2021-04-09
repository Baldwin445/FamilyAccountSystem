package com.baldwin.dao;

import com.baldwin.entity.Home;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    User loginConfirm(String acct, String pwd);

    RoleInfo getCurrentUserInfo(int userid);

    int regUser(User user);

    int existUserCheck(String acct);

    int setUserPermission(int userid, int access);

    int setHomeIDbyAcct(String acct, int homeid);
}
