package com.baldwin.service.impl;

import com.baldwin.dao.HomeMapper;
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
    @Resource
    private HomeMapper homeMapper;

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
     * @return return the id of the Acct User
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
    public int setHomeIDbyAcct(String acct, int homeid) {
        return mapper.setHomeIDbyAcct(acct, homeid);
    }

    @Override
    public List<User> getAllUser(int begin, int num) {
        return mapper.getAllUser(begin, num);
    }

    @Override
    public int countAllUser() {
        return mapper.countAllUser();
    }

    /**
     * delete all of the info according to userid
     * 根据提供的id删除用户所有信息
     * @param userid the provided user id
     * @return the result: -1 means user doesn't exist
     *
     */
    @Override
    public int clearUserByID(int userid) {
        User user = mapper.getUserByID(userid);
        if(user == null) return -1;
        int homeid = user.getHouseId();

        //update the home info if it exists
        if(homeid != 0){
            homeMapper.updateHomeMember(homeid);
            if(user.getAccess().getAccess() == 3)
                homeMapper.updateHomeHost(homeid, -1);
        }

        //delete the access and acct info
        //删除账户信息和权限信息
        mapper.deleteAccess(userid);
        mapper.deleteUser(userid);


        return 1;
    }


}
