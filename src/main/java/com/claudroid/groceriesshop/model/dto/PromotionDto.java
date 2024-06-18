package com.claudroid.groceriesshop.model.dto;

import java.util.List;

public class PromotionDto {

    private List<String> products;

    public List<String> getProducts() {
        return products;
    }

    public PromotionDto setProducts(List<String> products) {
        this.products = products;
        return this;
    }
}
