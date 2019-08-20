package com.example.gpbms.purchase.request;

import com.example.gpbms.purchase.entity.Purchase;
import com.example.gpbms.user.entity.User;
import com.example.gpbms.util.PageUtils;
import lombok.Data;

@Data
public class GetPurchasesReq {
    private Purchase purchase;
    private User user;
    private PageUtils pageUtils;
}
