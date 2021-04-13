package com.baldwin.service;

import com.baldwin.entity.Bill;

import java.util.List;

public interface BillService {
    int addPayBill(Bill bill);

    int addIncomeBill(Bill bill);

    List<Bill> getBill(int typeid, int userid, int begin, int num);

    int delBill(int billID);

    Bill getBill(int billID);

    int updateBill(Bill bill);
}
