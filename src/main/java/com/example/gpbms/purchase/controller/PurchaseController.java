package com.example.gpbms.purchase.controller;

import com.example.gpbms.budget.repository.BudgetRepository;
import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.purchase.entity.PurchaseAuditLog;
import com.example.gpbms.purchase.entity.PurchaseItems;
import com.example.gpbms.purchase.repository.*;
import com.example.gpbms.purchase.request.GetPurchasesReq;
import com.example.gpbms.user.entity.Role;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.user.repository.UserRepository;
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
import java.util.List;

@RestController
@RequestMapping("api")
public class PurchaseController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseAuditLogRepository purchaseAuditLogRepository;

    @Autowired
    private PurchaseItemsRepository purchaseItemsRepository;

    @Autowired
    private PurchaseAuditStatusRepository purchaseAuditStatusRepository;

    @Autowired
    private PurchaseCatalogItemRepository purchaseCatalogItemRepository;

    @Transactional
    @PostMapping(value = "savePurchase")
    public RespBean savePurchase(@RequestBody Purchase purchase){
        if(purchase.getPurchaseAuditStatus() == null || purchase.getPurchaseAuditStatus().getId() == 0){
            purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(0).get());
        }else{
            purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(1).get());
        }
        //通过前端传过来的Budget.id设置Budget,无预算的话默认设置为0，预算单0标记为无预算
        if(purchase.getBudget().getId().isEmpty() || purchase.getBudget().getId() == null){
            purchase.setBudget(budgetRepository.findById("0").orElse(null));
        }else{
            purchase.setBudget(budgetRepository.findById(purchase.getBudget().getId()).orElse(null));
        }
        if(!purchase.getPurchaseItems().isEmpty() && purchase.getPurchaseItems() != null){
            //先删掉原来的item，再重新添加
            if(!purchaseItemsRepository.findByPurchaseId(purchase.getId()).isEmpty()){
                for(PurchaseItems item : purchaseItemsRepository.findByPurchaseId(purchase.getId())){
                    purchaseItemsRepository.delete(item);
                }
            }
            for(PurchaseItems item : purchase.getPurchaseItems()){
                item.setPurchase(purchase);
                item.setPurchaseCatalogItem(purchaseCatalogItemRepository.findById(item.getLabel()).orElse(null));
                purchaseItemsRepository.save(item);
            }
        } else {
            if(!purchaseItemsRepository.findByPurchaseId(purchase.getId()).isEmpty()){
                for(PurchaseItems item : purchaseItemsRepository.findByPurchaseId(purchase.getId())){
                    purchaseItemsRepository.delete(item);
                }
            }
        }
        return RespBean.success("保存采购单成功", purchaseRepository.save(purchase));
    }

    @Transactional
    @PostMapping(value = "editPurchase")
    public RespBean editPurchase(@RequestBody Purchase purchase){
        if(purchase.getPurchaseAuditStatus() == null || purchase.getPurchaseAuditStatus().getId() == 0){
            purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(0).get());
        }else{
            purchase.setPurchaseAuditStatus(purchaseAuditStatusRepository.findById(1).get());
        }
        //通过前端传过来的Budget.id设置Budget,无预算的话默认设置为0，预算单0标记为无预算
        if(purchase.getBudget().getId().isEmpty() || purchase.getBudget().getId() == null){
            purchase.setBudget(budgetRepository.findById("0").orElse(null));
        }else{
            purchase.setBudget(budgetRepository.findById(purchase.getBudget().getId()).orElse(null));
        }
        if(!purchaseItemsRepository.findByPurchaseId(purchase.getId()).isEmpty()){
            for(PurchaseItems item : purchaseItemsRepository.findByPurchaseId(purchase.getId())){
                purchaseItemsRepository.delete(item);
            }
        }
        for(PurchaseItems item : purchase.getPurchaseItems()){
            item.setPurchase(purchase);
            purchaseItemsRepository.save(item);
        }
        return RespBean.success("修改采购单成功", purchaseRepository.save(purchase));
    }

    @Transactional
    @PostMapping(value = "deletePurchase")
    public RespBean deletePurchase(@RequestBody Purchase purchase){
        //删除前删除审核日志和品目
        if(!purchaseItemsRepository.findByPurchaseId(purchase.getId()).isEmpty() ){
            List<PurchaseItems> purchaseItemList = purchaseItemsRepository.findByPurchaseId(purchase.getId());
            for(PurchaseItems items : purchaseItemList){
                purchaseItemsRepository.delete(items);
            }
        }
        if(!purchaseAuditLogRepository.findByPurchaseId(purchase.getId()).isEmpty()){
            List<PurchaseAuditLog> purchaseAuditLogList = purchaseAuditLogRepository.findByPurchaseId(purchase.getId());
            for(PurchaseAuditLog auditLog : purchaseAuditLogList){
                purchaseAuditLogRepository.delete(auditLog);
            }
        }
        purchaseRepository.delete(purchase);
        return RespBean.success("删除预算单成功");
    }

    @PostMapping(value = "getPurchase")
    public RespBean getPurchase(@RequestBody Purchase purchase){
        Purchase resPurchase = purchaseRepository.findById(purchase.getId()).orElse(null);
        //让resPurchase忽略掉items和log以免堆栈溢出，通过下面方法加载items和log
        if(!purchaseItemsRepository.findByPurchaseId(purchase.getId()).isEmpty()){
            List<PurchaseItems> purchaseItemList = purchaseItemsRepository.findByPurchaseId(purchase.getId());
            resPurchase.setPurchaseItems(purchaseItemList);
        }
        if(!purchaseAuditLogRepository.findByPurchaseId(purchase.getId()).isEmpty()){
            List<PurchaseAuditLog> purchaseAuditLogList = purchaseAuditLogRepository.findByPurchaseId(purchase.getId());
            resPurchase.setPurchaseAuditLogs(purchaseAuditLogList);
        }
        return RespBean.success("加载采购单成功", resPurchase);
    }

    //按角色权限显示对应的采购单
    @PostMapping(value = "getPurchases")
    public RespBean getPurchases(@RequestBody GetPurchasesReq purchasesReq){
        Pageable pageable = PageRequest.of(purchasesReq.getPageUtils().getCurrentPage(), purchasesReq.getPageUtils().getPageSize());
        User operator = userRepository.findByUsername(purchasesReq.getUser().getUsername()).orElse(null);
        if(!operator.getRoles().isEmpty()){
            int[] permission = {0,0,0}; //标记当前用户权限
            List<Role> roles = operator.getRoles();
            for(Role role : roles){
                if(role.getId().equals("1")){
                    permission[0] = 1;
                }else if(role.getId().equals("2") || role.getId().equals("3")){
                    permission[1] = 1;
                }else if(role.getId().equals("4") || role.getId().equals("5")){
                    permission[2] = 1;
                }
            }
            if(permission[2] == 1){
                //资产处和财务处显示所有的预算单
                Page<Purchase> purchaseList = purchaseRepository.findAll(pageable);
                return RespBean.success("加载采购单成功",purchaseList);
            }else if(permission[1] == 1){
                //采购管理员和单位分管负责人显示本单位的预算单
                Page<Purchase> purchaseList = purchaseRepository.findByOrg(pageable,operator.getOrg());
                return RespBean.success("加载采购单成功",purchaseList);
            }else if(permission[0] == 1){
                //普通用户显示自己的预算单
                Page<Purchase> purchaseList = purchaseRepository.findByOwner(pageable,operator);
                return RespBean.success("加载采购单成功",purchaseList);
            }
        }
        return RespBean.failure("当前用户没有权限查看");
    }
}
