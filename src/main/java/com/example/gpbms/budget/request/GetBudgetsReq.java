package com.example.gpbms.budget.request;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.util.PageUtils;
import lombok.Data;

@Data
public class GetBudgetsReq {
    private Budget budget;
    private User user;
    private PageUtils pageUtils;
}
