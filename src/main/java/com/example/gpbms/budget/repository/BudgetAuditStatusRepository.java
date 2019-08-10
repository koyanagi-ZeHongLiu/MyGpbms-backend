package com.example.gpbms.budget.repository;

import com.example.gpbms.budget.entity.BudgetAuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BudgetAuditStatusRepository extends JpaRepository<BudgetAuditStatus,String> {
    Optional<BudgetAuditStatus> findById(Integer id);
}
