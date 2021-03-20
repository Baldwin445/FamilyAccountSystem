package com.baldwin.entity;

public class Payway {
    private int id;
    private String payway;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    @Override
    public String toString() {
        return "Payway{" +
                "id=" + id +
                ", payway='" + payway + '\'' +
                '}';
    }
}
