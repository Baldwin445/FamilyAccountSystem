package com.baldwin.entity;

public class Home {
    private int id;
    private int onwerId;
    private int member;
    private int acctNum;
    private String address;

    @Override
    public String toString() {
        return "Home{" +
                "id=" + id +
                ", onwerId=" + onwerId +
                ", member=" + member +
                ", acctNum=" + acctNum +
                ", address='" + address + '\'' +
                '}';
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

    public int getAcctNum() {
        return acctNum;
    }

    public void setAcctNum(int acctNum) {
        this.acctNum = acctNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
