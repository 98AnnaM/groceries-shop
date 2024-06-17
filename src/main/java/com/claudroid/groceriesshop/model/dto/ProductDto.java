package com.claudroid.groceriesshop.model.dto;

import com.claudroid.groceriesshop.validation.ValidProductName;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@ValidProductName
public class ProductDto {

    private Long id;
    @NotBlank(message = "Name cannot be blank.")
    private String name;
    @Positive(message = "Price must be positive.")
    private double price;

    public double getPrice() {
        return price;
    }

    public ProductDto setPrice(double price) {
        this.price = price;
        return this;
    }

    public String getName() {
        return name;
    }

    public ProductDto setName(String name) {
        this.name = name;
        return this;
    }

    public Long getId() {
        return id;
    }

    public ProductDto setId(Long id) {
        this.id = id;
        return this;
    }
}
