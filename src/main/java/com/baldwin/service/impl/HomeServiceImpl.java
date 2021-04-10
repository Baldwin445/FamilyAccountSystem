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

    /**
     *  return the rows affected
     */
    @Override
    public int modifyAddress(String homeid, String address) {
        int result = mapper.updateHomeAddress(homeid, address);
        if(result > 0) return result;
        else return -1;
    }
    /**
     *  return the rows affected
     */
    @Override
    public int deleteHome(String homeid) {
        int result = mapper.deleteHome(homeid);
        if(result > 0) return result;
        else
            return -1;
    }

    @Override
    public int addHomeAddress(Home home) {
        return mapper.addHomeAddress(home);
    }

    @Override
    public int addHomeAddressAcct(Home home) {
        return mapper.addHomeAddressAcct(home);
    }


}
