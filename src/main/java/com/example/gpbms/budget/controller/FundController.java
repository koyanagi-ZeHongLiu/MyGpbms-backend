package com.example.gpbms.budget.controller;

import com.example.gpbms.budget.entity.Fund;
import com.example.gpbms.budget.repository.FundRepository;
import com.example.gpbms.budget.request.GetFundsReq;
import com.example.gpbms.user.entity.User;
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

@RestController
@RequestMapping("api")
public class FundController {

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @PostMapping(value = "saveFund")
    public RespBean saveFund(@RequestBody Fund fund){
        fund.setOwner(userRepository.findByRealName(fund.getOwner().getRealName()).get());
        fund.setBudgetAmount(0.0);
        fund.setPurchaseAmount(0.0);
        return RespBean.success("保存经费单成功",fundRepository.save(fund));
    }

    @Transactional
    @PostMapping(value = "editFund")
    public RespBean editFund(@RequestBody Fund fund){
        fund.setOwner(userRepository.findByRealName(fund.getOwner().getRealName()).get());
        return RespBean.success("修改经费单成功",fundRepository.save(fund));
    }

    @Transactional
    @PostMapping(value = "deleteFund")
    public RespBean deleteFund(@RequestBody Fund fund){
        fundRepository.delete(fund);
        return RespBean.success("删除经费单成功");
    }

    @PostMapping(value = "getFund")
    public RespBean getFund(@RequestBody Fund fund){
        return RespBean.success("加载经费单成功",fundRepository.findById(fund.getId()).orElse(null));
    }

    @PostMapping(value = "getFunds")
    public RespBean getFunds(@RequestBody GetFundsReq fundsReq){
        Pageable pageable = PageRequest.of(fundsReq.getPageUtils().getCurrentPage(), fundsReq.getPageUtils().getPageSize());
        Page<Fund> fundList = fundRepository.findAll(pageable);
        return RespBean.success("加载经费单成功",fundList);
    }
    // TODO 用包装类接收前端传来的分页信息和用户RealName
    @PostMapping(value = "getFundsByRealName")
    public RespBean getFundsByRealname(@RequestBody GetFundsReq fundsReq) {
        User owner = userRepository.findByRealName(fundsReq.getOwner().getRealName()).orElse(null);
        Pageable pageable = PageRequest.of(fundsReq.getPageUtils().getCurrentPage(), fundsReq.getPageUtils().getPageSize());
        Page<Fund> fundList = fundRepository.findByOwner(pageable, owner);
        return RespBean.success("加载成功",fundList);
    }
}
