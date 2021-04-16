package com.baldwin.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: WeChatData
 * @Description: The WeChat Bill Import Data Model
 * @author: Baldwin445
 * @date: 21/4/15 0:38
 */
@ExcelTarget("wechatData")
public class WeChatData implements Serializable {
    @Excel(name = "交易时间", orderNum = "1", format = "yyyy-MM-dd HH:mm:ss")
    private String time;

    @Excel(name = "交易类型")
    private String tagName;

    @Excel(name = "商品")
    private String comment;

    @Excel(name = "收/支",replace = {"支出_1","收入_2","/_0"})
    private int typeid;

    @Excel(name = "金额(元)")
    private float money;

    // use to store tagID/userID then import into database
    // 用于赋值存储字段并作为实体直接导入数据库
    private int tagID;
    private int userID;
    private String collectID;

    @Override
    public String toString() {
        return "WeChatData{" +
                "time='" + time + '\'' +
                ", tagName='" + tagName + '\'' +
                ", comment='" + comment + '\'' +
                ", typeid=" + typeid +
                ", money=" + money +
                ", tagID=" + tagID +
                ", userID=" + userID +
                ", collectID='" + collectID + '\'' +
                '}';
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getCollectID() {
        return collectID;
    }

    public void setCollectID(String collectID) {
        this.collectID = collectID;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }
}
