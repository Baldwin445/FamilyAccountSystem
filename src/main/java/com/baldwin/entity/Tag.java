package com.baldwin.entity;

public class Tag {
    private int tagid;
    private String tagName;

    @Override
    public String toString() {
        return "Tag{" +
                "tagid=" + tagid +
                ", tagName='" + tagName + '\'' +
                '}';
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
