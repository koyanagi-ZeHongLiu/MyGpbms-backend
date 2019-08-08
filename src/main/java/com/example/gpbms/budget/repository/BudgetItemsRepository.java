package com.example.gpbms.budget.repository;

import com.example.gpbms.budget.entity.BudgetItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetItemsRepository extends JpaRepository<BudgetItems,String> {
    List<BudgetItems> findByBudgetId(String id);
}
