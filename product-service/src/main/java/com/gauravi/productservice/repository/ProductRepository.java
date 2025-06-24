package com.gauravi.productservice.repository;

import com.gauravi.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find by name (case-insensitive)
    Optional<Product> findByNameIgnoreCase(String name);
    
    // Find by category
    List<Product> findByCategoryIgnoreCase(String category);
    
    // Find by brand
    List<Product> findByBrandIgnoreCase(String brand);
    
    // Find products with price less than or equal to given price
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    
    // Find products with price greater than or equal to given price
    List<Product> findByPriceGreaterThanEqual(BigDecimal price);
    
    // Find products with price between min and max
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // Find products with stock quantity less than given value (out of stock or low stock)
    List<Product> findByStockQuantityLessThan(Integer quantity);
    
    // Find products with stock quantity greater than 0 (in stock)
    List<Product> findByStockQuantityGreaterThan(Integer quantity);
    
    // Custom query to search products by name or description
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> searchProducts(@Param("searchTerm") String searchTerm);
    
    // Check if product exists by name
    boolean existsByNameIgnoreCase(String name);
    
    // Count products by category
    long countByCategoryIgnoreCase(String category);
    
    // Count products by brand
    long countByBrandIgnoreCase(String brand);
} 