package com.baldwin.service.impl;

import com.baldwin.dao.UserMapper;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import com.baldwin.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName: UserServiceImpl
 * @Description: UserService function implement
 * @author: Baldwin445
 * @date: 21/3/21 12:23
 */
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper mapper;

    @Override
    public User loginConfirm(String acct, String pwd) {
        return mapper.loginConfirm(acct, pwd);
    }

    @Override
    public RoleInfo getCurrentUserInfo(int userid) {
        return mapper.getCurrentUserInfo(userid);
    }
}
