package com.claudroid.groceriesshop.service;

import com.claudroid.groceriesshop.model.dto.TwoForThreeDto;
import com.claudroid.groceriesshop.model.entity.PromotionEntity;
import com.claudroid.groceriesshop.model.enums.PromotionNameEnum;
import com.claudroid.groceriesshop.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    public TwoForThreeDto getTwoForThreePromotion() {
        promotionRepository
                .findAllByPromotionName(PromotionNameEnum.TWOFORTHREE)
                .stream();
    }


}
