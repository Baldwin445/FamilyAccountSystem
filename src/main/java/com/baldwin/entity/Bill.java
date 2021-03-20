package com.baldwin.entity;

public class Bill {
    private int id;
    private int paywayId;
    private int tagId;
    private int type;
    private int userId;
    private float money;
    private String comment;
    private String time;

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", paywayId=" + paywayId +
                ", tagId=" + tagId +
                ", type=" + type +
                ", userId=" + userId +
                ", money=" + money +
                ", comment='" + comment + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPaywayId() {
        return paywayId;
    }

    public void setPaywayId(int paywayId) {
        this.paywayId = paywayId;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
