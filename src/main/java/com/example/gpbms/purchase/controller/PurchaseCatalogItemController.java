package com.example.gpbms.purchase.controller;

import com.example.gpbms.purchase.entity.PurchaseCatalogItem;
import com.example.gpbms.purchase.repository.PurchaseCatalogItemRepository;
import com.example.gpbms.util.PageUtils;
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
public class PurchaseCatalogItemController {
    @Autowired
    private PurchaseCatalogItemRepository purchaseCatalogItemRepository;

    @Transactional
    @PostMapping(value = "savePurchaseCatalogItem")
    public void savePurchaseCatalogItem(@RequestBody PurchaseCatalogItem purchaseCatalogItem){
        purchaseCatalogItemRepository.save(purchaseCatalogItem);
    }

    @Transactional
    @PostMapping(value = "deletePurchaseCatalogItem")
    public void deletePurchaseCatalogItem(@RequestBody PurchaseCatalogItem purchaseCatalogItem){
        purchaseCatalogItemRepository.delete(purchaseCatalogItem);
    }

    @PostMapping(value = "getPurchaseCatalogItem")
    public void getPurchaseCatalogItem(@RequestBody PurchaseCatalogItem purchaseCatalogItem){
        purchaseCatalogItemRepository.findById(purchaseCatalogItem.getId()).orElse(null);
    }

    @PostMapping(value = "getPurchaseCatalogItems")
    public Page<PurchaseCatalogItem> getUsers(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<PurchaseCatalogItem> purchaseCatalogItemList = purchaseCatalogItemRepository.findAll(pageable);
        return purchaseCatalogItemList;
    }
}
