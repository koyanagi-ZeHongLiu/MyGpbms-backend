package com.example.gpbms.purchase.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PurchaseAuditStatus {
    @Id
    @Column(length = 11)
    private Integer id;

    @Column(name = "status_name")
    private String statusName;
}
