package com.baldwin.utils;

import com.baldwin.entity.Tag;
import com.baldwin.entity.User;

/**
 * @ClassName: SortModel
 * @Description: sort model of welcome page 用于首页排名情况的模型
 * @author: Baldwin445
 * @date: 21/4/19 22:17
 */
public class SortModel {
    private String nickname;
    private int id;
    private String tagName;
    private float money;
    private User user;
    private Tag tag;

    @Override
    public String toString() {
        return "SortModel{" +
                "nickname='" + nickname + '\'' +
                ", id=" + id +
                ", tagName='" + tagName + '\'' +
                ", money=" + money +
                ", user=" + user +
                ", tag=" + tag +
                '}';
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
