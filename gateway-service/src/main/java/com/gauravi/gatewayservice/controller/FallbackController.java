package com.gauravi.gatewayservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/health")
    public Mono<ResponseEntity<Map<String, Object>>> healthFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Health check service is temporarily unavailable");
        response.put("fallback", true);
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }

    @GetMapping("/product")
    public Mono<ResponseEntity<Map<String, Object>>> productFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Product service is temporarily unavailable");
        response.put("fallback", true);
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }

    @GetMapping("/products")
    public Mono<ResponseEntity<Map<String, Object>>> productsFallback() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "SERVICE_UNAVAILABLE");
        response.put("message", "Products service is temporarily unavailable");
        response.put("fallback", true);
        response.put("data", new Object[0]);
        
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response));
    }

    @GetMapping("/rate-limit-exceeded")
    public Mono<ResponseEntity<Map<String, Object>>> rateLimitExceeded() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "TOO_MANY_REQUESTS");
        response.put("message", "Rate limit exceeded. Please try again later.");
        response.put("error", "RATE_LIMIT_EXCEEDED");
        
        return Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response));
    }
} 