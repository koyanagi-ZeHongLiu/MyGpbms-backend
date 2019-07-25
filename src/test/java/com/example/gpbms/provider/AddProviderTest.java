package com.example.gpbms.provider;

import com.example.gpbms.provider.entity.Provider;
import com.example.gpbms.provider.repository.ProviderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddProviderTest {
    @Autowired
    ProviderRepository providerRepository;
    @Test
    public void addProviderTest() {
        String[] strings = {"福建优胜招标代理有限公司", "福建中实招标有限公司","福建国诚招标有限公司","厦门市公物采购招投标有限公司",
        "福建方兴招标代理有限公司","福建立勤招标代理有限公司","福建省金丰招标代理有限公司","福建信发招标代理有限公司",
        "福建榕卫招标有限公司","福建省智信招标有限公司","福建景鑫招标有限公司","福建省天海招标有限公司","福建顺恒工程项目管理有限公司",
        "福建省福怡药械招标有限公司","福州华腾招标有限公司"};
        ArrayList<Provider> providers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Provider provider = new Provider(null, String.valueOf(i), strings[i - 1]);
            providers.add(provider);
        }
        providerRepository.saveAll(providers);
    }

}
