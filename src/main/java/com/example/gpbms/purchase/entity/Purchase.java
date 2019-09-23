package com.example.gpbms.purchase.entity;

import com.example.gpbms.budget.entity.Budget;
import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Purchase {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "purchase_code",unique = true)
    private String purchaseCode;

    @Column(name = "purchase_name")
    private String purchaseName;

    @ManyToOne(fetch = FetchType.EAGER)
    private Org org;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @Column(name = "purchase_type")
    private String purchaseType;

    @Column(name = "project_type")
    private String projectType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Budget budget;

    @Column(name = "bidding_price")
    private Double biddingPrice;  //招标控制价

    @Column(name = "build_area")
    private Double buildArea;     //建筑面积

    @Column(name = "days")
    private String days;          //工期

    @Column(name = "contract_form")
    private String contractForm;  //合同形式

    @Column(name = "kvalue_down")
    private String kvalueDown;        //K值下调

    @Column(name = "payment_cycle")
    private String paymentCycle;  //付款周期

    @Column(name = "bidder_quality")
    private String bidderQuality; //投标人资质

    @Column(name = "else_need")
        private String elseNeed;      //其他要求

    @Column(name = "purchase_year")
    private String purchaseYear;  //所属年度

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    private PurchaseAuditStatus purchaseAuditStatus;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchase")
    private List<PurchaseItems> purchaseItems;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchase")
    private  List<PurchaseAuditLog> purchaseAuditLogs;

    @Column(name = "create_time")
    private Timestamp createTime;

}
