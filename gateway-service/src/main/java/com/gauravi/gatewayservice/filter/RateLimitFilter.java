package com.gauravi.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .doFinally(signalType -> {
                    ServerHttpResponse response = exchange.getResponse();
                    
                    // Add rate limit headers to all responses
                    if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                        response.getHeaders().add("X-RateLimit-Limit", "Rate limit exceeded");
                        response.getHeaders().add("X-RateLimit-Remaining", "0");
                        response.getHeaders().add("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() + 60000));
                        response.getHeaders().add("Retry-After", "60");
                    } else {
                        // Add rate limit info headers for successful requests
                        response.getHeaders().add("X-RateLimit-Limit", "Rate limit info");
                        response.getHeaders().add("X-RateLimit-Remaining", "Available");
                    }
                });
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
} 