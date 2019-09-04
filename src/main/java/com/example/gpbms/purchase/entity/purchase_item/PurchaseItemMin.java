package com.example.gpbms.purchase.entity.purchase_item;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseItemMin {
    /** 格式：品目编码（不包含父节点编码）- 品目名称： **/
    private String n;

    /** 品目子节点 **/
    private List<PurchaseItemMin> c;
}

