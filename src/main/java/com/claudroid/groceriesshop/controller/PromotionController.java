package com.claudroid.groceriesshop.controller;

import com.claudroid.groceriesshop.exceptions.InvalidInputExistsException;
import com.claudroid.groceriesshop.model.dto.PromotionDto;
import com.claudroid.groceriesshop.model.enums.PromotionNameEnum;
import com.claudroid.groceriesshop.service.ProductService;
import com.claudroid.groceriesshop.service.PromotionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;
    private final ProductService productService;

    public PromotionController(PromotionService promotionService, ProductService productService) {
        this.promotionService = promotionService;
        this.productService = productService;
    }

    @GetMapping("/2for3")
    public ResponseEntity<PromotionDto> getTwoForThree() {
        return ResponseEntity.ok(promotionService.getPromotion(PromotionNameEnum.TWO_FOR_THREE));
    }

    @GetMapping("/buy1get1half")
    public ResponseEntity<PromotionDto> buyOneGetOneHalf() {
        return ResponseEntity.ok(promotionService.getPromotion(PromotionNameEnum.BUY_ONE_GET_ONE_HALF));
    }

    @PostMapping("/2for3")
    public ResponseEntity<PromotionDto> createPromotionTwoForThree (@RequestBody @Valid PromotionDto promotionDto) {
        int distinctCount = (int) promotionDto.getProducts().stream().distinct().count();
        if (promotionDto.getProducts().size() != 3 || distinctCount != 3){
            throw new InvalidInputExistsException("Three different product must be added");
        }
        validateProductsExist(promotionDto);
        validationProductsAreNotIncludedInAnotherPromotion(promotionDto, PromotionNameEnum.BUY_ONE_GET_ONE_HALF);

        promotionService.setPromotion(PromotionNameEnum.TWO_FOR_THREE, promotionDto);
        return ResponseEntity.ok(promotionDto);
    }

    @PostMapping("/buy1get1half")
    public ResponseEntity<PromotionDto> createPromotionBuyOneGetOneHalf (@RequestBody @Valid PromotionDto promotionDto) {
        if (promotionDto.getProducts().size() != 1){
            throw new InvalidInputExistsException("One product must be added");
        }
        validateProductsExist(promotionDto);
        validationProductsAreNotIncludedInAnotherPromotion(promotionDto, PromotionNameEnum.TWO_FOR_THREE);

        promotionService.setPromotion(PromotionNameEnum.BUY_ONE_GET_ONE_HALF, promotionDto);
        return ResponseEntity.ok(promotionDto);
    }

    private void validateProductsExist(PromotionDto promotionDto){
        List<String> notExistingProducts = new ArrayList<>();
        for(String productName : promotionDto.getProducts()){
            if (!productService.productExistByName(productName)){
                notExistingProducts.add(productName);
            }
        }
        if (!notExistingProducts.isEmpty()){
            throw new InvalidInputExistsException("Products: " + String.join(", ", notExistingProducts) + " does not exist");
        }
    }

    private void validationProductsAreNotIncludedInAnotherPromotion(PromotionDto promotionDto, PromotionNameEnum promotionName) {
        List<String> productsInAnotherPromotion = new ArrayList<>();
        for(String productName : promotionDto.getProducts()){
            if (promotionService.productIsInPromotion(promotionName, productName)){
                productsInAnotherPromotion.add(productName);
            }
        }
        if (!productsInAnotherPromotion.isEmpty()){
            throw new InvalidInputExistsException(
                    "Products: " + String.join(", ", productsInAnotherPromotion) + " are included in another promotion.");
        }
    }
}
