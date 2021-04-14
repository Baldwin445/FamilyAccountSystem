package com.baldwin.controller;

import com.baldwin.entity.User;
import com.baldwin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @ClassName: JumpToController
 * @Description: Jump to Page Util
 * @author: Baldwin445
 * @date: 21/3/26 11:03
 */
@Controller
public class JumpToController {

    @Resource
    private UserService userService;

    /**
    * 接受/pages请求并将{page}部分处理返回
    * 实现无视/pages路径
    * */
    @RequestMapping("/pages/{page}")
    public String toPage(@PathVariable String page) {
        return page.replace("_", "/");
    }

    /**
     * 跳转页面同时附加id参数
     * @param userid
     * @return
     */
    @RequestMapping("/pages/{page}/{userid}")
    public String toPageWithID(@PathVariable String page, @PathVariable String userid,
                                   Model m) {
        User user = userService.getUserByID(Integer.valueOf(userid));
        return "/sys/userEdit";
    }

    @RequestMapping("/pages/null")
    public String toPage() {
        return "welcome";
    }

    @RequestMapping("/test")
    public String toTestPage() {
        return "/details/pay";
    }

    /**
     * 专门跳转用于提交用户数据
     * @param userid
     * @return
     */
    @RequestMapping("/pages/sys/userEdit/{userid}")
    public String toUpdateRoleInfo(@PathVariable String userid, Model m) {

        User user = userService.getUserByID(Integer.valueOf(userid));
        m.addAttribute("roleid", user.getRoleId());
        return "/sys/userEdit";
    }
}
