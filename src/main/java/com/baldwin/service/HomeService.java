package com.baldwin.service;

import com.baldwin.entity.Home;

import java.util.List;

public interface HomeService {
    List<Home> getAllHome();

    List<Home> getHomeByID_Hostname(String homeid, String hostname, int begin, int num);

    int countAllHome();

    List<Home> getAllHomePage(int begin, int num);

    int modifyAddress(String homeid, String address);

    int deleteHome(String homeid);

    int addHomeAddress(Home home);

    int addHomeAddressAcct(Home home);

}
