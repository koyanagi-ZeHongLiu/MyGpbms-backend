package com.example.gpbms.purchase.repository;

import com.example.gpbms.purchase.entity.PurchaseItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseItemsRepository extends JpaRepository<PurchaseItems,String> {
}
