package com.example.gpbms.user.controller;

import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.Role;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.user.repository.OrgRepository;
import com.example.gpbms.user.repository.RoleRepository;
import com.example.gpbms.user.repository.UserRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("api")
public class OrgController {

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @PostMapping(value = "saveOrg")
    public RespBean saveOrg(@RequestBody Org org){
        return RespBean.success("添加单位成功",orgRepository.save(org));
    }

    @Transactional
    @PostMapping(value = "editOrg")
    public RespBean editOrg(@RequestBody Org org){
        //经办人
        Role role1 = roleRepository.findById("1").get();
        //采购管理员
        Role role2 = roleRepository.findById("2").get();
        //单位负责人
        Role role3 = roleRepository.findById("3").get();
        //先找到原来的采购管理员和单位负责人，重置他们的权限为经办人
        Org oldOrg = orgRepository.findById(org.getId()).get();
        if(oldOrg.getPurchaseAdmin() != null){
            User oldPurchaseAdmin = userRepository.findByRealName(oldOrg.getPurchaseAdmin()).get();
            if(!oldPurchaseAdmin.getRoles().isEmpty()){
                List<Role> roles = oldPurchaseAdmin.getRoles();
                roles.forEach(role -> role.getUsers().remove(oldPurchaseAdmin));
                oldPurchaseAdmin.getRoles().clear();
                oldPurchaseAdmin.getRoles().add(role1);
                userRepository.save(oldPurchaseAdmin);
            }
        }
        if(oldOrg.getOrgAdmin()!= null){
            User oldOrgAdmin = userRepository.findByRealName(oldOrg.getOrgAdmin()).get();
            if(!oldOrgAdmin.getRoles().isEmpty()){
                List<Role> roles = oldOrgAdmin.getRoles();
                roles.forEach(role -> role.getUsers().remove(oldOrgAdmin));
                oldOrgAdmin.getRoles().clear();
                oldOrgAdmin.getRoles().add(role1);
                userRepository.save(oldOrgAdmin);
            }
        }

        User purchaseAdmin = userRepository.findByRealName(org.getPurchaseAdmin()).get();
        purchaseAdmin.getRoles().add(role2);
        // 同时要设置该采购管理员的单位为当前修改单位
        purchaseAdmin.setOrg(org);
        userRepository.save(purchaseAdmin);
        User orgAdmin = userRepository.findByRealName(org.getOrgAdmin()).get();
        orgAdmin.getRoles().add(role3);
        // 同时要设置该单位负责人的单位为当前修改单位
        orgAdmin.setOrg(org);
        userRepository.save(orgAdmin);
        return RespBean.success("修改单位成功",orgRepository.save(org));
    }

    @Transactional
    @PostMapping(value = "deleteOrg")
    public RespBean deleteOrg(@RequestBody Org org){
        orgRepository.delete(org);
        return RespBean.success("删除单位成功");
    }

    @PostMapping(value = "getOrg")
    public RespBean getOrg(@RequestBody Org org){
        return RespBean.success("加载单位成功",orgRepository.findById(org.getId()).orElse(null));
    }

    @PostMapping(value = "getOrgs")
    public RespBean getOrgs(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<Org> orgList = orgRepository.findAll(pageable);
        return RespBean.success("加载单位成功",orgList);
    }

}
