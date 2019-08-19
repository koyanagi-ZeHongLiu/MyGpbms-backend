package com.example.gpbms.purchase.entity;

import com.example.gpbms.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PurchaseAuditLog {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "audit_info")
    private String auditInfo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Purchaes purchaes;

    @ManyToOne(fetch = FetchType.EAGER)
    private User auditor;

    @Column(name = "create_time")
    private Timestamp createTime;
}
