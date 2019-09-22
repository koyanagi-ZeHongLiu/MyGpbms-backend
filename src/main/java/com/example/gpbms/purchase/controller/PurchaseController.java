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
import com.example.gpbms.util.ExcelUtil;
import com.example.gpbms.util.RespBean;
import jodd.util.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping(value = "exportPurchase")
    public void exportPurchase(@RequestBody Purchase purchase1, HttpServletResponse response) throws Exception {
        Purchase purchase = purchaseRepository.findById(purchase1.getId()).orElseThrow(RuntimeException::new);
        InputStream in = null;
        if(purchase.getProjectType()!=null) {
            if(purchase.getProjectType().equals("工程项目")) {
                in = this.getClass().getResourceAsStream("/template/福建师范大学工程项目申购表.xlsx");
            }else {
                in = this.getClass().getResourceAsStream("/template/福建师范大学货物和服务申购表.xls");
            }
        }else {
            in = this.getClass().getResourceAsStream("/template/福建师范大学货物和服务申购表.xls");
        }

        response.setCharacterEncoding(StringPool.UTF_8);
        response.setHeader("content-disposition", "attachment; filename=" + URLEncoder.encode("文件名", "UTF-8"));
        response.setContentType("application/vnd.ms-excel");

        ServletOutputStream os = response.getOutputStream();

        Map<String, Object> beanMaps = new HashMap<>();
        beanMaps.put("purchase", purchase);

        List<PurchaseAuditLog> logs = purchaseAuditLogRepository.findByPurchaseId(purchase.getId());

        for (PurchaseAuditLog purchaseLog : logs) {
            String message = purchaseLog.getAuditInfo();
            if (message.contains("采购管理员审核通过")) {
                beanMaps.put("r1", message.substring(message.indexOf("审核意见：")));
                beanMaps.put("r1Name",purchaseLog.getAuditor().getRealName());
                beanMaps.put("r1Time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(purchaseLog.getCreateTime()));
            }
            if (message.contains("单位负责人审核通过")) {
                beanMaps.put("r2", message.substring(message.indexOf("审核意见：")));
                beanMaps.put("r2Name",purchaseLog.getAuditor().getRealName());
                beanMaps.put("r2Time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(purchaseLog.getCreateTime()));
            }
            if (message.contains("资产处审核通过")) {
                beanMaps.put("r5", message.substring(message.indexOf("审核意见：")));
                beanMaps.put("r5Name",purchaseLog.getAuditor().getRealName());
                beanMaps.put("r5Time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(purchaseLog.getCreateTime()));
            }
        }
        ExcelUtil.innerExport(in, os, beanMaps);
    }

    @PostMapping(value = "exportPurchaseRecord")
    public void exportPurchaseRecord(@RequestBody Purchase purchase2, HttpServletResponse response) throws IOException, ParseException {
        Purchase purchase = purchaseRepository.findById(purchase2.getId()).orElseThrow(RuntimeException::new);

        InputStream in = null;
        String purchaseType = purchase.getPurchaseType();//获取采购类型
        if (purchaseType != null) {
            if (purchaseType.equals("直接购买")) {
                in = this.getClass().getResourceAsStream("/template/直接购买备案表 .xls");
            } else if (purchaseType.equals("自行组织采购")) {
                in = this.getClass().getResourceAsStream("/template/自行组织采购备案表.xls");
            }else if (purchaseType.equals("网上超市采购")) {
                in = this.getClass().getResourceAsStream("/template/委托招标采购备案表-货物与服务项目.xls");
            }else if (purchaseType.equals("委托招标")) {
                if(purchase.getProjectType()!=null) {
                    if (purchase.getProjectType().equals("工程项目")) {
                        in = this.getClass().getResourceAsStream("/template/委托招标采购备案表-工程项目.xls");
                    }else {
                        in = this.getClass().getResourceAsStream("/template/委托招标采购备案表-货物与服务项目.xls");
                    }
                }else {
                    in = this.getClass().getResourceAsStream("/template/委托招标采购备案表-货物与服务项目.xls");
                }
            }
        } else {
            //TODO 抛出异常，正常情况下采购类型不应为空.
            in = this.getClass().getResourceAsStream("/template/直接购买备案表 .xls");
        }

        response.setCharacterEncoding(StringPool.UTF_8);
        response.setHeader("content-disposition", "attachment; filename=" + URLEncoder.encode("文件名", "UTF-8"));
        response.setContentType("application/vnd.ms-excel");

        ServletOutputStream os = response.getOutputStream();

        HashMap<String, Object> beanMaps = new HashMap<>();
        beanMaps.put("purchase", purchase);

        ExcelUtil.innerExport(in,os,beanMaps);
    }
}
