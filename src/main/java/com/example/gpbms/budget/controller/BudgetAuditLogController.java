package com.example.gpbms.budget.controller;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.budget.entity.BudgetAuditLog;
import com.example.gpbms.budget.repository.BudgetAuditLogRepository;
import com.example.gpbms.budget.repository.BudgetAuditStatusRepository;
import com.example.gpbms.budget.repository.BudgetRepository;
import com.example.gpbms.budget.request.AuditBudgetReq;
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
    public RespBean approveBudget(@RequestBody AuditBudgetReq auditBudgetReq){
        //审核通过
        auditBudgetReq.getBudgetAuditLog().setAuditor(auditBudgetReq.getOperator());
        Budget budget = auditBudgetReq.getBudget();
        budget.setBudgetAuditStatus(budgetAuditStatusRepository.findById(budget.getBudgetAuditStatus().getId()+1).orElse(null));
        budgetRepository.save(budget);
        auditBudgetReq.getBudgetAuditLog().setBudget(budget);

        if(!budget.getBudgetAuditLogs().isEmpty() && budget.getBudgetAuditLogs() != null) {
            List<BudgetAuditLog> budgetAuditLogList = budget.getBudgetAuditLogs();
            budgetAuditLogList.add(auditBudgetReq.getBudgetAuditLog());
            budget.setBudgetAuditLogs(budgetAuditLogList);
        }
        return RespBean.success("审核成功", budgetAuditLogRepository.save(auditBudgetReq.getBudgetAuditLog()));
    }

    @Transactional
    @PostMapping(value = "rejectBudget")
    public RespBean rejectBudget(@RequestBody AuditBudgetReq auditBudgetReq){
        //驳回
        Budget budget = auditBudgetReq.getBudget();
        if(budget.getBudgetAuditStatus().getId() == 0){
            return RespBean.failure("驳回失败");
        }
        auditBudgetReq.getBudgetAuditLog().setAuditor(auditBudgetReq.getOperator());
        budget.setBudgetAuditStatus(budgetAuditStatusRepository.findById(budget.getBudgetAuditStatus().getId()-1).orElse(null));
        budgetRepository.save(budget);
        auditBudgetReq.getBudgetAuditLog().setBudget(budget);

        if(!budget.getBudgetAuditLogs().isEmpty() && budget.getBudgetAuditLogs() != null) {
            List<BudgetAuditLog> budgetAuditLogList = budget.getBudgetAuditLogs();
            budgetAuditLogList.add(auditBudgetReq.getBudgetAuditLog());
            budget.setBudgetAuditLogs(budgetAuditLogList);
        }
        return RespBean.success("驳回成功", budgetAuditLogRepository.save(auditBudgetReq.getBudgetAuditLog()));
    }
}
