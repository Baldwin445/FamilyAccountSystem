package com.baldwin.utils;

import com.baldwin.entity.User;

import javax.servlet.http.HttpSession;

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
}
