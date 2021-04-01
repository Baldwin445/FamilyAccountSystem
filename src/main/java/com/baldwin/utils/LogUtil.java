package com.baldwin.utils;

import com.baldwin.entity.Menu;
import com.baldwin.entity.Tag;
import com.baldwin.entity.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LogUtil {
    public static String log(String key, String value){
        String info = new Date() + "  [ " + key + " ] value is --- " +
                "[ " + value + " ]";
        System.out.println(info);
        return info;
    }

    public static <T> String log(T t){
        String info = t.toString();
        System.out.println(info);
        return info;
    }

    public static String log(User user){
        String info = user.toString();
        System.out.println(info);
        return info;
    }

    public static String log(String key, int value){
        String info = new Date() + "  [ " + key + " ] value is --- " +
                "[ " + value + " ]";
        System.out.println(info);
        return info;
    }

    public static <T> void log(List<T> list){
        for (T t:list)
            System.out.println(t);
    }

    public static void log(String msg){
        if (ResultUtil.ENABLE_CUSTOMEIZE_LOG){
            System.out.println(dateToStr(null) +" : "+msg);
        }
    }

    public static String dateToStr(Date date){
        if (date==null){
            date = new Date();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
}
