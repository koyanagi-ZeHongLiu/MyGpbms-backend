package com.example.gpbms.user.request;

import com.example.gpbms.user.entity.User;
import lombok.Data;

@Data
public class GetOperatorReq {

    private User operator;
    private int[] permission;
}
