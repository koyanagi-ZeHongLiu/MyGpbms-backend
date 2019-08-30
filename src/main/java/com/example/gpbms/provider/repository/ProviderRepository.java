package com.example.gpbms.provider.repository;

import com.example.gpbms.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProviderRepository extends JpaRepository<Provider,String> {
    Optional<Provider> findByProviderName(String name);
}
