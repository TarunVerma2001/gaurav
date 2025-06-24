package com.gauravi.productservice.service;

import com.gauravi.productservice.dto.ProductRequest;
import com.gauravi.productservice.dto.ProductResponse;
import com.gauravi.productservice.model.Product;
import com.gauravi.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    // Create a new product
    public ProductResponse createProduct(ProductRequest productRequest) {
        // Check if product with same name already exists
        if (productRepository.existsByNameIgnoreCase(productRequest.getName())) {
            throw new RuntimeException("Product with name '" + productRequest.getName() + "' already exists");
        }
        
        Product product = new Product(
            productRequest.getName(),
            productRequest.getDescription(),
            productRequest.getPrice(),
            productRequest.getStockQuantity(),
            productRequest.getCategory(),
            productRequest.getBrand()
        );
        
        Product savedProduct = productRepository.save(product);
        return convertToResponse(savedProduct);
    }
    
    // Get all products
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Get product by ID
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToResponse(product);
    }
    
    // Get product by name
    public ProductResponse getProductByName(String name) {
        Product product = productRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new RuntimeException("Product not found with name: " + name));
        return convertToResponse(product);
    }
    
    // Update product
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Check if new name conflicts with existing product (excluding current product)
        if (!existingProduct.getName().equalsIgnoreCase(productRequest.getName()) &&
            productRepository.existsByNameIgnoreCase(productRequest.getName())) {
            throw new RuntimeException("Product with name '" + productRequest.getName() + "' already exists");
        }
        
        // Update fields
        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setPrice(productRequest.getPrice());
        existingProduct.setStockQuantity(productRequest.getStockQuantity());
        existingProduct.setCategory(productRequest.getCategory());
        existingProduct.setBrand(productRequest.getBrand());
        
        Product updatedProduct = productRepository.save(existingProduct);
        return convertToResponse(updatedProduct);
    }
    
    // Delete product
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    
    // Get products by category
    public List<ProductResponse> getProductsByCategory(String category) {
        List<Product> products = productRepository.findByCategoryIgnoreCase(category);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Get products by brand
    public List<ProductResponse> getProductsByBrand(String brand) {
        List<Product> products = productRepository.findByBrandIgnoreCase(brand);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Search products by name or description
    public List<ProductResponse> searchProducts(String searchTerm) {
        List<Product> products = productRepository.searchProducts(searchTerm);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Get products by price range
    public List<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Get products with price less than or equal to given price
    public List<ProductResponse> getProductsByMaxPrice(BigDecimal maxPrice) {
        List<Product> products = productRepository.findByPriceLessThanEqual(maxPrice);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Get products with price greater than or equal to given price
    public List<ProductResponse> getProductsByMinPrice(BigDecimal minPrice) {
        List<Product> products = productRepository.findByPriceGreaterThanEqual(minPrice);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Get products with low stock (less than given quantity)
    public List<ProductResponse> getProductsWithLowStock(Integer quantity) {
        List<Product> products = productRepository.findByStockQuantityLessThan(quantity);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Get products in stock (stock quantity greater than 0)
    public List<ProductResponse> getProductsInStock() {
        List<Product> products = productRepository.findByStockQuantityGreaterThan(0);
        return products.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    // Update stock quantity
    public ProductResponse updateStockQuantity(Long id, Integer newQuantity) {
        if (newQuantity < 0) {
            throw new RuntimeException("Stock quantity cannot be negative");
        }
        
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        product.setStockQuantity(newQuantity);
        Product updatedProduct = productRepository.save(product);
        return convertToResponse(updatedProduct);
    }
    
    // Helper method to convert Product entity to ProductResponse DTO
    private ProductResponse convertToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getCategory(),
            product.getBrand(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
} 