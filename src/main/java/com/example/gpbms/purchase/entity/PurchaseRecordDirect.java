package com.example.gpbms.purchase.entity;

import com.example.gpbms.provider.entity.Provider;
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
/**
 * 直接购买备案
 */
public class PurchaseRecordDirect {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "record_code")
    private String recordCode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Purchaes purchaes;

    @Column(name = "trading_money")
    private Double tradingMoney;    //成交金额

    @Column(name = "trading_time")
    private String tradingTime;     //成交时间

    @ManyToOne(fetch = FetchType.EAGER)
    private Provider provider;      //供应商
}
