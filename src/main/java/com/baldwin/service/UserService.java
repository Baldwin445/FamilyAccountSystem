package com.baldwin.service;

import com.baldwin.entity.Home;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import com.baldwin.utils.Result;

import java.util.List;

public interface UserService {
    User loginConfirm(String acct, String pwd);

    RoleInfo getCurrentUserInfo(int userid);

    int regUser(User user);

    int existUserCheck(String acct);

    int setUserPermission(User user, int access);

    int setHomeIDbyAcct(String acct, int homeid);

    List<User> getAllUser(int begin, int num);

    int countAllUser();

    int clearUserByID(int userid);

    int updateRoleInfo(RoleInfo roleInfo);

    User getUserByID(int userid);

    Result addAdminOrUser(User user);
    Result addHost(User user);

    List<User> getHomeMember(int homeID, int begin, int num);


}
