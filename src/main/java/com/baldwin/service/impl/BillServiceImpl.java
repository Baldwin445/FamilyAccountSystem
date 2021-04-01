package com.baldwin.service.impl;

import com.baldwin.dao.BillMapper;
import com.baldwin.entity.Bill;
import com.baldwin.service.BillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Override
    public int addPayBill(Bill bill) {
        return billMapper.addPayBill(bill);
    }

    @Override
    public int addIncomeBill(Bill bill) {
        return billMapper.addIncomeBill(bill);
    }
}
