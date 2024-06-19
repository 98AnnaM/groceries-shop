package com.claudroid.groceriesshop.controller;

import com.claudroid.groceriesshop.model.dto.ProductDto;
import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.repository.ProductRepository;
import com.claudroid.groceriesshop.service.ProductService;
import com.claudroid.groceriesshop.service.PromotionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private ProductRepository productRepository;

    private ProductDto testProduct;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        testProduct = new ProductDto();
        testProduct.setName("testProduct");
        testProduct.setPrice(10.0);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        productService.createProduct(testProduct);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("testProduct")))
                .andExpect(jsonPath("$[0].price", is(10.0)));
    }

    @Test
    public void testCreateProductSuccess() throws Exception {
        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("testProduct")))
                .andExpect(jsonPath("$.price", is(10.0)));

        Assertions.assertEquals(1, productRepository.count());
    }

    @Test
    public void testCreateProductDuplicateNameFail() throws Exception {
        productService.createProduct(testProduct);

        mockMvc.perform(post("/products/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.fieldWithErrors[0]", is("This product name is occupied.")));

        Assertions.assertEquals(1, productRepository.count());
    }

    @Test
    public void testUpdateProductSuccess() throws Exception {
        ProductDto createdProduct = productService.createProduct(testProduct);
        createdProduct.setName("updatedProduct");
        createdProduct.setPrice(20.0);

        mockMvc.perform(put("/products/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updatedProduct")))
                .andExpect(jsonPath("$.price", is(20.0)));
    }

    @Test
    public void testUpdateProductDuplicateNameFail() throws Exception {
        ProductDto createdProduct = productService.createProduct(testProduct);
        productService.createProduct(testProduct.setName("testProduct2"));

        mockMvc.perform(put("/products/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdProduct.setName("testProduct2"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.fieldWithErrors[0]", is("This product name is occupied.")));
    }

    @Test
    public void testDeleteProductSuccess() throws Exception {
        ProductDto createdProduct = productService.createProduct(testProduct);

        mockMvc.perform(delete("/products/delete/" + createdProduct.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, productRepository.count());
    }



}