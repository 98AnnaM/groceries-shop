package com.claudroid.groceriesshop.init;

import com.claudroid.groceriesshop.model.entity.ProductEntity;
import com.claudroid.groceriesshop.repository.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInit implements ApplicationRunner {

    private final ProductRepository productRepository;

    public DatabaseInit(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Initialization logic here
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAfterStartup() {
        if (productRepository.count() == 0) {
            productRepository.save(new ProductEntity().setName("apple").setPrice(20.0));
            productRepository.save(new ProductEntity().setName("banana").setPrice(30.0));
            productRepository.save(new ProductEntity().setName("lemon").setPrice(40.0));
            productRepository.save(new ProductEntity().setName("orange").setPrice(50.0));
            productRepository.save(new ProductEntity().setName("tomato").setPrice(60.0));
        }
    }
}