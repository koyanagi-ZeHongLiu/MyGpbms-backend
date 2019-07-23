package com.example.gpbms.provider.repository;

import com.example.gpbms.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider,String> {
}
