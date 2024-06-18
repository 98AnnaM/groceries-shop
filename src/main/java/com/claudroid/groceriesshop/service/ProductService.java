package com.claudroid.groceriesshop.service;

import com.claudroid.groceriesshop.exceptions.EntityNotFoundException;
import com.claudroid.groceriesshop.model.dto.ProductDto;
import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Optional;
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

    public ProductDto updateProduct(ProductDto productDto) {
        ProductEntity productEntity = findProductById(productDto.getId());
        productEntity.setName(productDto.getName());
        productEntity.setPrice(productDto.getPrice());

        return map(productRepository.save(productEntity));
    }

    public boolean productExistByName(String name) {
        return productRepository.existsByName(name);
    }

    public ProductEntity getByName(String name){
            Optional<ProductEntity> productEntity = productRepository.findByName(name);
            if(!productEntity.isPresent()){
                throw new EntityNotFoundException("Product with name = " + name + " not found");
            }
        return productEntity.get();
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

