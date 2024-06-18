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

    void deleteAllByPromotionName(PromotionNameEnum promotionName);

     boolean existsByPromotionNameAndProduct_id(PromotionNameEnum promotionName, Long product_id);

     boolean existsByProduct_id(Long product_id);
}
