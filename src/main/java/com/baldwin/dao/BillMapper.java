package com.baldwin.dao;

import com.baldwin.entity.Bill;
import org.springframework.stereotype.Repository;

@Repository
public interface BillMapper {
    int addPayBill(Bill bill);

    int addIncomeBill(Bill bill);
}
