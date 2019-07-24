package com.example.gpbms.purchase.repository;

import com.example.gpbms.purchase.entity.PurchaseCatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseCatalogItemRepository extends JpaRepository<PurchaseCatalogItem,String> {
}
