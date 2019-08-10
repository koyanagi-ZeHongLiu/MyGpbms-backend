package com.example.gpbms.budget.controller;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.budget.entity.BudgetAuditLog;
import com.example.gpbms.budget.repository.BudgetAuditLogRepository;
import com.example.gpbms.budget.repository.BudgetAuditStatusRepository;
import com.example.gpbms.budget.repository.BudgetRepository;
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

@RestController
@RequestMapping("api")
public class BudgetAuditLogController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetAuditLogRepository budgetAuditLogRepository;

    @Autowired
    private BudgetAuditStatusRepository budgetAuditStatusRepository;

    @Transactional
    @PostMapping(value = "approveBudget")
    public RespBean approveBudget(@RequestBody BudgetAuditLog budgetAuditLog){
        //审核通过
        Budget budget = budgetRepository.findById(budgetAuditLog.getBudget().getId()).orElse(null);
        budget.setBudgetAuditStatus(budgetAuditStatusRepository.findById(budget.getBudgetAuditStatus().getId()+1).orElse(null));
        budgetAuditLog.setBudget(budget);
        //前端传过来当前用户id(审核人)
        budgetAuditLog.setAuditor(userRepository.findById(budgetAuditLog.getAuditor().getId()).orElse(null));
        return RespBean.success("审核成功", budgetAuditLogRepository.save(budgetAuditLog));
    }

    @Transactional
    @PostMapping(value = "rejectBudget")
    public RespBean rejectBudget(@RequestBody BudgetAuditLog budgetAuditLog){
        //驳回
        Budget budget = budgetRepository.findById(budgetAuditLog.getBudget().getId()).orElse(null);
        if(budget.getBudgetAuditStatus().getId() == 0){
            return RespBean.success("驳回失败");
        }
        budget.setBudgetAuditStatus(budgetAuditStatusRepository.findById(budget.getBudgetAuditStatus().getId()-1).orElse(null));
        budgetAuditLog.setBudget(budget);
        //前端传过来当前用户id(审核人)
        budgetAuditLog.setAuditor(userRepository.findById(budgetAuditLog.getAuditor().getId()).orElse(null));
        return RespBean.success("审核成功", budgetAuditLogRepository.save(budgetAuditLog));
    }
}
