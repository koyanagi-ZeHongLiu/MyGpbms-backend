package com.example.gpbms.budget.entity;


import com.example.gpbms.user.entity.Org;
import com.example.gpbms.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Budget {

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "budget_code",unique = true)
    private String budgetCode;

    @Column(name = "budget_name")
    private String budgetName;

    @ManyToOne(fetch = FetchType.EAGER)
    private Org org;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @OneToOne(fetch = FetchType.EAGER)
    private Fund fund;

    @Column(name = "budget_type")
    private String budgetType;   //年初预算还是追加预算

    @Column(name = "description")
    private String description;

    @Column(name = "budget_year")
    private String budgetYear;   //所属年份

    @OneToOne(fetch = FetchType.EAGER)
    private BudgetAuditStatus budgetAuditStatus; //运算单审核进度

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    private List<BudgetAuditLog> budgetAuditLogs;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "budget")
    private List<BudgetItems> budgetItems;

}
