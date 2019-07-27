package com.example.gpbms.provider.controller;

import com.example.gpbms.provider.entity.Provider;
import com.example.gpbms.provider.repository.ProviderRepository;
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

@RestController
@RequestMapping("api")
public class ProviderController {

    @Autowired
    private ProviderRepository providerRepository;

    @Transactional
    @PostMapping(value = "saveProvider")
    public RespBean saveProvider(@RequestBody Provider provider){
        return RespBean.success("代理机构添加成功",providerRepository.save(provider));
    }

    @Transactional
    @PostMapping(value = "deleteProvider")
    public RespBean deleteProvider(@RequestBody Provider provider){
        providerRepository.delete(provider);
        return RespBean.success("删除代理机构成功");
    }

    @PostMapping(value = "getProvider")
    public RespBean getProvider(@RequestBody Provider provider){
        return RespBean.success("加载代理机构成功",providerRepository.findById(provider.getId()).orElse(null));
    }

    @PostMapping(value = "getProviders")
    public RespBean getProviders(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<Provider> providerList = providerRepository.findAll(pageable);
        return RespBean.success("加载代理机构成功",providerList);
    }
}
