package com.example.gpbms.user.controller;

import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.Role;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.user.repository.RoleRepository;
import com.example.gpbms.user.repository.UserRepository;
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
import java.util.List;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    @PostMapping(value = "saveUser")
    public void saveUser(@RequestBody User user) {
        //默认角色为经办人
        if(user.getRoles().isEmpty()){
            user.getRoles().add(roleRepository.findById("1").get());
        }
        userRepository.save(user);
    }

    @Transactional
    @PostMapping(value = "editUser")
    public void editUser(@RequestBody User user) {
        // 从数据库取出要修改的user
        User pesistUser = userRepository.findById(user.getId()).orElse(null);
        //如果前台传回的密码为空，则密码保持不变,反之，重置密码
        if (user.getPassword() == null) {
            user.setPassword(pesistUser.getPassword());
        }
        //如果该用户在数据库里本来就有角色，先移除，再重新添加
        if(pesistUser.getRoles() != null){
            List<Role> roles = pesistUser.getRoles();
            //解除关系，只删除中间表记录。
            roles.forEach(role -> role.getUsers().remove(pesistUser));
        }
        userRepository.save(user);
    }
    @Transactional
    @PostMapping(value = "deleteUser")
    public void deleteUser(@RequestBody User user) {
        User u = userRepository.findById(user.getId()).get();
        //删除用户前先删除角色
        if(u.getRoles() != null){
            List<Role> roles = u.getRoles();
            roles.forEach(role -> role.getUsers().remove(u));
        }
        userRepository.delete(user);
    }

    @PostMapping(value = "getUser")
    public User getUser(@RequestBody User user){
       return userRepository.findById(user.getId()).orElse(null);
    }

    @PostMapping(value = "getUsers")
    public Page<User> getUsers(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<User> userList = userRepository.findAll(pageable);
        return userList;
    }

    @PostMapping(value = "getUsersByOrg")
    public List<User> getUsersByOrg(@RequestBody Org org){
        return userRepository.findByOrgId(org.getId());
    }
}
