package com.claudroid.groceriesshop.service;

import com.claudroid.groceriesshop.model.dto.PromotionDto;
import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.model.entity.PromotionEntity;
import com.claudroid.groceriesshop.model.enums.PromotionNameEnum;
import com.claudroid.groceriesshop.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductService productService;

    public PromotionService(PromotionRepository promotionRepository, ProductService productService) {
        this.promotionRepository = promotionRepository;
        this.productService = productService;
    }

    public PromotionDto getPromotion(PromotionNameEnum promotionName) {
        List<String> productNames = promotionRepository
                .findAllByPromotionName(promotionName)
                .stream()
                .flatMap(promotion -> promotion.getProducts().stream())
                .map(ProductEntity::getName)
                .collect(Collectors.toList());

        return new PromotionDto().setProducts(productNames);
    }

    @Transactional
    public void setPromotion(PromotionNameEnum promotionName, PromotionDto promotionDto) {
        promotionRepository.deleteAllByPromotionName(promotionName);

        List<ProductEntity> products = promotionDto.getProducts().stream()
                .map(productService::getByName)
                .collect(Collectors.toList());

        PromotionEntity promotionEntity = new PromotionEntity().setPromotionName(promotionName);
        promotionEntity.setProducts(products);

        promotionRepository.save(promotionEntity);
    }

    public boolean productIsInPromotion(PromotionNameEnum promotionName, String productName) {
        Long productId = productService.getByName(productName).getId();
        return promotionRepository.existsByPromotionNameAndProduct_id(promotionName, productId);
    }

    public boolean productIsInAnyPromotion(Long productId) {
        return promotionRepository.existsByProduct_id(productId);
    }
}
