package com.example.gpbms.budget.controller;

import com.example.gpbms.budget.entity.BudgetAuditLog;
import com.example.gpbms.budget.repository.BudgetAuditLogRepository;
import com.example.gpbms.util.PageUtils;
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
public class BudgetAuditLogController {

    @Autowired
    private BudgetAuditLogRepository budgetAuditLogRepository;

    @Transactional
    @PostMapping(value = "saveBudgetAuditLog")
    public RespBean saveBudgetAuditLog(@RequestBody BudgetAuditLog budgetAuditLog){
        return RespBean.success("信息保存成功", budgetAuditLogRepository.save(budgetAuditLog));
    }

    @Transactional
    @PostMapping(value = "deleteBudgetAuditLog")
    public RespBean deleteBudgetAuditLog(@RequestBody BudgetAuditLog budgetAuditLog){
        budgetAuditLogRepository.delete(budgetAuditLog);
        return RespBean.success("信息删除成功");
    }

    @PostMapping(value = "getBudgetAuditLog")
    public RespBean getBudgetAuditLog(@RequestBody BudgetAuditLog budgetAuditLog){
        return RespBean.success("加载审核成功", budgetAuditLogRepository.findById(budgetAuditLog.getId()).orElse(null));
    }

    @PostMapping(value = "getBudgetAuditLogs")
    public RespBean getBudgetAuditLogs(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<BudgetAuditLog> budgetAuditLogList = budgetAuditLogRepository.findAll(pageable);
        return RespBean.success("加载经费单成功",budgetAuditLogList);
    }
}
