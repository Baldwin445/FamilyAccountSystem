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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
        LogUtil.log("AddBill Result", result);

        return ResultUtil.success(bill);
    }

    @RequestMapping(value = "/getBill/{type}/{userid}",method = RequestMethod.POST)
    @ResponseBody
    public String getBill(@PathVariable String type, @PathVariable String userid,
                          int limit, int page){
        int typeid = type.equals("pay")? 1:(type.equals("income")?2:0);
        int id = Integer.valueOf(userid);
        int begin = limit * (page - 1);
        int num = page * limit;

        List<Bill> bill = billService.getBill(typeid, id, begin, num);
        String js = BillUtil.billModelToJSON(bill);
        LogUtil.log(bill);
        LogUtil.log(js);

        String jso = "{\"code\":0,\"msg\":\"\",\"count\":"+bill.size()+",\"data\":"+js+"}";
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
        Bill bill = billService.getBill(Integer.valueOf(billID));
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
        int begin = limit * (page - 1);
        int num = page * limit;
        int typeid = type.equals("pay")? 1:(type.equals("income")?2:0);
        int userid = (int) request.getSession().getAttribute(UserUtil.CURRENT_USERID);
        List<Bill> bills =
                billService.searchBill(begin, num, userid, start, end, name, tagid, typeid);
        LogUtil.log(bills);

        String js = BillUtil.billModelToJSON(bills);
        LogUtil.log(bills);
        LogUtil.log(js);

        String jso = "{\"code\":0,\"msg\":\"\",\"count\":"+bills.size()+",\"data\":"+js+"}";
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


}
