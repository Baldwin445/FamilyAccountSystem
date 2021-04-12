package com.baldwin.dao;

import com.baldwin.entity.Home;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeMapper {
    List<Home> getAllHome();

    List<Home> getHomeByID_Hostname(String homeid, String hostname, int begin, int num);

    int countAllHome();

    List<Home> getAllHomePage(int begin, int num);

    int updateHomeAddress(String homeid, String address);

    int deleteHome(String homeid);

    int addHomeAddress(Home home);

    int addHomeAddressAcct(Home home);

    int updateHomeMember(int homeid, int num);

    int updateHomeHost(int homeid, int userid);

    Home getHomeByHomeID(int homeid);

}
