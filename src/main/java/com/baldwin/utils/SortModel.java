package com.baldwin.utils;

import com.baldwin.entity.Tag;
import com.baldwin.entity.User;

import java.util.List;

/**
 * @ClassName: SortModel
 * @Description: sort model of welcome page 用于首页排名情况的模型 同时用于用户数据表统计
 * @author: Baldwin445
 * @date: 21/4/19 22:17
 */
public class SortModel {
    //排名页参数
    private String nickname;
    private int id;
    private String tagName;
    private float money;
    private User user;
    private Tag tag;
    //chart页参数
    private List<Float> profit, income, pay;
    private List<String> username;

    @Override
    public String toString() {
        return "SortModel{" +
                "nickname='" + nickname + '\'' +
                ", id=" + id +
                ", tagName='" + tagName + '\'' +
                ", money=" + money +
                ", user=" + user +
                ", tag=" + tag +
                ", profit=" + profit +
                ", income=" + income +
                ", pay=" + pay +
                ", username=" + username +
                '}';
    }

    public List<String> getUsername() {
        return username;
    }

    public void setUsername(List<String> username) {
        this.username = username;
    }

    public List<Float> getProfit() {
        return profit;
    }

    public void setProfit(List<Float> profit) {
        this.profit = profit;
    }

    public List<Float> getIncome() {
        return income;
    }

    public void setIncome(List<Float> income) {
        this.income = income;
    }

    public List<Float> getPay() {
        return pay;
    }

    public void setPay(List<Float> pay) {
        this.pay = pay;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
