package com.baldwin.dao;

import com.baldwin.entity.Menu;
import com.baldwin.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: MenuMapper
 * @Description: menu mapper interface
 * @author: Baldwin445
 * @date: 21/3/25 15:07
 */
@Repository
public interface MenuMapper {
    List<Menu> getMenusByUserID(int userid);

    List<Menu> getMenusByIDMenuLv(int userid, int menulv);

    List<Menu> getMenusByID_Lv_Type(int userid, int menulv, String type);
}
