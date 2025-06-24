package com.gauravi.productservice.config;

import com.gauravi.productservice.model.Product;
import com.gauravi.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        productRepository.deleteAll();
        
        // Create sample products
        Product product1 = new Product(
            "iPhone 15 Pro",
            "Latest iPhone with advanced camera system and A17 Pro chip",
            new BigDecimal("999.99"),
            50,
            "Electronics",
            "Apple"
        );
        
        Product product2 = new Product(
            "Samsung Galaxy S24",
            "Premium Android smartphone with AI features",
            new BigDecimal("899.99"),
            35,
            "Electronics",
            "Samsung"
        );
        
        Product product3 = new Product(
            "MacBook Pro 14",
            "Professional laptop with M3 chip for developers and creators",
            new BigDecimal("1999.99"),
            25,
            "Electronics",
            "Apple"
        );
        
        Product product4 = new Product(
            "Nike Air Max 270",
            "Comfortable running shoes with Air Max technology",
            new BigDecimal("129.99"),
            100,
            "Footwear",
            "Nike"
        );
        
        Product product5 = new Product(
            "Adidas Ultraboost 22",
            "High-performance running shoes with Boost midsole",
            new BigDecimal("179.99"),
            75,
            "Footwear",
            "Adidas"
        );
        
        Product product6 = new Product(
            "Sony WH-1000XM5",
            "Premium noise-cancelling wireless headphones",
            new BigDecimal("349.99"),
            30,
            "Electronics",
            "Sony"
        );
        
        Product product7 = new Product(
            "Coffee Maker Pro",
            "Automatic coffee maker with programmable timer",
            new BigDecimal("89.99"),
            45,
            "Home & Kitchen",
            "Breville"
        );
        
        Product product8 = new Product(
            "Wireless Gaming Mouse",
            "High-precision gaming mouse with RGB lighting",
            new BigDecimal("79.99"),
            60,
            "Electronics",
            "Logitech"
        );
        
        Product product9 = new Product(
            "Yoga Mat Premium",
            "Non-slip yoga mat made from eco-friendly materials",
            new BigDecimal("49.99"),
            80,
            "Sports & Fitness",
            "Lululemon"
        );
        
        Product product10 = new Product(
            "Bluetooth Speaker",
            "Portable waterproof speaker with 20-hour battery life",
            new BigDecimal("129.99"),
            40,
            "Electronics",
            "JBL"
        );
        
        // Save all products
        productRepository.saveAll(Arrays.asList(
            product1, product2, product3, product4, product5,
            product6, product7, product8, product9, product10
        ));
        
        System.out.println("Sample products loaded successfully!");
    }
} 