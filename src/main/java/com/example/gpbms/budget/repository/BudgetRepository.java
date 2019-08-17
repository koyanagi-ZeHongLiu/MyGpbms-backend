package com.example.gpbms.budget.repository;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget,String> {
    Page<Budget> findByOrg(Pageable pageable, Org org);
    Page<Budget> findByOwner(Pageable pageable, User owner);
    Budget findByBudgetCode(String code);
}
