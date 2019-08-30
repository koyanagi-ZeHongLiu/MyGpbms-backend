package com.example.gpbms.purchase.request;

import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.purchase.entity.PurchaseRecordDirect;
import lombok.Data;

@Data
public class DirectRecordReq {
    private Purchase purchase;
    private PurchaseRecordDirect purchaseRecordDirect;
}
