package com.example.gpbms.budget.controller;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.budget.repository.BudgetRepository;
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
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    @Transactional
    @PostMapping(value = "saveBudget")
    public RespBean saveBudget(@RequestBody Budget budget){
        //在前端设置好budgetStatus，是保存还是提交
        return RespBean.success("保存预算单成功", budgetRepository.save(budget));
    }

    @Transactional
    @PostMapping(value = "deleteBudget")
    public RespBean deleteBudget(@RequestBody Budget budget){
        //删除前删除审核日志和品目
        budgetRepository.delete(budget);
        return RespBean.success("删除预算单成功");
    }

    @PostMapping(value = "getBudget")
    public RespBean getBudget(@RequestBody Budget budget){
        return RespBean.success("加载预算单成功", budgetRepository.findById(budget.getId()).orElse(null));
    }

    @PostMapping(value = "getBudgets")
    public RespBean getBudgets(@RequestBody PageUtils pageUtils){
        Pageable pageable = PageRequest.of(pageUtils.getCurrentPage(), pageUtils.getPageSize());
        Page<Budget> budgetList = budgetRepository.findAll(pageable);
        return RespBean.success("加载经费单成功",budgetList);
    }
}
