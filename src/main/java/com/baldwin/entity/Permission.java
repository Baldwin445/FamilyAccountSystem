package com.baldwin.entity;

public class Permission{
    private int userId;
    private int access;

    @Override
    public String toString() {
        return "Permission{" +
                "userId=" + userId +
                ", access=" + access +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }
}
