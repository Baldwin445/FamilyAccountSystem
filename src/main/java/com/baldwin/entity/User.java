package com.baldwin.entity;

public class User {
    private int id;
    private int roleId;
    private int houseId;
    private String acct;
    private String pwd;
    private String profile;
    private RoleInfo roleInfo;
    private Permission access;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", houseId=" + houseId +
                ", acct='" + acct + '\'' +
                ", pwd='" + pwd + '\'' +
                ", profile='" + profile + '\'' +
                ", roleInfo=" + roleInfo +
                ", access=" + access +
                '}';
    }

    public Permission getAccess() {
        return access;
    }

    public void setAccess(Permission access) {
        this.access = access;
    }

    public RoleInfo getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(RoleInfo roleInfo) {
        this.roleInfo = roleInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
