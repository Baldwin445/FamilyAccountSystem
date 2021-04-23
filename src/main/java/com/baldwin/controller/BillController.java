package com.baldwin.controller;

import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import com.baldwin.configs.WeChatImportHandle;
import com.baldwin.entity.Bill;
import com.baldwin.entity.User;
import com.baldwin.entity.WeChatData;
import com.baldwin.service.BillService;
import com.baldwin.service.TagService;
import com.baldwin.service.UserService;
import com.baldwin.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @ClassName: BillController
 * @Description: get Post and deal with bill service
 * @author: Baldwin445
 * @date: 21/3/31 14:51
 */
@Controller
public class BillController {
    @Resource
    private BillService billService;
    @Resource
    private TagService tagService;
    @Resource
    private UserService userService;


    @RequestMapping(value = "/addBill/{type}",method = RequestMethod.POST)
    @ResponseBody
    public Result addPayBill(@PathVariable String type, Bill bill, HttpSession session){
        //check the type
        int typeid = type.equals("pay")? 1:(type.equals("income")?2:0);
        if(typeid == 0)
            return ResultUtil.unSuccess("获取账单类型失败，请重新提交");
        int userid = (Integer)session.getAttribute(UserUtil.CURRENT_USERID);
        int tagid = tagService.tagNameToID(userid, bill.getTagName(), typeid);
        if(tagid < 0) return ResultUtil.unSuccess();

        bill.setUserid(userid);
        bill.setTypeid(typeid);
        bill.setTagid(tagid);

        int result;
        if(typeid == 1) result = billService.addPayBill(bill);
        else result = billService.addIncomeBill(bill);
//        LogUtil.log("AddBill Result", result);

        return ResultUtil.success(bill);
    }

    @RequestMapping(value = "/getBill/{type}/{userid}",method = RequestMethod.POST)
    @ResponseBody
    public String getBill(@PathVariable String type, @PathVariable String userid,
                          int limit, int page){
        int begin = (page - 1) * limit;

        int typeid = type.equals("pay")? 1:(type.equals("income")?2:0);
        int id = Integer.valueOf(userid);

        List<Bill> bill = billService.getBill(typeid, id, begin, limit);
        int count = billService.countBill(typeid, id);
        String js = BillUtil.billModelToJSON(bill);
//        LogUtil.log(bill);
//        LogUtil.log(js);

        String jso = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":"+js+"}";
        return jso;
    }

    @RequestMapping("/delBill/{billID}")
    @ResponseBody
    public Result delBill(@PathVariable String billID){
        int result = billService.delBill(Integer.valueOf(billID));
        if(result == 1) return ResultUtil.success();
        else return ResultUtil.unSuccess();
    }

    @RequestMapping("/getBill/{billID}")
    @ResponseBody
    public Result getBillByID(@PathVariable String billID){
        Bill bill = billService.getBillByID(Integer.valueOf(billID));
        if(bill == null) return ResultUtil.unSuccess();
        else {
            bill.setTime(bill.getTime().substring(0,10));
            return ResultUtil.success(bill);
        }
    }

    @RequestMapping("/updateBill/{billID}")
    @ResponseBody
    public Result updateBill(@PathVariable String billID, Bill bill){
        bill.setId(Integer.valueOf(billID));
        int result = billService.updateBill(bill);
        if(result == 1) return ResultUtil.success();
        else return ResultUtil.unSuccess();
    }

    @RequestMapping("/searchBill/{type}")
    @ResponseBody
    public String searchHome(int page, int limit, HttpServletRequest request,
                             @PathVariable String type,
                             @RequestParam(required = false, name = "tagID") int tagid,
                             @RequestParam(required = false, name = "name") String name,
                             @RequestParam(required = false, name = "startDate") String start,
                             @RequestParam(required = false, name = "endDate") String end){
        int begin = (page - 1) * limit;

        int typeid = type.equals("pay")? 1:(type.equals("income")?2:0);
        int userid = (int) request.getSession().getAttribute(UserUtil.CURRENT_USERID);
        List<Bill> bills =
                billService.searchBill(begin, limit, userid, start, end, name, tagid, typeid);
        int count = billService.countSearchBill(userid, start, end, name, tagid, typeid);

        String js = BillUtil.billModelToJSON(bills);
//        LogUtil.log(bills);
//        LogUtil.log(js);

        String jso = "{\"code\":0,\"msg\":\"\",\"count\":"+count+",\"data\":"+js+"}";
        return jso;
    }

    @RequestMapping("/dataImportUpload/{userid}")
    @ResponseBody
    public String importUpload(@PathVariable String userid,
                               @RequestParam(name="platform") String platform,
                               @RequestParam(value="file") MultipartFile file, Model m){
        //未选择平台信息时报错
        if(platform.equals(""))
            return "{\"code\":500,\"msg\":\"请选择平台！\",\"count\":"+0+",\"data\":"+0+"}";

        if(platform.equals("wechat")){
            // Wechat data processing module
            // 微信数据处理模块
            CsvImportParams params = new CsvImportParams();
            WeChatImportHandle handle = new WeChatImportHandle();
            handle.setNeedHandlerFields(new String[]{"金额(元)","商品"});
            params.setTitleRows(16);
            params.setHeadRows(1);
            params.setDataHandler(handle);
            try {
                List<WeChatData> datas = CsvImportUtil.importCsv(file.getInputStream(),
                        WeChatData.class, params);
                datas = billService.importWeChatData(Integer.valueOf(userid), datas);
                JSONArray json = JSONArray.fromObject(datas);

                return "{\"code\":500,\"msg\":\"数据读取失败！\",\"count\":"+datas.size()+",\"data\":"+json.toString()+"}";
            } catch (IOException e) {
                e.printStackTrace();
                return "{\"code\":500,\"msg\":\"数据读取失败！\",\"count\":"+0+",\"data\":"+0+"}";
            }
        }
        if(platform.equals("alipay")){
            // Alipay data processing module
            //支付宝数据处理模块
            System.out.println("it's alipay");

        }
        return "{\"code\":0,\"msg\":\"上传成功！\",\"count\":"+0+",\"data\":"+0+"}";
    }

