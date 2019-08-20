package com.example.gpbms.purchase.repository;

import com.example.gpbms.purchase.entity.PurchaseAuditLog;
import com.example.gpbms.purchase.entity.PurchaseItems;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseAuditLogRepository extends JpaRepository<PurchaseAuditLog,String> {
    List<PurchaseAuditLog> findByPurchaseId(String id);
}
