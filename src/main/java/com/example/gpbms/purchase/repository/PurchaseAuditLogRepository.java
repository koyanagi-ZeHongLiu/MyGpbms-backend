package com.example.gpbms.purchase.repository;

import com.example.gpbms.purchase.entity.PurchaseAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseAuditLogRepository extends JpaRepository<PurchaseAuditLog,String> {
}
