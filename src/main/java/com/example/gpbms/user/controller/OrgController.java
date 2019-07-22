package com.example.gpbms.user.controller;

import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.repository.OrgRepository;
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
public class OrgController {

    @Autowired
    private OrgRepository orgRepository;

    @Transactional
    @PostMapping(value = "saveOrg")
    public void saveOrg(@RequestBody Org org){
        orgRepository.save(org);
    }

    @Transactional
    @PostMapping(value = "deleteOrg")
    public void deleteOrg(@RequestBody Org org){
        orgRepository.delete(org);
    }

    @PostMapping(value = "getOrg")
    public Org getOrg(@RequestBody Org org){
        return orgRepository.findById(org.getId()).orElse(null);
    }

    @PostMapping(value = "getOrgs")
    public Page<Org> getOrgs(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<Org> orgList = orgRepository.findAll(pageable);
        return orgList;
    }

}
