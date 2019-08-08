package com.example.gpbms.budget.controller;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.budget.entity.BudgetAuditLog;
import com.example.gpbms.budget.entity.BudgetItems;
import com.example.gpbms.budget.repository.BudgetAuditLogRepository;
import com.example.gpbms.budget.repository.BudgetItemsRepository;
import com.example.gpbms.budget.repository.BudgetRepository;
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
import java.util.List;

@RestController
@RequestMapping("api")
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetItemsRepository budgetItemsRepository;

    @Autowired
    private BudgetAuditLogRepository budgetAuditLogRepository;

    @Transactional
    @PostMapping(value = "saveBudget")
    public RespBean saveBudget(@RequestBody Budget budget){
        //在前端设置好budgetStatus，是保存还是提交
        if(budget.getBudgetStatus() == null){
            budget.setBudgetStatus(0);
        }
        if(!budget.getBudgetItems().isEmpty() && budget.getBudgetItems() != null){
            for(BudgetItems item : budget.getBudgetItems()){
                item.setBudget(budget);
                budgetItemsRepository.save(item);
            }
        }
        return RespBean.success("保存预算单成功", budgetRepository.save(budget));
    }

    @Transactional
    @PostMapping(value = "deleteBudget")
    public RespBean deleteBudget(@RequestBody Budget budget){
        //删除前删除审核日志和品目
        if(!budgetItemsRepository.findByBudgetId(budget.getId()).isEmpty() && budgetItemsRepository.findByBudgetId(budget.getId())!=null ){
            List<BudgetItems> budgetItemList = budgetItemsRepository.findByBudgetId(budget.getId());
            for(BudgetItems items : budgetItemList){
                budgetItemsRepository.delete(items);
            }
        }
        if(!budgetAuditLogRepository.findByBudgetId(budget.getId()).isEmpty() && budgetAuditLogRepository.findByBudgetId(budget.getId())!=null){
            List<BudgetAuditLog> budgetAuditLogList = budgetAuditLogRepository.findByBudgetId(budget.getId());
            for(BudgetAuditLog auditLog : budgetAuditLogList){
                budgetAuditLogRepository.delete(auditLog);
            }
        }
        budgetRepository.delete(budget);
        return RespBean.success("删除预算单成功");
    }

    @PostMapping(value = "getBudget")
    public RespBean getBudget(@RequestBody Budget budget){
        List<BudgetItems> budgetItemList = budgetItemsRepository.findByBudgetId(budget.getId());
        budget.setBudgetItems(budgetItemList);
        List<BudgetAuditLog> budgetAuditLogList = budgetAuditLogRepository.findByBudgetId(budget.getId());
        budget.setBudgetAuditLogs(budgetAuditLogList);
        return RespBean.success("加载预算单成功", budgetRepository.findById(budget.getId()).orElse(null));
    }

    @PostMapping(value = "getBudgets")
    public RespBean getBudgets(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<Budget> budgetList = budgetRepository.findAll(pageable);
        return RespBean.success("加载经费单成功",budgetList);
    }

}
