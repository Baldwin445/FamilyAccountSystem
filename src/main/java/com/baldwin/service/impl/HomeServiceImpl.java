package com.baldwin.service.impl;

import com.baldwin.dao.HomeMapper;
import com.baldwin.entity.Home;
import com.baldwin.service.HomeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: HomeServiceImpl
 * @Description:
 * @author: Baldwin445
 * @date: 21/4/7 10:57
 */
@Service
public class HomeServiceImpl implements HomeService {
    @Resource
    private HomeMapper mapper;

    @Override
    public List<Home> getAllHome() {
        return mapper.getAllHome();
    }

    @Override
    public List<Home> getHomeByID_Hostname(String homeid, String hostname, int begin, int num) {
        return mapper.getHomeByID_Hostname(homeid, hostname,begin,num);
    }

    @Override
    public int countAllHome() {
        return mapper.countAllHome();
    }

    @Override
    public List<Home> getAllHomePage(int begin, int num) {
        return mapper.getAllHomePage(begin, num);
    }
}
