package com.example.gpbms.purchase.repository;

import com.example.gpbms.purchase.entity.PurchaseAuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseAuditStatusRepository extends JpaRepository<PurchaseAuditStatus,String> {
    Optional<PurchaseAuditStatus> findById(Integer id);
}
