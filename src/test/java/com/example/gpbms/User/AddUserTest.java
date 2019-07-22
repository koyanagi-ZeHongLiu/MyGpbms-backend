package com.example.gpbms.User;

import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.user.repository.OrgRepository;
import com.example.gpbms.user.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)

public class AddUserTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrgRepository orgRepository;
    @Test
    public void addUser() {
        for (int i = 0; i < 50; i++) {
            User user = new User();
            if (i < 10) {
                user.setUsername("Test00" + i);
                user.setRealName("测试账号00" + i);
            }else {
                user.setUsername("Test0" + i);
                user.setRealName("测试账号0" + i);
            }
            user.setPassword("123456");
            user.setPhoneNumber("18812345678");
            userRepository.save(user);
        }
    }

    @Test
    public void addOrg() {
        for (int i = 0; i < 20; i++) {
            Org org = new Org();
            if (i < 10) {
                org.setOrgCode("000" + i);
                org.setOrgName("测试用学院00" + i);
            }else {
                org.setOrgCode("00" + i);
                org.setOrgName("测试用学院00" + i);
            }
            orgRepository.save(org);
        }
    }
}
