package com.baldwin.service.impl;

import com.baldwin.dao.BillMapper;
import com.baldwin.dao.UserMapper;
import com.baldwin.entity.Bill;
import com.baldwin.entity.User;
import com.baldwin.service.BillService;
import com.baldwin.utils.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: BillServiceImpl
 * @Description: implement of Bill Service
 * @author: Baldwin445
 * @date: 21/3/31 13:50
 */
@Service
public class BillServiceImpl implements BillService {
    @Resource
    private BillMapper billMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public int addPayBill(Bill bill) {
        return billMapper.addPayBill(bill);
    }

    @Override
    public int addIncomeBill(Bill bill) {
        return billMapper.addIncomeBill(bill);
    }

    @Override
    public int delBill(int billID) {
        return billMapper.delBill(billID);
    }

    @Override
    public Bill getBill(int billID) {
        return billMapper.getBillByID(billID);
    }

    @Override
    public int updateBill(Bill bill) {
        return billMapper.updateBill(bill);
    }

    @Override
    public List<Bill> getBill(int typeid, int userid, int begin, int num) {
        User user = userMapper.getCompleteUser(userid);
        int access = user.getAccess().getAccess();
        List<Bill> bill;

        //user: 仅看自己的账单
        //host/admin: 看全家人的
        if(access == UserUtil.USER_ACCESS_MEMBER)
            bill = billMapper.getBill(typeid, userid, begin, num);
        else if(user.getHouseId() == 0)
            bill = billMapper.getBill(typeid, userid, begin, num);
        else
            bill = billMapper.getHomeBill(typeid, user.getHouseId(), begin, num);

        return bill;
    }
}
