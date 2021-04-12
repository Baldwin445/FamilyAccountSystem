package com.baldwin.entity;

/**
 * @ClassName: Reimburse
 * @Description:
 * @author: Baldwin445
 * @date: 21/4/11 20:13
 */
public class Reimburse {
    private int id;
    private int userid;
    private int state;
    private float reduce;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public float getReduce() {
        return reduce;
    }

    public void setReduce(float reduce) {
        this.reduce = reduce;
    }

    @Override
    public String toString() {
        return "Reimburse{" +
                "id=" + id +
                ", userid=" + userid +
                ", state=" + state +
                ", reduce=" + reduce +
                '}';
    }
}
