package com.claudroid.groceriesshop.repository;

import com.claudroid.groceriesshop.model.entity.PromotionEntity;
import com.claudroid.groceriesshop.model.entity.PromotionEntity;
import com.claudroid.groceriesshop.model.enums.PromotionNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<PromotionEntity, Long> {

    List<PromotionEntity> findAllByPromotionName(PromotionNameEnum promotionName);
}
