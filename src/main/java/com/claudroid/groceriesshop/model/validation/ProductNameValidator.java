package com.claudroid.groceriesshop.model.validation;

import javax.validation.ConstraintValidator;

import com.claudroid.groceriesshop.model.dto.ProductDto;
import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.repository.ProductRepository;
import com.claudroid.groceriesshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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

        if (id == null) {
            return !productRepository.existsByName(name);
        }

        ProductEntity productEntity = productRepository.findById(id).orElse(null);
        if (productEntity == null) {
            return !productRepository.existsByName(name);
        }

        boolean notValid = !name.equals(productEntity.getName()) && productRepository.existsByName(name);

        if (notValid) {
            context.buildConstraintViolationWithTemplate("This product name is occupied.")
                    .addPropertyNode("name")
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return !notValid;
    }
}