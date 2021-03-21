package com.baldwin.service;

import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;

public interface UserService {
    User loginConfirm(String acct, String pwd);

    RoleInfo getCurrentUserInfo(int userid);

}
