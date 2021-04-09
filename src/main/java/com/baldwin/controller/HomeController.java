package com.baldwin.controller;

import com.baldwin.entity.Home;
import com.baldwin.service.HomeService;
import com.baldwin.service.UserService;
import com.baldwin.utils.LogUtil;
import com.baldwin.utils.Result;
import com.baldwin.utils.ResultUtil;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName: HomeController
 * @Description: the Home  Entity service
 * @author: Baldwin445
 * @date: 21/4/6 22:15
 */
@Controller
public class HomeController {
    @Resource
    private UserService userService;
    @Resource
    private HomeService homeService;

    @RequestMapping("/getAllHomeJson")
    @ResponseBody
    public String getAllHomeJson(int page, int limit, Model m){
        int begin = limit * (page - 1);
        int num = page * limit;
        //get the range of the List
        List<Home> homeList = homeService.getAllHomePage(begin, num);
        int count = homeService.countAllHome();
        //put data into Json
        JSONArray json = JSONArray.fromObject(homeList);
        String js = json.toString();

        String jso = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":"+js+"}";
        LogUtil.log(count);
        return jso;
    }

    /**
     * get the POST then return the Data of Home Info
     * 接受请求返回家庭信息，用于管理员
     */
    @RequestMapping(value = "/getAllHome",method = RequestMethod.POST)
    public String getAllHome(Model m){
        m.addAttribute("homelist", homeService.getAllHome());
        return "/sys/home::hometable";
    }

    /**
     * get the Search POST then return the Info of Home
     * POST includes the nickname/realname of host and HomeID
     * 根据提交的家庭ID和户主名称搜索信息
     */
    @RequestMapping(value = "/getHomeSearch")
    @ResponseBody
    public String searchHome(Model m, int page, int limit,
                             @RequestParam(required = false, name = "homeid") String homeid,
                             @RequestParam(required = false, name = "hostname") String hostname){
        int begin = limit * (page - 1);
        int num = page * limit;

        //when INPUT NOTHING, set them null can bring to /getAllHome
        if(homeid.equals("")) homeid = null;
        if(hostname.equals("")) hostname = null;
        int count;

        List<Home> homeList = homeService.getHomeByID_Hostname(homeid, hostname, begin, num);
        JSONArray json = JSONArray.fromObject(homeList);
        String js = json.toString();

        //when it needs to get All home
        //it needs a Right count of info
        if(homeid == null && hostname == null)
            count = homeService.countAllHome();
        else
            count = homeList.size();

        LogUtil.log("count", count);
        LogUtil.log(homeList.size());

        String jso = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":"+js+"}";
        return jso;
    }

    /**
     * get the Modify Home Address Data Post
     * @param homeid homeid
     * @param address home address
     * @return
     */
    @RequestMapping(value = "/modifyHome/{homeid}/{address}")
    @ResponseBody
    public Result modifyAddress(@PathVariable String homeid, @PathVariable String address){
        int result = homeService.modifyAddress(homeid, address);
        if(result == -1) return ResultUtil.unSuccess();
        else return ResultUtil.success();
    }

    @RequestMapping(value = "/delHome/{homeid}")
    @ResponseBody
    public Result deleteHome(@PathVariable String homeid){
        int result = homeService.deleteHome(homeid);
        if(result == -1) return ResultUtil.unSuccess();
        else return ResultUtil.success();
    }


    /**
     *
     * @param home
     * @return
     * @Bug 注意不能添加已有家庭编号的用户
     */
    @RequestMapping(value = "/addHome")
    @ResponseBody
    public Result addHome(Home home){
        if(home.getOwnerAcct().equals("")){
            homeService.addHomeAddress(home);
            return ResultUtil.success();
        }
        int id = userService.existUserCheck(home.getOwnerAcct());
        if(id > 0){
            home.setOwnerId(id);
            homeService.addHomeAddressAcct(home);
            userService.setHomeIDbyAcct(home.getOwnerAcct(), home.getId());
            return ResultUtil.success("添加户主信息成功");
        }
        else return ResultUtil.unSuccess("不存在此用户");

    }

}
