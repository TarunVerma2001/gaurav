# Product Service

A Spring Boot microservice for managing products with CRUD operations using H2 in-memory database.

## Features

- **CRUD Operations**: Create, Read, Update, Delete products
- **Advanced Search**: Search products by name, description, category, brand
- **Price Filtering**: Filter products by price range, min/max price
- **Stock Management**: Track stock quantities and filter by stock levels
- **Validation**: Input validation with proper error handling
- **H2 Database**: In-memory database with console access
- **RESTful API**: Clean REST endpoints with proper HTTP status codes

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database**
- **Maven**
- **Spring Validation**

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Clone and Navigate

```bash
cd product-service
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on port **8081**.

### 4. Access H2 Console

Open your browser and navigate to: `http://localhost:8081/h2-console`

- **JDBC URL**: `jdbc:h2:mem:productdb`
- **Username**: `sa`
- **Password**: `password`

## API Endpoints

### Base URL: `http://localhost:8081/api/products`

### 1. Product Management

#### Create Product
```http
POST /api/products
Content-Type: application/json

{
  "name": "Product Name",
  "description": "Product description",
  "price": 99.99,
  "stockQuantity": 100,
  "category": "Electronics",
  "brand": "Brand Name"
}
```

#### Get All Products
```http
GET /api/products
```

#### Get Product by ID
```http
GET /api/products/{id}
```

#### Get Product by Name
```http
GET /api/products/name/{name}
```

#### Update Product
```http
PUT /api/products/{id}
Content-Type: application/json

{
  "name": "Updated Product Name",
  "description": "Updated description",
  "price": 149.99,
  "stockQuantity": 75,
  "category": "Electronics",
  "brand": "Brand Name"
}
```

#### Delete Product
```http
DELETE /api/products/{id}
```

### 2. Search and Filter

#### Search Products
```http
GET /api/products/search?q=search_term
```

#### Get Products by Category
```http
GET /api/products/category/{category}
```

#### Get Products by Brand
```http
GET /api/products/brand/{brand}
```

#### Get Products by Price Range
```http
GET /api/products/price-range?minPrice=50&maxPrice=200
```

#### Get Products by Max Price
```http
GET /api/products/price/max/{maxPrice}
```

#### Get Products by Min Price
```http
GET /api/products/price/min/{minPrice}
```

### 3. Stock Management

#### Get Products with Low Stock
```http
GET /api/products/stock/low/{quantity}
```

#### Get Products In Stock
```http
GET /api/products/stock/in-stock
```

#### Update Stock Quantity
```http
PATCH /api/products/{id}/stock?quantity=50
```

### 4. Health Check
```http
GET /api/products/health
```

## Sample Data

The application comes pre-loaded with 10 sample products across different categories:

- **Electronics**: iPhone 15 Pro, Samsung Galaxy S24, MacBook Pro 14, Sony WH-1000XM5, Wireless Gaming Mouse, Bluetooth Speaker
- **Footwear**: Nike Air Max 270, Adidas Ultraboost 22
- **Home & Kitchen**: Coffee Maker Pro
- **Sports & Fitness**: Yoga Mat Premium

## Response Format

### Success Response
```json
{
  "id": 1,
  "name": "iPhone 15 Pro",
  "description": "Latest iPhone with advanced camera system",
  "price": 999.99,
  "stockQuantity": 50,
  "category": "Electronics",
  "brand": "Apple",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Error Response
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Product with name 'iPhone 15 Pro' already exists"
}
```

## Validation Rules

- **Name**: Required, must be unique
- **Price**: Required, must be positive
- **Stock Quantity**: Required, must be positive
- **Description**: Optional
- **Category**: Optional
- **Brand**: Optional

## Testing the API

### Using curl

#### Create a Product
```bash
curl -X POST http://localhost:8081/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test Product",
    "description": "A test product",
    "price": 29.99,
    "stockQuantity": 10,
    "category": "Test",
    "brand": "Test Brand"
  }'
```

#### Get All Products
```bash
curl -X GET http://localhost:8081/api/products
```

#### Search Products
```bash
curl -X GET "http://localhost:8081/api/products/search?q=iPhone"
```

### Using Postman

Import the following collection or create requests manually using the endpoints listed above.

## Project Structure

```
product-service/
├── src/
│   ├── main/
│   │   ├── java/com/gauravi/productservice/
│   │   │   ├── ProductServiceApplication.java
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java
│   │   │   ├── service/
│   │   │   │   └── ProductService.java
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java
│   │   │   ├── model/
│   │   │   │   └── Product.java
│   │   │   ├── dto/
│   │   │   │   ├── ProductRequest.java
│   │   │   │   └── ProductResponse.java
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── config/
│   │   │       └── DataInitializer.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
└── pom.xml
```

## Configuration

The application configuration is in `src/main/resources/application.yml`:

- **Server Port**: 8081
- **Database**: H2 in-memory
- **H2 Console**: Enabled at `/h2-console`
- **JPA**: Hibernate with create-drop DDL

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the MIT License. 