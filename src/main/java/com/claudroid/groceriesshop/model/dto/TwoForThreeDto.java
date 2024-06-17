package com.claudroid.groceriesshop.model.dto;

import java.util.List;

public class TwoForThreeDto {

    private List<String> productNames;

    public List<String> getProductNames() {
        return productNames;
    }

    public TwoForThreeDto setProductNames(List<String> productNames) {
        this.productNames = productNames;
        return this;
    }
}
