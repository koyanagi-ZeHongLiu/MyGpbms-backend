package com.example.gpbms.purchase.controller;


import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.purchase.entity.PurchaseAuditLog;
import com.example.gpbms.purchase.repository.PurchaseAuditLogRepository;
import com.example.gpbms.purchase.repository.PurchaseAuditStatusRepository;
import com.example.gpbms.purchase.repository.PurchaseRepository;
import com.example.gpbms.purchase.request.AuditPurchaseReq;
import com.example.gpbms.util.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("api")
public class PurchaseAuditLogController {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseAuditLogRepository purchaseAuditLogRepository;

    @Autowired
    private PurchaseAuditStatusRepository purchaseAuditStatusRepository;

    @Transactional
    @PostMapping(value = "approvePurchase")
    public RespBean approvePurchase(@RequestBody AuditPurchaseReq auditPurchaseReq){
        //审核通过
        auditPurchaseReq.getPurchaseAuditLog().setAuditor(auditPurchaseReq.getOperator());
        Purchase purchase = auditPurchaseReq.getPurchase();
        purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(purchase.getPurchaseAuditStatus().getId()+1).orElse(null));
        purchaseRepository.save(purchase);
        auditPurchaseReq.getPurchaseAuditLog().setPurchase(purchase);

        if(!purchase.getPurchaseAuditLogs().isEmpty() && purchase.getPurchaseAuditLogs() != null) {
            List<PurchaseAuditLog> purchaseAuditLogList = purchase.getPurchaseAuditLogs();
            purchaseAuditLogList.add(auditPurchaseReq.getPurchaseAuditLog());
            purchase.setPurchaseAuditLogs(purchaseAuditLogList);
        }
        return RespBean.success("审核成功", purchaseAuditLogRepository.save(auditPurchaseReq.getPurchaseAuditLog()));
    }

    @Transactional
    @PostMapping(value = "rejectPurchase")
    public RespBean rejectPurchase(@RequestBody AuditPurchaseReq auditPurchaseReq){
        //驳回
        Purchase purchase = auditPurchaseReq.getPurchase();
        if(purchase.getPurchaseAuditStatus().getId() == 0){
            return RespBean.failure("驳回失败");
        }
        auditPurchaseReq.getPurchaseAuditLog().setAuditor(auditPurchaseReq.getOperator());
        purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(purchase.getPurchaseAuditStatus().getId()-1).orElse(null));
        purchaseRepository.save(purchase);
        auditPurchaseReq.getPurchaseAuditLog().setPurchase(purchase);

        if(!purchase.getPurchaseAuditLogs().isEmpty() && purchase.getPurchaseAuditLogs() != null) {
            List<PurchaseAuditLog> purchaseAuditLogList = purchase.getPurchaseAuditLogs();
            purchaseAuditLogList.add(auditPurchaseReq.getPurchaseAuditLog());
            purchase.setPurchaseAuditLogs(purchaseAuditLogList);
        }
        return RespBean.success("驳回成功", purchaseAuditLogRepository.save(auditPurchaseReq.getPurchaseAuditLog()));
    }
}
