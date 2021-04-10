package com.baldwin.utils;

import com.baldwin.entity.Permission;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ClassName: UserUtil
 * @Description: for storing the info of the current user
 * @author: Baldwin445
 * @date: 21/3/21 13:02
 */
public class UserUtil {

    //current USER info
    public static String CURRENT_USER = "currentUser";
    public static String CURRENT_USERID = "currentUserID";

    //User Permission Level
    public static int USER_ACCESS_ADMIN = 7;
    public static int USER_ACCESS_HOST = 3;
    public static int USER_ACCESS_MEMBER = 1;


    //get User in session
    public static User getSessionUser(HttpSession session){
        return (User)session.getAttribute(UserUtil.CURRENT_USER);
    }

    //return the List<User> JSON(Two level into One level)
    //返回用户列表的json数据，将数据扁平化
    public static String getUserJSON(List<User> list){
        JSONArray jsonArray = new JSONArray();

        for(User l: list){
            JSONObject json, jsonAdd;
            json = JSONObject.fromObject(l);
            if(l.getRoleInfo() != null){
                jsonAdd = JSONObject.fromObject(l.getRoleInfo());
                json.putAll(jsonAdd);
            }
            Permission p = l.getAccess();
            if (p != null){
                if(p.getAccess() == 1) json.put("access", "普通用户");
                if(p.getAccess() == 3) json.put("access", "家庭户主");
                if(p.getAccess() == 7) json.put("access", "系统管理员");
            }
            json.remove("roleInfo");
            jsonArray.add(json);
        }
        return jsonArray.toString();
    }
}
