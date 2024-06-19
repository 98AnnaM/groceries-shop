package com.claudroid.groceriesshop.service;

import com.claudroid.groceriesshop.exceptions.EntityNotFoundException;
import com.claudroid.groceriesshop.model.dto.ProductDto;
import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductEntity productEntity;
    private ProductDto productDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        productEntity = new ProductEntity()
                .setId(1L)
                .setName("testProduct")
                .setPrice(10.0);

        productDto = new ProductDto()
                .setId(1L)
                .setName("testProduct")
                .setPrice(10.0);
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(productEntity));

        List<ProductDto> products = productService.getAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(productDto.getName(), products.get(0).getName());
    }

    @Test
    public void testCreateProduct() {
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        ProductDto createdProduct = productService.createProduct(productDto);

        assertNotNull(createdProduct);
        assertEquals(productDto.getName(), createdProduct.getName());
        assertEquals(productDto.getPrice(), createdProduct.getPrice());
    }

    @Test
    public void testDeleteProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(productEntity);
    }

    @Test
    public void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        verify(productRepository, never()).delete(any());
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(productEntity));
        when(productRepository.save(any(ProductEntity.class))).thenReturn(productEntity);

        ProductDto updatedProduct = productService.updateProduct(productDto);

        assertNotNull(updatedProduct);
        assertEquals(productDto.getName(), updatedProduct.getName());
        assertEquals(productDto.getPrice(), updatedProduct.getPrice());
    }

    @Test
    public void testUpdateProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(productDto);
        });

        verify(productRepository, never()).save(any());
    }

    @Test
    public void testProductExistByName() {
        when(productRepository.existsByName("Test Product")).thenReturn(true);

        boolean exists = productService.productExistByName("Test Product");

        assertTrue(exists);
    }

    @Test
    public void testGetByNameSuccess() {
        when(productRepository.findByName("Test Product")).thenReturn(Optional.of(productEntity));

        ProductEntity product = productService.getByName("Test Product");

        assertNotNull(product);
        assertEquals(productEntity.getName(), product.getName());
    }

    @Test
    public void testGetByNameNotFound() {
        when(productRepository.findByName("Nonexistent Product")).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.getByName("Nonexistent Product");
        });

        String expectedMessage = "Product with name = Nonexistent Product not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

