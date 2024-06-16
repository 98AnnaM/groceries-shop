package com.claudroid.groceriesshop.controller;

import com.claudroid.groceriesshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PriceController {

    private final ProductService productService;

    public PriceController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/price")
    public Double getTotalPrice(@RequestBody List<String> productNames) {
        return productService.getTotalPrice(productNames);
    }
}
