package com.example.gpbms.purchase.request;

import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.purchase.entity.PurchaseRecordSelfOrganized;
import lombok.Data;

@Data
public class SelfOrganizedRecordReq {
    private Purchase purchase;
    private PurchaseRecordSelfOrganized purchaseRecordSelfOrganized;
}
