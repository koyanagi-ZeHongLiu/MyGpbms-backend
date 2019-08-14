package com.example.gpbms.budget.repository;

import com.example.gpbms.budget.entity.Fund;
import com.example.gpbms.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FundRepository extends JpaRepository<Fund,String> {
    Optional<Fund> findByFundName(String fundName);
    Page<Fund> findByOwner(Pageable pageable, User owner);
}
