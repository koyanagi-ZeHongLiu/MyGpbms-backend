package com.example.gpbms.budget.request;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.budget.entity.BudgetAuditLog;
import com.example.gpbms.user.entity.User;
import lombok.Data;

@Data
public class AuditBudgetReq {
    private BudgetAuditLog budgetAuditLog;
    private Budget budget;
    private User operator;
}
