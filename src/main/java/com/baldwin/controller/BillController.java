package com.baldwin.controller;

import com.baldwin.entity.Bill;
import com.baldwin.service.BillService;
import com.baldwin.service.TagService;
import com.baldwin.utils.LogUtil;
import com.baldwin.utils.Result;
import com.baldwin.utils.ResultUtil;
import com.baldwin.utils.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
}