    @RequestMapping("/getBillToCharts/{userid}")
    @ResponseBody
    public Result<Bill> getBillToCharts(@PathVariable String userid, String startTime, String endTime){
        List<Bill> bills = billService.getBillToChart(Integer.valueOf(userid), startTime, endTime);
        if(bills == null) return ResultUtil.unSuccess();
        else return ResultUtil.success(bills);
    }

    @RequestMapping(value = {"/tag", "/pages/chart_tag"})
    public ModelAndView getTagToCharts(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/chart/tag");
        HttpSession session = request.getSession();
        List<Bill> bills = billService.getBillToChart((int)session.getAttribute(UserUtil.CURRENT_USERID),
                "2021-04-01", "2021-04-30");
        //分类处理账单信息
        Map<Integer, List<Bill>> map = bills.stream().collect(Collectors.groupingBy(Bill::getTypeid));

        Map<String, JSONArray> jsonMap = billToSumSort(map.get(1));
        mav.addObject("payName", jsonMap.get("name").toString());
        mav.addObject("payNameValue", jsonMap.get("nameValue").toString());
        mav.addObject("payValue", jsonMap.get("value").toString());
        jsonMap = billToSumSort(map.get(2));
        mav.addObject("incomeName", jsonMap.get("name").toString());
        mav.addObject("incomeNameValue", jsonMap.get("nameValue").toString());
        mav.addObject("incomeValue", jsonMap.get("value").toString());

        return mav;
    }

    @RequestMapping(value = {"/platform", "/pages/chart_platform"})
    public ModelAndView getUserBillToCharts(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/chart/platform");
        HttpSession session = request.getSession();
        List<Bill> bills = billService.getBillToChart((int)session.getAttribute(UserUtil.CURRENT_USERID),
                "2021-04-01", "2021-04-30");
        SortModel sm = getUserDataToChart(bills);
        mav.addObject("payList", JSONArray.fromObject(sm.getPay()).toString());
        mav.addObject("incomeList",  JSONArray.fromObject(sm.getIncome()));
        mav.addObject("profitList", JSONArray.fromObject(sm.getProfit()));
        mav.addObject("userList", JSONArray.fromObject(sm.getUsername()));

        return mav;
    }

    /**
     * 将bill账单转为
     * @param bills
     * @return
     */
    private Map<String, JSONArray> billToSumSort(List<Bill> bills){
        JSONArray name = new JSONArray(), value = new JSONArray(), nameValue = new JSONArray();
        List<SortModel> datas = new ArrayList<>();
        List<String> tagName = bills.stream().map(Bill::getTagName).distinct().collect(Collectors.toList());
        Map<String, List<Bill>> billTag = bills.stream().collect(Collectors.groupingBy(Bill::getTagName));
        for (String t: tagName){
            JSONObject nameJson = new JSONObject(), valueJson = new JSONObject();
            Double money = billTag.get(t).stream().mapToDouble(Bill::getMoney).sum();
            
            nameJson.put("name", t);
            name.add(nameJson);

            valueJson.put("value", money.floatValue());
            value.add(valueJson);

            valueJson.put("name", t);
            nameValue.add(valueJson);
        }
        Map<String, JSONArray> map = new HashMap<>();
        map.put("name", name);
        map.put("nameValue", nameValue);
        map.put("value", value);
        return map;
    }

    private SortModel getUserDataToChart(List<Bill> bills){
        SortModel sm = new SortModel();
        List<Float> pay=new ArrayList<>(), income=new ArrayList<>(), profit=new ArrayList<>();
        List<String> user=new ArrayList<>();

        List<Integer> userid = bills.stream().map(Bill::getUserid).distinct().collect(Collectors.toList());
        Map<Integer, List<Bill>> map = bills.stream().collect(Collectors.groupingBy(Bill::getUserid));
        for (Integer id: userid){
            Double payM, incomeM;
            List<Bill> temp = map.get(id);
            Map<Integer, List<Bill>> mapBillType =temp.stream().collect(Collectors.groupingBy(Bill::getTypeid));
            if(null != mapBillType.get(1))
                payM = mapBillType.get(1).stream().mapToDouble(Bill::getMoney).sum();
            else payM = 0.0;
            if(null != mapBillType.get(2))
                incomeM = mapBillType.get(2).stream().mapToDouble(Bill::getMoney).sum();
            else incomeM = 0.0;
            pay.add(-payM.floatValue());
            income.add(incomeM.floatValue());
            profit.add(incomeM.floatValue()-payM.floatValue());
            user.add(userService.getUserByID(id).getRoleInfo().getNickname());
        }
        sm.setIncome(income);
        sm.setPay(pay);
        sm.setProfit(profit);
        sm.setUsername(user);

        return sm;
    }

}
