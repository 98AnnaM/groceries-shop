package com.claudroid.groceriesshop.controller;

import com.claudroid.groceriesshop.exceptions.InvalidInputExistsException;
import com.claudroid.groceriesshop.model.dto.ProductDto;
import com.claudroid.groceriesshop.service.PromotionService;
import com.claudroid.groceriesshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PromotionService promotionService;

    public ProductController(ProductService productService, PromotionService promotionService) {
        this.productService = productService;
        this.promotionService = promotionService;
    }

    @GetMapping()
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/add")
    public ResponseEntity<ProductDto> createProduct (@RequestBody @Valid ProductDto productDto) {
        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.ok(createdProduct);
    }

    @PutMapping("/edit")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto){
        ProductDto updatedProduct = productService.updateProduct(productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        if (promotionService.productIsInAnyPromotion(id)){
            throw new InvalidInputExistsException("Product can not be deleted, because it is in promotion");
        }
        productService.deleteProduct(id);
    }
}
