package com.claudroid.groceriesshop.validation;

import javax.validation.ConstraintValidator;

import com.claudroid.groceriesshop.model.dto.ProductDto;
import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.repository.ProductRepository;

import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class ProductNameValidator implements ConstraintValidator<ValidProductName, ProductDto> {

    private final ProductRepository productRepository;

    public ProductNameValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public void initialize(ValidProductName constraintAnnotation) {
    }

    @Override
    public boolean isValid(ProductDto productDto, ConstraintValidatorContext context) {
        Long id = productDto.getId();
        String name = productDto.getName();

        boolean isNotValid = false;

        if (id == null) {
            isNotValid = productRepository.existsByName(name);
        } else {
            Optional<ProductEntity> productEntityOpt = productRepository.findById(id);
            if (productEntityOpt.isPresent()) {
                ProductEntity productEntity = productEntityOpt.get();
                isNotValid = !name.equals(productEntity.getName()) && productRepository.existsByName(name);
            } else {
                isNotValid = productRepository.existsByName(name);
            }
        }

        if (isNotValid) {
            context.buildConstraintViolationWithTemplate("This product name is occupied.")
                    .addPropertyNode("name")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return !isNotValid;
    }
    }