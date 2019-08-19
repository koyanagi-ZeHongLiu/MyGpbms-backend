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
 * 委托采购备案
 */
public class PurchaseRecordEntrust {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "record_code")
    private String recordCode;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Purchase purchase;

    @ManyToOne(fetch = FetchType.EAGER)
    private Provider provider;      //委托代理机构

    @Column(name = "year")
    private String year;            //所属年度

    @Column(name = "file_number")
    private String fileNumber;      //档案编号

    @Column(name = "contract_number")
    private String contractNumber;  //合同编号

    @Column(name = "notice_time")
    private String noticeTime;      //公告时间

    @Column(name = "bid_time")
    private String bidTime;         //开标时间

    @Column(name = "contract_package")
    private String contractPackage; //合同包

    @Column(name = "bid_money")
    private Double bidMoney;        //中标金额

    @Column(name = "bid_provider")
    private String bidProvider;     //中标供应商

    @Column(name = "contract_time")
    private String contractTime;    //合同签订时间

    @Column(name = "contract_money")
    private Double contractMoney;   //合同金额

    @Column(name = "add_time")
    private String addTime;         //补充协议时间（默认无）

    @Column(name = "add_money")
    private Double addMoney;        //补充协议金额（默认无，可负值）

    @Column(name = "check_time")
    private String checkTime;       //验收时间

}
