# Quick Start Guide

This guide will help you get the Business Management API up and running in minutes.

## Prerequisites

- Docker and Docker Compose installed
- Git installed

## Steps

### 1. Clone the Repository

```bash
git clone <repository-url>
cd business-management-api
```

### 2. Start the Application with Docker Compose

```bash
docker-compose up -d
```

This will:
- Start a PostgreSQL database
- Build and start the Spring Boot application
- Run Flyway migrations automatically

### 3. Verify the Application is Running

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### 4. Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui.html
```

### 5. Register a User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### 6. Login and Get JWT Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

Copy the `token` from the response.

### 7. Create an Invoice

```bash
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "clientName": "ABC Corp",
    "amount": 1500.00,
    "dateIssued": "2024-01-15",
    "dueDate": "2024-02-15",
    "status": "PENDING"
  }'
```

### 8. List All Invoices

```bash
curl -X GET "http://localhost:8080/api/v1/invoices?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Stopping the Application

```bash
docker-compose down
```

To remove all data:
```bash
docker-compose down -v
```

## Next Steps

- Explore the API using Swagger UI
- Read the full [README.md](README.md) for detailed documentation
- Check out the [API Endpoints](#) section for all available endpoints

## Troubleshooting

### Port Already in Use

If port 8080 or 5432 is already in use, modify the `docker-compose.yml` file:

```yaml
services:
  app:
    ports:
      - "8081:8080"  # Change 8081 to any available port
```

### Database Connection Issues

Check if PostgreSQL is running:
```bash
docker-compose logs postgres
```

### Application Logs

View application logs:
```bash
docker-compose logs -f app
```

## Development Mode

To run in development mode without Docker:

1. Start PostgreSQL:
```bash
docker run --name postgres -e POSTGRES_DB=businessdb -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15-alpine
```

2. Run the application:
```bash
./gradlew bootRun
```

## Support

For issues or questions, please open an issue in the repository.

