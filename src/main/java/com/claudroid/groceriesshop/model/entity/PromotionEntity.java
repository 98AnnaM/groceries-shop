package com.claudroid.groceriesshop.model.entity;

import com.claudroid.groceriesshop.model.enums.PromotionNameEnum;

import javax.persistence.*;
import java.util.List;

@Entity
public class PromotionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromotionNameEnum promotionName;

    @OneToMany
    private List<ProductEntity> product;

    public Long getId() {
        return id;
    }

    public PromotionEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public PromotionNameEnum getPromotionName() {
        return promotionName;
    }

    public PromotionEntity setPromotionName(PromotionNameEnum promotionName) {
        this.promotionName = promotionName;
        return this;
    }

    public List<ProductEntity> getProducts() {
        return product;
    }

    public PromotionEntity setProducts(List<ProductEntity> product) {
        this.product = product;
        return this;
    }
}
