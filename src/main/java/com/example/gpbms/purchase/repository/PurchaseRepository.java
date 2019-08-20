package com.example.gpbms.purchase.repository;


import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase,String> {
    Page<Purchase> findByOrg(Pageable pageable, Org org);
    Page<Purchase> findByOwner(Pageable pageable, User owner);
}
