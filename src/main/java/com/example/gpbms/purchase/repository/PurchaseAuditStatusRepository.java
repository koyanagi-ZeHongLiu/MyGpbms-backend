package com.example.gpbms.purchase.repository;

import com.example.gpbms.purchase.entity.PurchaseAuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseAuditStatusRepository extends JpaRepository<PurchaseAuditStatus,String> {
}
