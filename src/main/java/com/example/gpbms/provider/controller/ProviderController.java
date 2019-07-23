package com.example.gpbms.provider.controller;

import com.example.gpbms.provider.entity.Provider;
import com.example.gpbms.provider.repository.ProviderRepository;
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
public class ProviderController {

    @Autowired
    private ProviderRepository providerRepository;

    @Transactional
    @PostMapping(value = "saveProvider")
    public void saveProvider(@RequestBody Provider provider){
        providerRepository.save(provider);
    }

    @Transactional
    @PostMapping(value = "deleteProvider")
    public void deleteProvider(@RequestBody Provider provider){
        providerRepository.delete(provider);
    }

    @PostMapping(value = "getProvider")
    public Provider getProvider(@RequestBody Provider provider){
        return providerRepository.findById(provider.getId()).orElse(null);
    }

    @PostMapping(value = "getProviders")
    public Page<Provider> getProviders(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<Provider> providerList = providerRepository.findAll(pageable);
        return providerList;
    }
}
