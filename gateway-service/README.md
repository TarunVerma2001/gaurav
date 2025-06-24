# Gateway Service with Rate Limiting

A Spring Cloud Gateway service that provides rate limiting for the Product Service API using Redis for memory management.

## Features

- **Rate Limiting**: IP-based rate limiting with different limits for each API endpoint
- **Redis Integration**: Uses Redis for storing rate limit counters
- **Circuit Breaker**: Resilience4j circuit breaker for fault tolerance
- **Fallback Responses**: Graceful degradation when services are unavailable
- **IP Detection**: Supports proxy headers (X-Forwarded-For, X-Real-IP)

## Technology Stack

- **Java 17**
- **Spring Cloud Gateway 2023.0.0**
- **Spring Boot 3.2.0**
- **Redis** (for rate limiting storage)
- **Resilience4j** (circuit breaker)

## Rate Limiting Configuration

### API Endpoint Rate Limits

| Endpoint | Method | Replenish Rate | Burst Capacity | Description |
|----------|--------|----------------|----------------|-------------|
| `/api/products/health` | GET | 10/min | 20 | Health check |
| `/api/products` | GET | 30/min | 60 | Get all products |
| `/api/products/{id}` | GET | 50/min | 100 | Get product by ID |
| `/api/products/name/{name}` | GET | 25/min | 50 | Get product by name |
| `/api/products` | POST | 5/min | 10 | Create product |
| `/api/products/{id}` | PUT | 10/min | 20 | Update product |
| `/api/products/{id}` | DELETE | 3/min | 5 | Delete product |
| `/api/products/search` | GET | 20/min | 40 | Search products |
| `/api/products/category/{category}` | GET | 40/min | 80 | Filter by category |
| `/api/products/brand/{brand}` | GET | 40/min | 80 | Filter by brand |
| `/api/products/price-range` | GET | 25/min | 50 | Price range filter |
| `/api/products/price/max/{maxPrice}` | GET | 25/min | 50 | Max price filter |
| `/api/products/price/min/{minPrice}` | GET | 25/min | 50 | Min price filter |
| `/api/products/stock/**` | GET | 15/min | 30 | Stock management |
| `/api/products/{id}/stock` | PATCH | 5/min | 10 | Update stock |

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- Redis server running on localhost:6379

## Getting Started

### 1. Start Redis

```bash
# Using Docker
docker run -d -p 6379:6379 redis:7-alpine

# Or using Homebrew (macOS)
brew install redis
brew services start redis

# Or using apt (Ubuntu)
sudo apt-get install redis-server
sudo systemctl start redis
```

### 2. Build and Run Gateway Service

```bash
cd gateway-service
mvn clean install
mvn spring-boot:run
```

The gateway will start on port **8080**.

### 3. Ensure Product Service is Running

Make sure your Product Service is running on port **8081**.

## Usage

### Accessing APIs through Gateway

Instead of calling the Product Service directly on port 8081, now call through the gateway on port 8080:

**Before (Direct to Product Service):**
```bash
curl http://localhost:8081/api/products
```

**After (Through Gateway):**
```bash
curl http://localhost:8080/api/products
```

### Rate Limit Headers

The gateway adds rate limit headers to responses:

- `X-RateLimit-Limit`: Rate limit information
- `X-RateLimit-Remaining`: Remaining requests
- `X-RateLimit-Reset`: Reset time (for exceeded limits)
- `Retry-After`: Seconds to wait (for exceeded limits)

### Rate Limit Exceeded Response

When rate limit is exceeded, you'll get:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": "TOO_MANY_REQUESTS",
  "message": "Rate limit exceeded. Please try again later.",
  "error": "RATE_LIMIT_EXCEEDED"
}
```

## Testing Rate Limiting

### Test Rate Limits

```bash
# Test health endpoint (10 requests per minute)
for i in {1..15}; do
  curl -i http://localhost:8080/api/products/health
  echo "Request $i"
done

# Test create product (5 requests per minute)
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/products \
    -H "Content-Type: application/json" \
    -d '{"name":"Test Product","price":29.99,"stockQuantity":10}'
  echo "Create request $i"
done
```

### Monitor Rate Limits in Redis

```bash
# Connect to Redis CLI
redis-cli

# Check rate limit keys
KEYS *request_rate_limiter*

# Get specific rate limit info
GET request_rate_limiter:127.0.0.1:health-check
```

## Circuit Breaker Configuration

The gateway includes circuit breakers for fault tolerance:

- **Failure Rate Threshold**: 50%
- **Wait Duration**: 5 seconds
- **Ring Buffer Size**: 10
- **Fallback**: Returns service unavailable response

## Fallback Endpoints

When services are unavailable, the gateway provides fallback responses:

- `/fallback/health` - Health check fallback
- `/fallback/product` - Single product fallback
- `/fallback/products` - Multiple products fallback

## Monitoring

### Actuator Endpoints

- `http://localhost:8080/actuator/health` - Gateway health
- `http://localhost:8080/actuator/gateway` - Gateway routes
- `http://localhost:8080/actuator/circuitbreakers` - Circuit breaker status

### Redis Monitoring

Monitor Redis memory usage and rate limit keys:

```bash
# Redis info
redis-cli info memory

# Monitor Redis commands
redis-cli monitor
```

## Configuration

### Customizing Rate Limits

Edit `application.yml` to modify rate limits:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: custom-route
          uri: http://localhost:8081
          predicates:
            - Path=/api/products/custom
          filters:
            - name: RequestRateLimiter
              args:
                redis-rate-limiter.replenishRate: 100  # requests per minute
                redis-rate-limiter.burstCapacity: 200  # burst capacity
                key-resolver: "#{@ipKeyResolver}"
```

### Redis Configuration

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
```

## Troubleshooting

### Common Issues

1. **Redis Connection Failed**
   - Ensure Redis is running on localhost:6379
   - Check Redis logs: `redis-cli ping`

2. **Rate Limiting Not Working**
   - Check Redis keys: `redis-cli KEYS *request_rate_limiter*`
   - Verify IP detection in logs

3. **Circuit Breaker Issues**
   - Check actuator endpoint: `/actuator/circuitbreakers`
   - Verify fallback responses

### Logs

Enable debug logging:

```yaml
logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    org.springframework.data.redis: DEBUG
    com.gauravi.gatewayservice: DEBUG
```

## Architecture

```
Client → Gateway (8080) → Product Service (8081)
                ↓
            Redis (6379)
```

- **Gateway**: Handles routing, rate limiting, and circuit breaking
- **Redis**: Stores rate limit counters per IP address
- **Product Service**: Backend service with business logic

## Security Considerations

- Rate limiting is based on IP address
- Supports proxy headers for real IP detection
- Circuit breakers prevent cascading failures
- Fallback responses don't expose sensitive information 