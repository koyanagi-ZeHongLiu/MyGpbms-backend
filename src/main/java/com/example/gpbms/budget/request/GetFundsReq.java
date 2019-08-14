package com.example.gpbms.budget.request;

import com.example.gpbms.budget.entity.Fund;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.util.PageUtils;
import lombok.Data;

@Data
public class GetFundsReq {
    private Fund fund;
    private User owner;
    private PageUtils pageUtils;
}
