package com.baldwin.dao;

import com.baldwin.entity.Bill;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillMapper {
    int addPayBill(Bill bill);

    int addIncomeBill(Bill bill);

    List<Bill> getBill(int typeid, int userid, int begin, int num);

    List<Bill> getHomeBill(int typeid, int houseid, int begin, int num);

    int delBill(int billId);

    Bill getBillByID(int billID);

    int updateBill(Bill bill);

}
