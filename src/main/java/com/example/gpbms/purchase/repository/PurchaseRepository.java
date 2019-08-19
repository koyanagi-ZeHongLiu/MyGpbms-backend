package com.example.gpbms.purchase.repository;


import com.example.gpbms.purchase.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase,String> {
}
