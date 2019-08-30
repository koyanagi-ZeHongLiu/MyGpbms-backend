package com.example.gpbms.purchase.request;

import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.purchase.entity.PurchaseRecordEntrust;
import lombok.Data;

@Data
public class EntrustRecordReq {
    private Purchase purchase;
    private PurchaseRecordEntrust purchaseRecordEntrust;
}
