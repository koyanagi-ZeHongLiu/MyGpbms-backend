package com.example.gpbms.purchase.request;

import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.purchase.entity.PurchaseAuditLog;
import com.example.gpbms.user.entity.User;
import lombok.Data;

@Data
public class AuditPurchaseReq {
    private PurchaseAuditLog purchaseAuditLog;
    private Purchase purchase;
    private User operator;
}
