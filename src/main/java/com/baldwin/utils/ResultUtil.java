package com.baldwin.utils;

import java.util.List;

/**
 * 接口访问返回
 */
public class ResultUtil {
    //Result
    public static int SUCCESS=200; //成功
    public static int UNSUCCESS=400;   //失败
    public static int ERROR=500;   //异常
    public static boolean ENABLE_CUSTOMEIZE_LOG = true;


    public static Result success(){
        Result result = new Result();
        result.setCode(ResultUtil.SUCCESS);
        result.setMsg("操作成功！");
        return result;
    }

    public static Result success(List list){
        Result result = new Result();
        result.setCode(ResultUtil.SUCCESS);
        result.setMsg("操作成功！");
        result.setDatas(list);
        return result;
    }

    public static Result success(Object o){
        Result result = new Result();
        result.setCode(ResultUtil.SUCCESS);
        result.setMsg("操作成功！");
        result.setData(o);
        return result;
    }

    public static Result success(String msg, Object object){
        Result result = new Result();
        result.setCode(ResultUtil.SUCCESS);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    public static Result error(Exception e){
        Result result = new Result();
        result.setCode(ResultUtil.ERROR);
        result.setMsg("操作失败，发生异常");
        //如果启用自定义日志，则在控制台打印错误信息
        LogUtil.log(e.getMessage());
        return result;
    }

    public static Result unSuccess(){
        Result result = new Result();
        result.setCode(ResultUtil.UNSUCCESS);
        result.setMsg("操作失败！");
        return result;
    }

    public static Result unSuccess(String msg){
        Result result = new Result();
        result.setCode(ResultUtil.UNSUCCESS);
        result.setMsg(msg);
        return result;
    }
}
