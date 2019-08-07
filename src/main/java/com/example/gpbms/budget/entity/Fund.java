package com.example.gpbms.budget.entity;

import com.example.gpbms.user.entity.User;
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
public class Fund {

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "fund_code",unique = true)
    private String fundCode;

    @Column(name = "fund_name")
    private String fundName;

    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @Column(name = "fund_type")
    private String fundType;

    @Column(name = "total_amount")
    private Double totalAmount;   //总额度

    @Column(name = "budget_amount")
    private Double budgetAmount;  //预算冻结额度

    @Column(name = "purchase_amount")
    private Double purchaseAmount;//采购冻结额度

}
