package com.baldwin.controller;

import com.baldwin.entity.RoleInfo;
import com.baldwin.entity.User;
import com.baldwin.service.UserService;
import com.baldwin.utils.LogUtil;
import com.baldwin.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @ClassName: UserController
 * @Description: deal with UserService class
 * @author: Baldwin445
 * @date: 21/3/21 10:57
 */
@Controller
public class UserController {
    @Resource
    private UserService userService;

    /*
    * the beginning page
    * */
    @RequestMapping(value = { "/", "login", "login.html"})
    public String toLogin(HttpServletRequest request,
                          HttpServletResponse response){
        HttpSession session = request.getSession();
        if(session.getAttribute(UserUtil.CURRENT_USER) == null){//当获取不到内容时返回“login”字符串
            return "login";//返回
        }else{                                       //当获取到内容时返回空并且跳转
            try{
                response.sendRedirect("/index");
            }catch (IOException e){
                e.printStackTrace();
                return "login";
            }
            return null;
        }
    }

    /*
    * confirm the acct info in form
    * */
    @RequestMapping("/login.check")
    @ResponseBody
    public User login(HttpServletRequest request, String acct, String pwd){
        LogUtil.logInfo("ACCT", acct);
        LogUtil.logInfo("PWD", pwd);

        User user = userService.loginConfirm(acct, pwd);
        request.getSession().setAttribute(UserUtil.CURRENT_USER, user);

        return user;
    }

    @RequestMapping(value = {"index", "index.html"})
    public String indexPage(Model m, HttpServletRequest request){
        HttpSession session = request.getSession();
        User current = (User) session.getAttribute(UserUtil.CURRENT_USER);
        RoleInfo userInfo = userService.getCurrentUserInfo(current.getRoleId());
        m.addAttribute("name", userInfo.getNickname());
        return "index";
    }

    /*
    * Use for test the jump to the page
    * HELLO.html            : using @ResponseBody
    * Just a "HELLO" word   : without using @ResponseBody
    * */
    @RequestMapping(value = "hello")
    public String welcomePage(Model m, HttpServletRequest request){
        m.addAttribute("name", "TEST!");
        return "hello";
    }
}
