package com.example.gpbms.user.controller;

import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.Role;
import com.example.gpbms.user.entity.User;
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
import java.util.*;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @PostMapping(value = "saveUser")
    public RespBean saveUser(@RequestBody User user) {
        //默认角色为经办人
        if(user.getRoles().isEmpty()){
            user.getRoles().add(roleRepository.findById("1").get());
        }

       return RespBean.success("添加用户成功",userRepository.save(user));
    }

    @Transactional
    @PostMapping(value = "editUser")
    public RespBean editUser(@RequestBody User user) {
        // 从数据库取出要修改的user
        User pesistUser = userRepository.findById(user.getId()).orElse(null);
        //如果前台传回的密码为空，则密码保持不变,反之，重置密码
        if (user.getPassword() == null) {
            user.setPassword(pesistUser.getPassword());
        }
        //如果该用户在数据库里本来就有角色，先移除，再重新添加
        if(!pesistUser.getRoles().isEmpty()){
            List<Role> roles = pesistUser.getRoles();
            //解除关系，只删除中间表记录。
            roles.forEach(role -> role.getUsers().remove(pesistUser));
        }
        return RespBean.success("修改用户信息成功",userRepository.save(user));
    }

    @Transactional
    @PostMapping(value = "updateUserRoles")
    public RespBean updateUserRoles(@RequestBody Map<String ,Object> map) {
        String userId = (String) map.get("userId");
        User user = userRepository.findById(userId).get();
        //如果该用户在数据库里本来就有角色，先移除，再重新添加
        if(!user.getRoles().isEmpty()){
            List<Role> roles = user.getRoles();
            //解除关系，只删除中间表记录。
            roles.forEach(role -> role.getUsers().remove(user));
        }
        //新的角色集
        ArrayList<Role> roles = new ArrayList<>();
        // 拿到该用户新的角色集的id数组
        ArrayList rids = (ArrayList) map.get("rids");
        for (int i = 0; i < rids.size(); i++) {
            roles.add(roleRepository.findById((String) rids.get(i)).get());
        }
        //重新设置该用户的角色集
        user.setRoles(roles);
        return RespBean.success("修改用户信息成功",userRepository.save(user));
    }

    @Transactional
    @PostMapping(value = "deleteUser")
    public RespBean deleteUser(@RequestBody User user) {
        User u = userRepository.findById(user.getId()).get();
        //删除用户前先删除角色
        if(!u.getRoles().isEmpty()){
            List<Role> roles = u.getRoles();
            roles.forEach(role -> role.getUsers().remove(u));
        }
        userRepository.delete(user);
        return RespBean.success("删除用户成功");
    }

    @PostMapping(value = "getUser")
    public RespBean getUser(@RequestBody User user){
        //msg显不显示前端可控
        return RespBean.success("加载用户信息成功",userRepository.findById(user.getId()).orElse(null));
    }

    @PostMapping(value = "getUsers")
    public RespBean getUsers(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<User> userList = userRepository.findAll(pageable);
        return RespBean.success("加载用户信息成功",userList);
    }

    @PostMapping(value = "getUsersByOrg")
    public RespBean getUsersByOrg(@RequestBody Org org){
        return RespBean.success("加载用户信息成功",userRepository.findByOrgId(org.getId()));
    }
}
