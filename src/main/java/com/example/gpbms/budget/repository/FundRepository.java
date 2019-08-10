package com.example.gpbms.budget.repository;

import com.example.gpbms.budget.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FundRepository extends JpaRepository<Fund,String> {
    Optional<Fund> findByFundName(String fundName);
}
