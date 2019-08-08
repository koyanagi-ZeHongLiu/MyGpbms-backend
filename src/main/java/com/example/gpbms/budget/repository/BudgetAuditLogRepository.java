package com.example.gpbms.budget.repository;

import com.example.gpbms.budget.entity.BudgetAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetAuditLogRepository extends JpaRepository<BudgetAuditLog,String> {
    List<BudgetAuditLog> findByBudgetId(String id);
}
