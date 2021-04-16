package com.baldwin.service;

import com.baldwin.entity.Bill;
import com.baldwin.entity.WeChatData;

import java.util.List;

public interface BillService {
    int addPayBill(Bill bill);

    int addIncomeBill(Bill bill);

    List<Bill> getBill(int typeid, int userid, int begin, int num);

    int delBill(int billID);

    Bill getBill(int billID);

    int updateBill(Bill bill);

    List<Bill> searchBill(int begin, int num, int userid,
                          String startDate, String endDate,
                          String name, int tagid, int typeid);

    List<WeChatData> importWeChatData(int userid, List<WeChatData> wcData);
}
