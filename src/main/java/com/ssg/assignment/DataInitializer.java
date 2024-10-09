package com.ssg.assignment;

import com.ssg.assignment.entity.Product;
import com.ssg.assignment.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;


    @Override
    public void run(String... args) throws Exception {
        productRepository.save(new Product(0, 0.0f));
        productRepository.save(new Product(0, 0.0f));
        productRepository.save(new Product(0, 0.0f));
        productRepository.save(new Product(0, 0.0f));
        productRepository.save(new Product(0, 0.0f));
    }
}
