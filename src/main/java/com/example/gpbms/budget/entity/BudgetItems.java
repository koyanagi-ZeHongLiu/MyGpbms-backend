package com.example.gpbms.budget.entity;

import com.example.gpbms.purchase.entity.PurchaseCatalogItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BudgetItems {

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Budget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    private PurchaseCatalogItem purchaseCatalogItem;

    @Column(name = "item_count")
    private Integer itemCount;     //品目总数

    @Column(name = "item_total_price")
    private Double itemTotalPrice; //总价

    @Column(name = "item_unit")
    private String itemUnit;       //计量单位

    @Column(name = "label")
    private String label;
}
