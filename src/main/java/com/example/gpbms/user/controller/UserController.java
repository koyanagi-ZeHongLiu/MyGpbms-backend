package com.example.gpbms.user.controller;

import com.example.gpbms.user.entity.User;
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

    @Transactional
    @PostMapping(value = "saveUser")
    public void saveUser(@RequestBody User user) {
        userRepository.save(user);
    }

    @Transactional
    @PostMapping(value = "deleteUser")
    public void deleteUser(@RequestBody User user) {
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

}
