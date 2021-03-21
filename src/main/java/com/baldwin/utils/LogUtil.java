package com.baldwin.utils;

import com.baldwin.entity.User;

import java.util.Date;

public class LogUtil {
    public static String logInfo(String key, String value){
        String info = new Date() + "  [ " + key + " ] value is --- " +
                "[ " + value + " ]";
        System.out.println(info);
        return info;
    }

    public static String logStudentInfo(User user){
        String info = user.toString();
        System.out.println(info);
        return info;
    }

    public static String logInfo(String key, int value){
        String info = new Date() + "  [ " + key + " ] value is --- " +
                "[ " + value + " ]";
        System.out.println(info);
        return info;
    }
}
