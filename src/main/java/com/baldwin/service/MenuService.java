package com.baldwin.service;

import com.baldwin.entity.Menu;
import com.baldwin.entity.User;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: MenuService
 * @Description: Menus Service Interface
 * @author: Baldwin445
 * @date: 21/3/25 15:10
 */
public interface MenuService {
    List<Menu> getMenusByUserID(int userid);

    List<Menu> getMenusByIDMenuLv(int userid, int menulv);

    List<Menu> getCorrectMenusByID(int userid);

    List<Menu> getMenusByID_Lv_Type(int userid, int menulv, String type);

    List<Menu> getToolbarByMenuLv(int menulv);

    List<Menu> getToolbarByMenuLv_Type(int menulv, String type);

    List<Menu> getCorrectToolbar();

}
