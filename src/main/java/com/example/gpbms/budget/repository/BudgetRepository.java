package com.example.gpbms.budget.repository;

import com.example.gpbms.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget,String> {
}
