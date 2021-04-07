package com.baldwin.service;

import com.baldwin.entity.Home;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;

import java.util.List;

public interface UserService {
    User loginConfirm(String acct, String pwd);

    RoleInfo getCurrentUserInfo(int userid);

    int regUser(User user);

    int existUserCheck(String acct);

    int setUserPermission(User user, int access);


}
