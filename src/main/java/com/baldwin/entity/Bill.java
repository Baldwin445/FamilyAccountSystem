package com.baldwin.entity;

public class Bill {
    private int id;
    private int tagid;
    private String tagName;
    private int typeid;        //1 支出 2 收入
    private String type;
    private String collectid;  //导入账单id
    private int paywayid;      //判断导入账单的类型
    private int userid;
    private float money;
    private String comment;
    private String time;
    private User user;
    private RoleInfo roleInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", tagid=" + tagid +
                ", tagName='" + tagName + '\'' +
                ", typeid=" + typeid +
                ", type='" + type + '\'' +
                ", collectid='" + collectid + '\'' +
                ", paywayid=" + paywayid +
                ", userid=" + userid +
                ", money=" + money +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                ", user=" + user +
                ", roleInfo=" + roleInfo +
                '}';
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    public int getTypeid() {
        return typeid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getType() {
        return type;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCollectid() {
        return collectid;
    }

    public void setCollectid(String collectid) {
        this.collectid = collectid;
    }

    public int getPaywayid() {
        return paywayid;
    }

    public void setPaywayid(int paywayid) {
        this.paywayid = paywayid;
    }

    public int getTagid() {
        return tagid;
    }

    public void setTagid(int tagid) {
        this.tagid = tagid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
