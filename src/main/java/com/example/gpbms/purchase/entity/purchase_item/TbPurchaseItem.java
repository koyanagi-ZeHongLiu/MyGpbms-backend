package com.example.gpbms.purchase.entity.purchase_item;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = TbPurchaseItem.TB_NAME)
public class TbPurchaseItem {
    private static final long serialVersionUID = 1L;

    public static final String TB_NAME = "tb_purchase_item";

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    // TODO isGeneric、isNonGeneric作用未知
    @Column(name = "is_generic", nullable = false)
    private Boolean isGeneric;

    @Column(name = "is_non_generic", nullable = false)
    private Boolean isNonGeneric;

    /** 金额起点 **/
    @Column(name = "moneyStart")
    private Double moneyStart;

    @Column(length = 2000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TbPurchaseItem parent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Set<TbPurchaseItem> children;

    public TbPurchaseItem() {
    }

    public TbPurchaseItem(Integer id) {
        super();
        this.id = id;
    }

    // TODO init方法作用
    public TbPurchaseItem init() {
        Assert.noNullElements(new Object[]{code, name});
        if(isGeneric == null) {
            setIsGeneric(false);
        }
        if(isNonGeneric == null) {
            setIsNonGeneric(false);
        }
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsGeneric() {
        return isGeneric;
    }

    public void setIsGeneric(Boolean isGeneric) {
        this.isGeneric = isGeneric;
    }

    public Boolean getIsNonGeneric() {
        return isNonGeneric;
    }

    public void setIsNonGeneric(Boolean isNonGeneric) {
        this.isNonGeneric = isNonGeneric;
    }

    public Double getMoneyStart() {
        return moneyStart;
    }

    public void setMoneyStart(Double moneyStart) {
        this.moneyStart = moneyStart;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TbPurchaseItem getParent() {
        return parent;
    }

    public void setParent(TbPurchaseItem parent) {
        this.parent = parent;
    }

    public Set<TbPurchaseItem> getChildren() {
        return children;
    }

    public void setChildren(Set<TbPurchaseItem> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "TbPurchaseItem [" + (id != null ? "id=" + id + ", " : "") + (code != null ? "code=" + code : "") + "]";
    }
}
