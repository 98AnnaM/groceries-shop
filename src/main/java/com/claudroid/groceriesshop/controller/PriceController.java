package com.claudroid.groceriesshop.controller;

import com.claudroid.groceriesshop.service.PriceService;
import com.claudroid.groceriesshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PriceController {

    private final ProductService productService;
    private final PriceService priceService;

    public PriceController(ProductService productService, PriceService priceService) {
        this.productService = productService;
        this.priceService = priceService;
    }

    @PostMapping("/price")
    public Double getTotalPrice(@RequestBody List<String> productNames) {
        return priceService.getTotalPrice(productNames);
    }
}
