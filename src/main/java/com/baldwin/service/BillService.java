package com.baldwin.service;

import com.baldwin.entity.Bill;

public interface BillService {
    int addPayBill(Bill bill);

    int addIncomeBill(Bill bill);
}
