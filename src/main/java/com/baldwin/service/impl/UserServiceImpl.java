package com.baldwin.service.impl;

import com.baldwin.dao.HomeMapper;
import com.baldwin.dao.UserMapper;
import com.baldwin.entity.Home;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import com.baldwin.service.UserService;
import com.baldwin.utils.LogUtil;
import com.baldwin.utils.Result;
import com.baldwin.utils.ResultUtil;
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
            homeMapper.updateHomeMember(homeid, -1);
            if(user.getAccess().getAccess() == 3)
                homeMapper.updateHomeHost(homeid, -1);
        }
        //delete the access and acct info
        //删除账户信息和权限信息
        mapper.deleteAccess(userid);
        mapper.deleteUser(userid);

        return 1;
    }

    @Override
    public int updateRoleInfo(RoleInfo roleInfo) {
        return mapper.updateRoleInfo(roleInfo);
    }

    @Override
    public User getUserByID(int userid) {
        return mapper.getCompleteUser(userid);
    }

    /**
     * for admin to add user/admin 对于管理员用于添加管理员和用户账号
     * @param user
     * @return the result and data
     * @TODO: 检测登录账号的acct的冲突
     */
    @Override
    public Result addAdminOrUser(User user) {
        //add home info -> roleinfo -> user -> access
        try {
            //home member +1家庭成员人数+1
            if(user.getHouseId() != 0) homeMapper.updateHomeMember(user.getHouseId(), 1);
            //add role info 增加用户个人信息
            RoleInfo role = user.getRoleInfo();
            mapper.addRoleInfo(role);
            user.setRoleId(role.getRoleId());
            //add user acct 添加用户账号及权限
            if(user.getHouseId() != 0) mapper.addUser(user);
            else mapper.addUserNoHomeID(user);
            mapper.setUserPermission(user.getId(), user.getAccess().getAccess());
        }catch (Exception e){
            return ResultUtil.error(e);
        }

        return ResultUtil.success(user);
    }

    /**
     * for admin to add host 对于管理员用于添加户主账号
     * @param user
     * @return the result and data
     * @TODO: 检测登录账号的acct的冲突
     */
    @Override
    public Result addHost(User user) {
        //get home -> add roleinfo -> user -> access -> home info
        Home home = new Home();
        if(user.getHouseId() == 0)
            homeMapper.addHomeAddress(home);
        else
            home = homeMapper.getHomeByHomeID(user.getHouseId());
        user.setHouseId(home.getId());

        mapper.addRoleInfo(user.getRoleInfo());
        user.setRoleId(user.getRoleInfo().getRoleId());

        mapper.addUser(user);
        mapper.setUserPermission(user.getId(), user.getAccess().getAccess());

        homeMapper.updateHomeMember(user.getHouseId(), 1);
        homeMapper.updateHomeHost(user.getHouseId(), user.getId());

        return ResultUtil.success(user);
    }

    @Override
    public List<User> getHomeMember(int homeID, int begin, int num) {
        return mapper.getHomeMember(homeID, begin, num);
    }


}
