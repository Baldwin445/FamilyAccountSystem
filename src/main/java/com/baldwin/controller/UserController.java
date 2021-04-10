package com.baldwin.controller;

import com.baldwin.entity.Home;
import com.baldwin.entity.Menu;
import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import com.baldwin.service.MenuService;
import com.baldwin.service.UserService;
import com.baldwin.utils.*;
import com.baldwin.utils.UserUtil;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName: UserController
 * @Description: deal with UserService class
 * @author: Baldwin445
 * @date: 21/3/21 10:57
 * TODO: 1. 登录后，切换首页所获取的数据消失
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private MenuService menuService;

    /*
    * the beginning page
    * */
    @RequestMapping(value = { "/", "login", "login.html"})
    public String toLogin(HttpServletRequest request,
                          HttpServletResponse response){
        HttpSession session = request.getSession();
        if(session.getAttribute(UserUtil.CURRENT_USER) == null){//当获取不到内容时返回“login”字符串
            return "login";//返回
        }else{//当获取到内容时返回空并且跳转
            try{
                response.sendRedirect("/pages/index");
            }catch (IOException e){
                e.printStackTrace();
                return "login";
            }
            return null;
        }
    }

    /*
    * confirm the acct info of the post form
    * */
    @RequestMapping("/login.check")
    @ResponseBody
    public Result login(HttpServletRequest request, String acct, String pwd){
        LogUtil.log("ACCT", acct);
        LogUtil.log("PWD", pwd);

        User user = userService.loginConfirm(acct, pwd);
        request.getSession().setAttribute(UserUtil.CURRENT_USER, user);
        request.getSession().setAttribute(UserUtil.CURRENT_USERID, user.getId());

        return ResultUtil.success(user);
    }

    /**
     * turn to Register Page
     */
    @RequestMapping("/register")
    public String registerPage(){
        return "register";
    }

    /**
     * register self
     * set the NEW USER nothing info
     */
    @RequestMapping("/regSelf")
    @ResponseBody
    public Result addUserSelf(User user){
        if (userService.existUserCheck(user.getAcct()) > 0)
            return ResultUtil.unSuccess("用户已存在！");
        userService.regUser(user);
        System.out.println(user);
        LogUtil.log(user);
        userService.setUserPermission(user, UserUtil.USER_ACCESS_MEMBER);

        return ResultUtil.success();
    }

    @RequestMapping(value = {"/pages/index"})
    public String indexPage(Model m, HttpServletRequest request){
        HttpSession session = request.getSession();
        User current = (User) session.getAttribute(UserUtil.CURRENT_USER);
        RoleInfo userInfo = userService.getCurrentUserInfo(current.getRoleId());
        m.addAttribute("name", userInfo.getNickname());
        m.addAttribute(MenuUtil.USER_MENUS, setSessionMenus(current, session));
        m.addAttribute(MenuUtil.USER_TOOLBAR, session.getAttribute(MenuUtil.USER_TOOLBAR));
        return "index";
    }

    /**
    * Use for test the jump to the page
    * HELLO.html            : using @ResponseBody
    * Just a "HELLO" word   : without using @ResponseBody
    * */
    @RequestMapping(value = "/welcome")
    public ModelAndView welcomePage(Model m, HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("welcome");
        //two ways of Thymeleaf getting object
        mav.addObject("name", "TEST!");
//        m.addAttribute("name", "TEST!");
        return mav;
    }

    /**
     * Using by UserID to find the MENUS and put it in Session
     * 通过用户信息获取用户菜单信息，并存入session中
     * @param user
     * @param session
     * @return
     */
    public List<Menu> setSessionMenus(User user, HttpSession session){
        List<Menu> menusNew = menuService.getCorrectMenusByID(user.getId());   //获取已分级的新菜单
        List<Menu> toolBar = menuService.getCorrectToolbar();
        session.setAttribute(MenuUtil.USER_MENUS, menusNew);
        session.setAttribute(MenuUtil.USER_TOOLBAR, toolBar);
//        LogUtil.log(menusNew);
//        LogUtil.log(toolBar);
        return menusNew;
    }

    /**
     *  get all user
     * @param page  the table page num 每次请求表格页面
     * @param limit the rows of every page 每页的行数
     * @return  the json data 表格所需的json数据格式
     */
    @RequestMapping("/getAllUser")
    @ResponseBody
    public String getAllUser(int page, int limit){
        int begin = limit * (page - 1);
        int num = page * limit;
        //get the range of the List
        List<User> userList = userService.getAllUser(begin, num);
        LogUtil.log(userList);
        //put data into Json
        String js = UserUtil.getUserJSON(userList);
        LogUtil.log(js);

        int count = userService.countAllUser();
        String jso = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":"+js+"}";
        return jso;
    }

    @RequestMapping("/delUser/{userid}")
    @ResponseBody
    public Result deleteUser(@PathVariable String userid){
        if(userService.clearUserByID(Integer.valueOf(userid)) > 0)
            return ResultUtil.success();
        else
            return ResultUtil.unSuccess();
    }

    @RequestMapping("/updateRoleInfo/{roleid}")
    @ResponseBody
    public Result updateUserInfoNoHomeID(@PathVariable String roleid, HttpServletRequest request, RoleInfo roleInfo){
        roleInfo.setRoleId(Integer.valueOf(roleid));
        int result = userService.updateRoleInfo(roleInfo);
        if(result == 1) return ResultUtil.success();
        else if(result == 0) return ResultUtil.unSuccess();
        else return ResultUtil.unSuccess("修改成功但数据异常");
    }

}
