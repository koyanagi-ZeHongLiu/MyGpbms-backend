package com.example.gpbms.purchase.entity;

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
public class PurchaseItems {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    private PurchaseCatalogItem purchaseCatalogItem;

    @Column(name = "item_count")
    private Integer itemCount;     //品目总数

    @Column(name = "item_total_price")
    private Double itemTotalPrice; //总价

    @Column(name = "item_unit")
    private String itemUnit;       //计量单位

    @Column(name = "is_import")
    private Integer isImport;      //是否进口，0代表进口，1代表不是进口

    @Column(name = "label")
    private String label;
}
