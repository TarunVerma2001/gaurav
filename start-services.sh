#!/bin/bash

echo "🚀 Starting Product Service with Rate Limiting..."

# Check if Docker is installed
if ! command -v docker &> /dev/null; then
    echo "❌ Error: Docker is not installed or not in PATH"
    echo "Please install Docker first: https://docs.docker.com/get-docker/"
    exit 1
fi

# Check if Docker Compose is installed
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Error: Docker Compose is not installed or not in PATH"
    echo "Please install Docker Compose first: https://docs.docker.com/compose/install/"
    exit 1
fi

# Build the services
echo "📦 Building services..."
cd product-service && mvn clean package -DskipTests && cd ..
cd gateway-service && mvn clean package -DskipTests && cd ..

# Start Redis first
echo "🔴 Starting Redis..."
docker-compose up -d redis

# Wait for Redis to be healthy
echo "⏳ Waiting for Redis to be ready..."
until docker-compose exec -T redis redis-cli ping > /dev/null 2>&1; do
    echo "Waiting for Redis..."
    sleep 2
done
echo "✅ Redis is ready!"

# Start Product Service
echo "🛍️ Starting Product Service..."
docker-compose up -d product-service

# Wait for Product Service to be ready
echo "⏳ Waiting for Product Service to be ready..."
until curl -s http://localhost:8081/api/products/health > /dev/null 2>&1; do
    echo "Waiting for Product Service..."
    sleep 3
done
echo "✅ Product Service is ready!"

# Start Gateway Service
echo "🚪 Starting Gateway Service..."
docker-compose up -d gateway-service

# Wait for Gateway Service to be ready
echo "⏳ Waiting for Gateway Service to be ready..."
until curl -s http://localhost:8080/actuator/health > /dev/null 2>&1; do
    echo "Waiting for Gateway Service..."
    sleep 3
done
echo "✅ Gateway Service is ready!"

echo ""
echo "🎉 All services are running!"
echo ""
echo "📋 Service URLs:"
echo "   Gateway Service: http://localhost:8080"
echo "   Product Service: http://localhost:8081"
echo "   Redis: localhost:6379"
echo ""
echo "🔗 Test the API through Gateway:"
echo "   curl http://localhost:8080/api/products/health"
echo ""
echo "📊 Monitor services:"
echo "   Gateway Health: http://localhost:8080/actuator/health"
echo "   Product Health: http://localhost:8081/api/products/health"
echo ""
echo "🛑 To stop all services:"
echo "   docker-compose down"
echo ""
echo "📝 To view logs:"
echo "   docker-compose logs -f" 