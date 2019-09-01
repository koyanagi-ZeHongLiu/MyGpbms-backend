package com.example.gpbms.purchase.controller;

import com.example.gpbms.provider.repository.ProviderRepository;
import com.example.gpbms.purchase.entity.*;
import com.example.gpbms.purchase.repository.*;
import com.example.gpbms.purchase.request.DirectRecordReq;
import com.example.gpbms.purchase.request.EntrustRecordReq;
import com.example.gpbms.purchase.request.GetPurchasesReq;
import com.example.gpbms.purchase.request.SelfOrganizedRecordReq;
import com.example.gpbms.util.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("api")
public class PurchaseRecordController {
    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    private PurchaseAuditStatusRepository purchaseAuditStatusRepository;

    @Autowired
    private PurchaseRecordDirectRepository purchaseRecordDirectRepository;

    @Autowired
    private PurchaseRecordEntrustRepository purchaseRecordEntrustRepository;

    @Autowired
    private PurchaseRecordSelfOrganizedRepository purchaseRecordSelfOrganizedRepository;

    @Transactional
    @PostMapping(value = "saveDirect")
    public RespBean saveDirect(@RequestBody DirectRecordReq directRecordReq){
        PurchaseRecordDirect purchaseRecordDirect = directRecordReq.getPurchaseRecordDirect();
        Purchase purchase = directRecordReq.getPurchase();
        purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(purchase.getPurchaseAuditStatus().getId()+1).orElse(null));
        purchaseRepository.save(purchase);
        purchaseRecordDirect
                .setPurchase(purchase)
                .setProvider(providerRepository.findByProviderName(purchaseRecordDirect.getProvider().getProviderName()).orElse(null));
        return RespBean.success("提交备案成功", purchaseRecordDirectRepository.save(purchaseRecordDirect));
    }

    @Transactional
    @PostMapping(value = "saveSelfOrganized")
    public RespBean saveSelfOrganized(@RequestBody SelfOrganizedRecordReq selfOrganizedRecordReq){
        PurchaseRecordSelfOrganized purchaseRecordSelfOrganized = selfOrganizedRecordReq.getPurchaseRecordSelfOrganized();
        Purchase purchase = selfOrganizedRecordReq.getPurchase();
        purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(purchase.getPurchaseAuditStatus().getId()+1).orElse(null));
        purchaseRepository.save(purchase);
        purchaseRecordSelfOrganized
                .setPurchase(purchase)
                .setProvider(providerRepository.findByProviderName(purchaseRecordSelfOrganized.getProvider().getProviderName()).orElse(null));
        return RespBean.success("提交备案成功", purchaseRecordSelfOrganizedRepository.save(purchaseRecordSelfOrganized));
    }

    @Transactional
    @PostMapping(value = "saveEntrust")
    public RespBean saveEntrust(@RequestBody EntrustRecordReq entrustRecordReq){
        PurchaseRecordEntrust purchaseRecordEntrust = entrustRecordReq.getPurchaseRecordEntrust();
        Purchase purchase = entrustRecordReq.getPurchase();
        purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(purchase.getPurchaseAuditStatus().getId()+1).orElse(null));
        purchaseRepository.save(purchase);
        purchaseRecordEntrust
                .setPurchase(purchase)
                .setProvider(providerRepository.findByProviderName(purchaseRecordEntrust.getProvider().getProviderName()).orElse(null));
        return RespBean.success("提交备案成功", purchaseRecordEntrustRepository.save(purchaseRecordEntrust));
    }

    @Transactional
    @PostMapping(value = "getRecords")
    public RespBean getDirects(@RequestBody GetPurchasesReq purchasesReq){
        Pageable pageable = PageRequest.of(purchasesReq.getPageUtils().getCurrentPage(), purchasesReq.getPageUtils().getPageSize());
        PurchaseAuditStatus status = purchaseAuditStatusRepository.findById(4).get();
        Page<Purchase> purchaseList = purchaseRepository.findByPurchaseTypeAndPurchaseAuditStatus(pageable, purchasesReq.getPurchaseType(), status);
        return RespBean.success("加载采购单成功", purchaseList);
    }
}
