package com.example.gpbms.budget.entity;

import com.example.gpbms.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BudgetAuditLog {

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "audit_info")
    private String auditInfo;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Budget budget;

    @ManyToOne(fetch = FetchType.LAZY)
    private User auditor;

    @Column(name = "create_time")
    private Timestamp createTime;
}
