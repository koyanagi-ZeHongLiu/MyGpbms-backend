package com.example.gpbms.user.repository;

import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByRealName(String realName);
    Optional<User> findByUsername(String username);
    List<User> findByOrgId(String orgId);
}
