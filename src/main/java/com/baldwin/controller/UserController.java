package com.baldwin.controller;

import com.baldwin.entity.*;
import com.baldwin.service.BillService;
import com.baldwin.service.MenuService;
import com.baldwin.service.UserService;
import com.baldwin.utils.*;
import com.baldwin.utils.UserUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    @Resource
    private BillService billService;

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
     * logout 登出操作
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        LogUtil.log(cookies);

        request.getSession().removeAttribute(UserUtil.CURRENT_USER);
        request.getSession().removeAttribute(UserUtil.CURRENT_USERID);
        return "login";
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
    @RequestMapping(value = {"/pages/welcome","/welcome"})
    public ModelAndView welcomePage(Model m, HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("welcome");

        // two ways of Thymeleaf getting object 两种设置Thymeleaf的参数方法
        // mav.addObject("name", "TEST!");
        // m.addAttribute("name", "TEST!");

        String[] strs = getMonthRange();
        int userid = (int) request.getSession().getAttribute(UserUtil.CURRENT_USERID);
        User user = userService.getUserByID(userid);
        if(user.getAccess().getAccess() == 1)
            mav.addObject("role", "普通用户");
        else if(user.getAccess().getAccess() == 3)
            mav.addObject("role", "家庭户主");
        else if(user.getAccess().getAccess() == 7)
            mav.addObject("role", "系统管理员");
        mav.addObject("acct", user.getRoleInfo().getNickname());
        mav.addObject("time", strs[0]);
        List<Bill> bills =
                billService.getBillToChart(userid, strs[1], strs[2]);
        List<Integer> idList = bills.stream().map(Bill::getUserid).distinct().collect(Collectors.toList());
        Map<Integer, String> userIDAcct = new HashMap<>();
        if(idList != null)
            for(int id: idList){
                RoleInfo r = userService.getUserByID(id).getRoleInfo();
                userIDAcct.put(id, r.getNickname());
            }

        Map<String, List<SortModel>> sortUser = sortAll(bills, idList, userIDAcct);
        mav.addObject("otherIncome", sortUser.get("income"));
        mav.addObject("otherPay", sortUser.get("pay"));
        mav.addObject("profit", sortUser.get("profit"));
        mav.addObject("tags", sortUser.get("tags"));

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

        //get the range of the List
        List<User> userList = userService.getAllUser(begin, limit);
//        LogUtil.log(userList);
        //put data into Json
        String js = UserUtil.userModelToJSON(userList);
//        LogUtil.log(js);

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

    /**
     * admin add the user(host/user/admin)
     * add home info -> roleinfo -> user -> access
     * @param user
     * @return
     */
    @RequestMapping("/adminAddUser")
    @ResponseBody
    public Result adminAddUser(User user){
        int access = user.getAccess().getAccess();
        Result result;
        if(access >> 2 == 1) return userService.addAdminOrUser(user);
        if(access >> 1 == 1) return userService.addHost(user);
        if(access == 1) return userService.addAdminOrUser(user);

        return ResultUtil.unSuccess("未知错误 未能成功执行");
    }


    /**
     *
     * @return return the current date and the first and last date of this month
     */
    private String[] getMonthRange() {
        //TODO: 此处获取的日期为下月的第一天 但不影响最后结果
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String current =
                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(c.getTime());
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//1:本月第一天
        String day1= format.format(c.getTime());
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        String day2= format.format(ca.getTime());
        String[] strs = {current, day1, day2};
        return strs;
    }

    /**
     *
     * @param bills
     * @param userID
     * @return 返回前两个排行榜的数据
     */
    private List<SortModel> sortUserBill(List<Bill> bills, List<Integer> userID,
                                         Map<Integer, String> userIDAcct){
        List<SortModel> model = new ArrayList<>();
        Map<Integer, List<Bill>> billUser = bills.stream().collect(Collectors.groupingBy(Bill::getUserid));
        for(Integer id :userID){
            List<Bill> temp = billUser.get(id);
            SortModel sortModel = new SortModel();
            if(temp != null){
                Double money = billUser.get(id).stream().mapToDouble(Bill::getMoney).sum();
                sortModel.setId(id);
                sortModel.setMoney(money.floatValue());
                sortModel.setNickname(userIDAcct.get(id));
            }
            else {
                sortModel.setId(id);
                sortModel.setMoney(0);
                sortModel.setNickname(userIDAcct.get(id));
            }
            model.add(sortModel);
        }
        model = model.stream().sorted(Comparator.comparing(SortModel::getMoney).reversed())
                .collect(Collectors.toList());
        return model;
    }

    /**
     *
     * @param bills
     * @param userID
     * @param userIDAcct
     * @return 返回前三个排行榜的数据
     */
    private Map<String, List<SortModel>> sortAll(List<Bill> bills,
                                       List<Integer> userID, Map<Integer, String> userIDAcct){
        // divided by typeID 按支付类型分类
        Map<Integer, List<Bill>> map =
                bills.stream().collect(Collectors.groupingBy(Bill::getTypeid));
        //sort user 用户收支排序
        List<SortModel> pay = new ArrayList<>(), income = new ArrayList<>(), profit = new ArrayList<>();
        Map<Integer, List<Bill>> payMap = map.get(1).stream().collect(Collectors.groupingBy(Bill::getUserid));
        Map<Integer, List<Bill>> incomeMap = map.get(2).stream().collect(Collectors.groupingBy(Bill::getUserid));
        for(Integer id :userID){
            Double payM, incomeM;
            if(payMap.get(id) != null) payM = payMap.get(id).stream().mapToDouble(Bill::getMoney).sum();
            else payM = 0.0;
            if(incomeMap.get(id) != null) incomeM = incomeMap.get(id).stream().mapToDouble(Bill::getMoney).sum();
            else incomeM = 0.0;
            //每个需要加入的对象需要独立new 否则会出现修改一个 另一个也被更新的情况（stream特性）
            SortModel payModel = new SortModel(), incomeModel = new SortModel(), profitModel = new SortModel();
            payModel.setNickname(userIDAcct.get(id));
            payModel.setMoney(payM.floatValue());
            pay.add(payModel);
            incomeModel.setNickname(userIDAcct.get(id));
            incomeModel.setMoney(incomeM.floatValue());
            income.add(incomeModel);
            profitModel.setNickname(userIDAcct.get(id));
            profitModel.setMoney(incomeM.floatValue()-payM.floatValue());
            profit.add(profitModel);
        }
        Map<String, List<SortModel>> sortMap = new HashMap<>();
        pay = pay.stream().sorted(Comparator.comparing(SortModel::getMoney).reversed())
                .collect(Collectors.toList());
        sortMap.put("pay", pay);
        income = income.stream().sorted(Comparator.comparing(SortModel::getMoney).reversed())
                .collect(Collectors.toList());
        sortMap.put("income", income);
        profit = profit.stream().sorted(Comparator.comparing(SortModel::getMoney).reversed())
                .collect(Collectors.toList());
        sortMap.put("profit", profit);

        //sort tags 标签收支
        List<String> tagName = bills.stream().map(Bill::getTagName).collect(Collectors.toList());
        Map<String, List<Bill>> tagBill = bills.stream().collect(Collectors.groupingBy(Bill::getTagName));
        List<SortModel> tagSort = new ArrayList<>();
        for(String t: tagName){
            SortModel sm = new SortModel();
            Double money = tagBill.get(t).stream().mapToDouble(Bill::getMoney).sum();
            sm.setMoney(money.floatValue());
            sm.setTagName(t);
            tagSort.add(sm);
        }
        tagSort = tagSort.stream().distinct().collect(Collectors.toList());
        tagSort = tagSort.stream().sorted(Comparator.comparing(SortModel::getMoney).reversed())
                .collect(Collectors.toList());
//        LogUtil.log(tagSort);
        if(profit.size() <= 3) tagSort = tagSort.subList(0, 3);
        else tagSort = tagSort.subList(0, profit.size());
        sortMap.put("tags", tagSort);

        return sortMap;
    }

}
