package com.baldwin.service.impl;

import com.baldwin.dao.UserMapper;
import com.baldwin.entity.Home;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import com.baldwin.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public int regUser(User user) {
        return mapper.regUser(user);
    }

    /**
     * @return return the count of the same acct name
     */
    @Override
    public int existUserCheck(String acct) {
        return mapper.existUserCheck(acct);
    }

    @Override
    public int setUserPermission(User user, int access){
        return mapper.setUserPermission(user.getId(), access);
    }

    @Override
    public List<Home> getAllHome() {
        return mapper.getAllHome();
    }
}
