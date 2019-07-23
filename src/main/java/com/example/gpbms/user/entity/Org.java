package com.example.gpbms.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate  //这两个注解是为属性设置默认值时用到的
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Org {

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "custom-uuid4")
    @GenericGenerator(name = "custom-uuid4", strategy = "com.example.gpbms.util.Uuid4Generator")
    private String id;

    @Column(name = "org_name", unique = true)
    private String orgName;

    @Column(name = "org_code", unique = true)
    private String orgCode;

    /*@JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private User purchaseAdmin;*/
    @Column(name = "purchase_admin",columnDefinition = "varchar(32) default null")
    private String purchaseAdmin;

    /*@JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private User orgAdmin;*/
    @Column(name = "org_admin",columnDefinition = "varchar(32) default null")
    private String orgAdmin;


    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "org")
    private List<User> users;

}
