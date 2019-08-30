package com.example.gpbms.purchase.controller;

import com.example.gpbms.provider.repository.ProviderRepository;
import com.example.gpbms.purchase.entity.PurchaseRecordDirect;
import com.example.gpbms.purchase.entity.PurchaseRecordEntrust;
import com.example.gpbms.purchase.entity.PurchaseRecordSelfOrganized;
import com.example.gpbms.purchase.repository.PurchaseRecordDirectRepository;
import com.example.gpbms.purchase.repository.PurchaseRecordEntrustRepository;
import com.example.gpbms.purchase.repository.PurchaseRecordSelfOrganizedRepository;
import com.example.gpbms.purchase.repository.PurchaseRepository;
import com.example.gpbms.purchase.request.DirectRecordReq;
import com.example.gpbms.purchase.request.EntrustRecordReq;
import com.example.gpbms.purchase.request.SelfOrganizedRecordReq;
import com.example.gpbms.util.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
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
    private PurchaseRecordDirectRepository purchaseRecordDirectRepository;

    @Autowired
    private PurchaseRecordEntrustRepository purchaseRecordEntrustRepository;

    @Autowired
    private PurchaseRecordSelfOrganizedRepository purchaseRecordSelfOrganizedRepository;

    @Transactional
    @PostMapping(value = "saveDirect")
    public RespBean saveDirect(@RequestBody DirectRecordReq directRecordReq){
        PurchaseRecordDirect purchaseRecordDirect = directRecordReq.getPurchaseRecordDirect();
        purchaseRecordDirect
                .setPurchase(directRecordReq.getPurchase())
                .setProvider(providerRepository.findByProviderName(purchaseRecordDirect.getProvider().getProviderName()).orElse(null));
        return RespBean.success("提交备案成功", purchaseRecordDirectRepository.save(purchaseRecordDirect));
    }

    @Transactional
    @PostMapping(value = "saveSelfOrganized")
    public RespBean saveSelfOrganized(@RequestBody SelfOrganizedRecordReq selfOrganizedRecordReq){
        PurchaseRecordSelfOrganized purchaseRecordSelfOrganized = selfOrganizedRecordReq.getPurchaseRecordSelfOrganized();
        purchaseRecordSelfOrganized
                .setPurchase(selfOrganizedRecordReq.getPurchase())
                .setProvider(providerRepository.findByProviderName(purchaseRecordSelfOrganized.getProvider().getProviderName()).orElse(null));
        return RespBean.success("提交备案成功", purchaseRecordSelfOrganizedRepository.save(purchaseRecordSelfOrganized));
    }

    @Transactional
    @PostMapping(value = "saveEntrust")
    public RespBean saveEntrust(@RequestBody EntrustRecordReq entrustRecordReq){
        PurchaseRecordEntrust purchaseRecordEntrust = entrustRecordReq.getPurchaseRecordEntrust();
        purchaseRecordEntrust
                .setPurchase(entrustRecordReq.getPurchase())
                .setProvider(providerRepository.findByProviderName(purchaseRecordEntrust.getProvider().getProviderName()).orElse(null));
        return RespBean.success("提交备案成功", purchaseRecordEntrustRepository.save(purchaseRecordEntrust));
    }
}
