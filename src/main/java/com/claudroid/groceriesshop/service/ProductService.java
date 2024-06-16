package com.claudroid.groceriesshop.service;

import com.claudroid.groceriesshop.exceptions.EntityNotFoundException;
import com.claudroid.groceriesshop.exceptions.ProductNameExistsException;
import com.claudroid.groceriesshop.model.dto.ProductDto;
import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts(){
        return productRepository.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public ProductDto createProduct(ProductDto productDto) {
        ProductEntity productEntity = new ProductEntity().setName(productDto.getName()).setPrice(productDto.getPrice());
        return map(productRepository.save(productEntity));
    }

    public void deleteProduct(Long id) {
        productRepository.delete(findProductById(id));
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        ProductEntity productEntity = findProductById(id);
        productEntity.setName(productDto.getName());
        productEntity.setPrice(productDto.getPrice());

        return map(productRepository.save(productEntity));
    }

    public Double getTotalPrice(List<String> productNames) {
        return productNames.stream()
                .map(productRepository::findByName)
                .map(ProductEntity::getPrice)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private ProductDto map(ProductEntity productEntity){
        return new ProductDto()
                .setName(productEntity.getName())
                .setPrice(productEntity.getPrice())
                .setId(productEntity.getId());
    }

    private ProductEntity findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product with id = " + id + " not found"));
    }
    }

