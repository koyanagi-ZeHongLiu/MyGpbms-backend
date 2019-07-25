package com.example.gpbms.user.controller;

import com.example.gpbms.user.entity.Role;
import com.example.gpbms.user.repository.RoleRepository;
import com.example.gpbms.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("api")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @PostMapping(value = "saveRole")
    public void saveRole(@RequestBody Role role){
        roleRepository.save(role);
    }

    @Transactional
    @PostMapping(value = "deleteRole")
    public void deleteRole(@RequestBody Role role){
        roleRepository.delete(role);
    }

    @PostMapping(value = "getRole")
    public Role getRole(@RequestBody Role role){
        return roleRepository.findById(role.getId()).orElse(null);
    }

    @PostMapping(value = "getRoles")
    public Page<Role> getRoles(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<Role> roleList = roleRepository.findAll(pageable);
        return roleList;
    }
}
