package com.gauravi.productservice.controller;

import com.gauravi.productservice.dto.ProductRequest;
import com.gauravi.productservice.dto.ProductResponse;
import com.gauravi.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    // Create a new product
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        ProductResponse createdProduct = productService.createProduct(productRequest);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }
    
    // Get all products
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    // Get product by name
    @GetMapping("/name/{name}")
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable String name) {
        ProductResponse product = productService.getProductByName(name);
        return ResponseEntity.ok(product);
    }
    
    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, 
                                                       @Valid @RequestBody ProductRequest productRequest) {
        ProductResponse updatedProduct = productService.updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);
    }
    
    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    // Get products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category) {
        List<ProductResponse> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    // Get products by brand
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<ProductResponse>> getProductsByBrand(@PathVariable String brand) {
        List<ProductResponse> products = productService.getProductsByBrand(brand);
        return ResponseEntity.ok(products);
    }
    
    // Search products
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String q) {
        List<ProductResponse> products = productService.searchProducts(q);
        return ResponseEntity.ok(products);
    }
    
    // Get products by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        List<ProductResponse> products = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }
    
    // Get products with price less than or equal to max price
    @GetMapping("/price/max/{maxPrice}")
    public ResponseEntity<List<ProductResponse>> getProductsByMaxPrice(@PathVariable BigDecimal maxPrice) {
        List<ProductResponse> products = productService.getProductsByMaxPrice(maxPrice);
        return ResponseEntity.ok(products);
    }
    
    // Get products with price greater than or equal to min price
    @GetMapping("/price/min/{minPrice}")
    public ResponseEntity<List<ProductResponse>> getProductsByMinPrice(@PathVariable BigDecimal minPrice) {
        List<ProductResponse> products = productService.getProductsByMinPrice(minPrice);
        return ResponseEntity.ok(products);
    }
    
    // Get products with low stock
    @GetMapping("/stock/low/{quantity}")
    public ResponseEntity<List<ProductResponse>> getProductsWithLowStock(@PathVariable Integer quantity) {
        List<ProductResponse> products = productService.getProductsWithLowStock(quantity);
        return ResponseEntity.ok(products);
    }
    
    // Get products in stock
    @GetMapping("/stock/in-stock")
    public ResponseEntity<List<ProductResponse>> getProductsInStock() {
        List<ProductResponse> products = productService.getProductsInStock();
        return ResponseEntity.ok(products);
    }
    
    // Update stock quantity
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStockQuantity(@PathVariable Long id, 
                                                             @RequestParam Integer quantity) {
        ProductResponse updatedProduct = productService.updateStockQuantity(id, quantity);
        return ResponseEntity.ok(updatedProduct);
    }
    
    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Product Service is running!");
    }
} 