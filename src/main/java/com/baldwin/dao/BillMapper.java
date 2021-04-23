package com.baldwin.dao;

import com.baldwin.entity.Bill;
import com.baldwin.entity.WeChatData;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillMapper {
    int addPayBill(Bill bill);

    int addIncomeBill(Bill bill);

    List<Bill> getSelfBill(int typeID, int userID, int begin, int num);

    List<Bill> getHomeBill(int typeID, int homeID, int begin, int num);

    int countSelfBill(int typeID, int userID);

    int countHomeBill(int typeID, int homeID);

    int delBill(int billId);

    Bill getBillByID(int billID);

    int updateBill(Bill bill);

    List<Bill> searchSelfBill(int begin, int num, int userid,
                          String startDate, String endDate,
                          String name, int tagid, int typeid);

    List<Bill> searchHomeBill(int begin, int num, int homeid,
                              String startDate, String endDate,
                              String name, int tagid, int typeid);

    int updateBillCollection(String collectID, int userID, int amount, String from);

    int insertWeChatData(WeChatData weChatData);

    List<Bill> getBillByCollectID(String collectID);

    int countSearchSelfBill(int userid,
                              String startDate, String endDate,
                              String name, int tagid, int typeid);

    int countSearchHomeBill(int homeid,
                              String startDate, String endDate,
                              String name, int tagid, int typeid);

    List<Bill> getReimburse(int userID, int begin, int num);

    int countReimburse(int userID);
}
