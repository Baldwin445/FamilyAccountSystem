package com.baldwin.entity;

public class Home {
    private int id;
    private int onwerId;
    private int member;
    private int acctnum;
    private String address;
    private String nickname;    //the nickname of host
    private String realname;    //the realname of host
    private int count;          //the amount of info

    @Override
    public String toString() {
        return "Home{" +
                "id=" + id +
                ", onwerId=" + onwerId +
                ", member=" + member +
                ", acctnum=" + acctnum +
                ", address='" + address + '\'' +
                ", nickname='" + nickname + '\'' +
                ", realname='" + realname + '\'' +
                ", count=" + count +
                '}';
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOnwerId() {
        return onwerId;
    }

    public void setOnwerId(int onwerId) {
        this.onwerId = onwerId;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getAcctnum() {
        return acctnum;
    }

    public void setAcctnum(int acctnum) {
        this.acctnum = acctnum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
