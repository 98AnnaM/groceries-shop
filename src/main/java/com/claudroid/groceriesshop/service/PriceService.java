package com.claudroid.groceriesshop.service;

import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.model.enums.PromotionNameEnum;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceService {

    private final ProductService productService;
    private final PromotionService promotionService;

    public PriceService(ProductService productService, PromotionService promotionService) {
        this.productService = productService;
        this.promotionService = promotionService;
    }

    public  Double getTotalPrice(List<String> productNames) {
        Double priceWithoutPromotion = productNames.stream()
                .map(productService::getByName)
                .map(ProductEntity::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();

        Double endPrice = priceWithoutPromotion - applyPromotionBuyOneGetOneHalf(productNames) - applyTwoForThree(productNames);
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(endPrice));
    }

    private Double applyPromotionBuyOneGetOneHalf(List<String> productNames){
        List<String> productsOnPromotion = productNames.stream()
                .filter(productName ->
                        promotionService.productIsInPromotion(PromotionNameEnum.BUY_ONE_GET_ONE_HALF, productName))
                .collect(Collectors.toList());

        double decreasePrice = 0;
        if (productsOnPromotion.size() >= 2){
            decreasePrice = productService.getByName(productsOnPromotion.get(0)).getPrice()/2;
        }
        return decreasePrice;
    }

    private Double applyTwoForThree(List<String> productNames){
        List<ProductEntity> productsOnPromotion = productNames.stream()
                .filter(productName ->
                        promotionService.productIsInPromotion(PromotionNameEnum.TWO_FOR_THREE, productName))
                .map(productService::getByName)
                .collect(Collectors.toList());

        double decreasePrice = 0;
        if (productsOnPromotion.size() >= 3){
           decreasePrice = Math.min(productsOnPromotion.get(0).getPrice(),
                   Math.min(productsOnPromotion.get(1).getPrice(), productsOnPromotion.get(2).getPrice()));
        }
        return decreasePrice;
    }

}
