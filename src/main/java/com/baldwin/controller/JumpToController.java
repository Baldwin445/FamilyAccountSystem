package com.baldwin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: JumpToController
 * @Description: Jump to Page Util
 * @author: Baldwin445
 * @date: 21/3/26 11:03
 */
@Controller
public class JumpToController {
    /*
    * 接受/pages请求并将{page}部分处理返回
    * 实现无视/pages路径
    * */
    @RequestMapping("/pages/{page}")
    public String toPage(@PathVariable String page) {
        return page.replace("_", "/");
    }

    @RequestMapping("/pages/null")
    public String toPage() {
        return "welcome";
    }
}
