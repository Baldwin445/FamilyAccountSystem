package com.baldwin.entity;

public class Tag {
    private int tagid;
    private int userid;
    private String tagName;
    private int typeid;

    @Override
    public String toString() {
        return "Tag{" +
                "tagid=" + tagid +
                ", userid=" + userid +
                ", tagName='" + tagName + '\'' +
                ", typeid=" + typeid +
                '}';
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getTagid() {
        return tagid;
    }

    public void setTagid(int tagid) {
        this.tagid = tagid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
