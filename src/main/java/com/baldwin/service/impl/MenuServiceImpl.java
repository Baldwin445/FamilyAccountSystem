package com.baldwin.service.impl;

import com.baldwin.dao.MenuMapper;
import com.baldwin.entity.Menu;
import com.baldwin.entity.User;
import com.baldwin.service.MenuService;
import com.baldwin.utils.LogUtil;
import com.baldwin.utils.MenuUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: MenuServiceImpl
 * @Description: implement for Menu Service
 * @author: Baldwin445
 * @date: 21/3/25 15:12
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Resource
    private MenuMapper mapper;

    @Override
    public List<Menu> getMenusByID_Lv_Type(int userid, int lv, String type) {
        //according to UserID/Menus Level/Menus Type(classification)
        return mapper.getMenusByID_Lv_Type(userid, lv, type);
    }

    @Override
    public List<Menu> getMenusByUserID(int userid) {
        return mapper.getMenusByUserID(userid);
    }

    @Override
    public List<Menu> getMenusByIDMenuLv(int userid, int menulv) {
        //according to UserID/Menus Level
        return mapper.getMenusByIDMenuLv(userid, menulv);
    }

    @Override
    public List<Menu> getCorrectMenusByID(int userid) {
        //get a NEW Menus by dealing with Classifying Level 1 and 2
        //achieve the function which can make level one include level two
        List<Menu> menus = mapper.getMenusByIDMenuLv(userid, MenuUtil.MENU_LEVEL_ONE);
        for(Menu m: menus){
            m.setSecondMenus(mapper.getMenusByID_Lv_Type(userid, MenuUtil.MENU_LEVEL_TWO, m.getMenutype()));
        }
        return menus;
    }

    @Override
    public List<Menu> getToolbarByMenuLv(int menulv) {
        return mapper.getToolbarByMenuLv(menulv);
    }

    @Override
    public List<Menu> getToolbarByMenuLv_Type(int menulv, String type) {
        return mapper.getToolbarByMenuLv_Type(menulv, type);
    }

    @Override
    public List<Menu> getCorrectToolbar() {
        //return a initial top ToolBar including second-level menus
        List<Menu> menus = mapper.getToolbarByMenuLv(MenuUtil.MENU_LEVEL_ONE);
        for(Menu m: menus)
            m.setSecondMenus(mapper.getToolbarByMenuLv_Type(MenuUtil.MENU_LEVEL_TWO, m.getMenutype()));
        return menus;
    }
}
