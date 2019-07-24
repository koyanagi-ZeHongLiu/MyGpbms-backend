package com.example.gpbms.purchase.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class PurchaseCatalogItem {

    @Id
    @Column(length = 32)
    private String id;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description")
    private String description;

    @Column(name = "price_limit")
    private String priceLimit;
}
