package com.example.gpbms.purchase.repository;

import com.example.gpbms.purchase.entity.purchase_item.TbPurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TbPurchaseItemRepository extends JpaRepository<TbPurchaseItem, Integer> {

    public TbPurchaseItem findByCode(String code);

    public List<TbPurchaseItem> findByOrderByCode();

}
