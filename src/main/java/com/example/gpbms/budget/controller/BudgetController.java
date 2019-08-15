package com.example.gpbms.budget.controller;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.budget.entity.BudgetAuditLog;
import com.example.gpbms.budget.entity.BudgetItems;
import com.example.gpbms.budget.repository.*;
import com.example.gpbms.budget.request.GetBudgetsReq;
import com.example.gpbms.user.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private FundRepository fundRepository;


    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetItemsRepository budgetItemsRepository;

    @Autowired
    private BudgetAuditLogRepository budgetAuditLogRepository;

    @Autowired
    private BudgetAuditStatusRepository budgetAuditStatusRepository;

    @Transactional
    @PostMapping(value = "saveBudget")
    public RespBean saveBudget(@RequestBody Budget budget){
        //在前端设置好budgetStatusId，是保存还是提交
        //0:保存1:提交2:采购管理员审核通过3:单位负责人审核通过4:资产处审核通过5:财务处审核通过（即完成）
        //状态信息保存在数据库
        if(budget.getBudgetAuditStatus() == null || budget.getBudgetAuditStatus().getId() == 0){
            budget.setBudgetAuditStatus(budgetAuditStatusRepository.findById(0).get());
        }else{
            budget.setBudgetAuditStatus(budgetAuditStatusRepository.findById(1).get());
        }
        //通过前端传过来的owner.realName设置owner
        budget.setOwner(userRepository.findByRealName(budget.getOwner().getRealName()).orElse(null));
        //通过前端传过来的fund.fundName设置fund
        budget.setFund(fundRepository.findByFundName(budget.getFund().getFundName()).orElse(null));
        //将前端传过来的List<BudgetItems>按个存入BudgetItems表
        if(!budget.getBudgetItems().isEmpty() && budget.getBudgetItems() != null){
            for(BudgetItems item : budget.getBudgetItems()){
                item.setBudget(budget);
                budgetItemsRepository.save(item);
            }
        }
        return RespBean.success("保存预算单成功", budgetRepository.save(budget));
    }

    @Transactional
    @PostMapping(value = "editBudget")
    public RespBean editBudget(@RequestBody Budget budget){
        budget.setOwner(userRepository.findByRealName(budget.getOwner().getRealName()).orElse(null));
        budget.setFund(fundRepository.findByFundName(budget.getFund().getFundName()).orElse(null));
        if(!budget.getBudgetItems().isEmpty() && budget.getBudgetItems() != null){
            //先删掉原来的item，再重新添加
            Budget oldBudget = budgetRepository.findById(budget.getId()).orElse(null);
            if(!oldBudget.getBudgetItems().isEmpty() && oldBudget.getBudgetItems() != null){
                for(BudgetItems item : oldBudget.getBudgetItems()){
                    budgetItemsRepository.delete(item);
                }
            }
            for(BudgetItems item : budget.getBudgetItems()){
                item.setBudget(budget);
                budgetItemsRepository.save(item);
            }
        }
        return RespBean.success("修改预算单成功", budgetRepository.save(budget));
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
        Budget resBudget = budgetRepository.findById(budget.getId()).orElse(null);
        //让resBudget忽略掉items和log以免堆栈溢出，通过下面方法加载items和log
        if(!budgetItemsRepository.findByBudgetId(budget.getId()).isEmpty() && budgetItemsRepository.findByBudgetId(budget.getId())!=null){
            List<BudgetItems> budgetItemList = budgetItemsRepository.findByBudgetId(budget.getId());
            resBudget.setBudgetItems(budgetItemList);
        }
        if(!budgetAuditLogRepository.findByBudgetId(budget.getId()).isEmpty() && budgetAuditLogRepository.findByBudgetId(budget.getId())!=null){
            List<BudgetAuditLog> budgetAuditLogList = budgetAuditLogRepository.findByBudgetId(budget.getId());
            resBudget.setBudgetAuditLogs(budgetAuditLogList);
        }
        return RespBean.success("加载预算单成功", resBudget);
    }

    @PostMapping(value = "getBudgets")
    public RespBean getBudgets(@RequestBody GetBudgetsReq budgetsReq){
        Pageable pageable = PageRequest.of(budgetsReq.getPageUtils().getCurrentPage(), budgetsReq.getPageUtils().getPageSize());
        Page<Budget> budgetList = budgetRepository.findAll(pageable);
        return RespBean.success("加载经费单成功",budgetList);
    }

}
