package com.baldwin.entity;

import java.util.List;

public class Menu {
    private int access;
    private String menuid;
    private String menuname;
    private String location;
    private String menutype;
    private String menulevel;
    private String url;
    private List<Menu> secondMenus;

    @Override
    public String toString() {
        return "Menu{" +
                "access=" + access +
                ", menuid='" + menuid + '\'' +
                ", menuname='" + menuname + '\'' +
                ", location='" + location + '\'' +
                ", menutype='" + menutype + '\'' +
                ", menulevel='" + menulevel + '\'' +
                ", url='" + url + '\'' +
                ", secondMenus=" + secondMenus +
                '}';
    }

    public List<Menu> getSecondMenus() {
        return secondMenus;
    }

    public void setSecondMenus(List<Menu> secondMenus) {
        this.secondMenus = secondMenus;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMenutype() {
        return menutype;
    }

    public void setMenutype(String menutype) {
        this.menutype = menutype;
    }

    public String getMenulevel() {
        return menulevel;
    }

    public void setMenulevel(String menulevel) {
        this.menulevel = menulevel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
