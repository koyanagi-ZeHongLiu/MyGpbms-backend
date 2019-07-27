package com.example.gpbms.user.controller;

import com.example.gpbms.user.entity.Role;
import com.example.gpbms.user.repository.RoleRepository;
import com.example.gpbms.util.PageUtils;
import com.example.gpbms.util.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("api")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @PostMapping(value = "saveRole")
    public RespBean saveRole(@RequestBody Role role){
        return RespBean.success("添加角色成功",roleRepository.save(role));
    }

    @Transactional
    @PostMapping(value = "deleteRole")
    public RespBean deleteRole(@RequestBody Role role){
        roleRepository.delete(role);
        return RespBean.success("删除角色成功");
    }

    @PostMapping(value = "getRole")
    public RespBean getRole(@RequestBody Role role){
        return RespBean.success("加载角色成功",roleRepository.findById(role.getId()).orElse(null));
    }

    @PostMapping(value = "getRoles")
    public RespBean getRoles(){
        List<Role> roles = roleRepository.findAll();
        return RespBean.success("加载角色成功",roles);
    }
}
