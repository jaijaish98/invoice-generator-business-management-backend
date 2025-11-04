# Quick Start Guide

This guide will help you get the Business Management API up and running in minutes.

## Prerequisites

- **Java 17 or later** (Java 21 recommended)
- **PostgreSQL 15+** installed and running
- **Git** installed
- **Gradle** (included via Gradle Wrapper)

## Steps

### 1. Clone the Repository

```bash
git clone https://github.com/jaijaish98/invoice-generator-business-management-backend.git
cd invoice-generator-business-management-backend
```

### 2. Set Up PostgreSQL Database

#### Option A: Using Homebrew (macOS)

```bash
# Install PostgreSQL
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15

# Create database
createdb businessdb
```

#### Option B: Using PostgreSQL CLI

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE businessdb;

# Exit
\q
```

#### Option C: Using Docker (if you prefer)

```bash
docker run --name postgres-businessdb \
  -e POSTGRES_DB=businessdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15-alpine
```

### 3. Configure Environment Variables

Create a `.env` file or set environment variables:

```bash
export DB_USER=your_postgres_username
export DB_PASSWORD=your_postgres_password
```

Or use the default values in `application.yml` (postgres/postgres).

### 4. Build the Application

```bash
./gradlew clean build
```

This will:
- Download all dependencies
- Compile the Kotlin code
- Run tests
- Create executable JAR files

### 5. Run the Application

#### Option A: Using Gradle

```bash
DB_USER=your_username DB_PASSWORD=your_password ./gradlew bootRun
```

#### Option B: Using the JAR file

```bash
DB_USER=your_username DB_PASSWORD=your_password \
java -jar build/libs/business-management-api-0.0.1-SNAPSHOT.jar
```

The application will:
- Start on port **8080**
- Connect to PostgreSQL database
- Run Flyway migrations automatically
- Initialize all 5 modules (Auth, Invoices, Quotations, E-Way Bills, Payments)

### 6. Verify the Application is Running

```bash
curl http://localhost:8080/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

### 7. Access Swagger UI

Open your browser and navigate to:
```
http://localhost:8080/swagger-ui/index.html
```

Or access the API documentation:
```
http://localhost:8080/v3/api-docs
```

### 8. Register a User

**Required Fields:**
- `fullName` - User's full name (2-255 characters)
- `email` - Valid email address (unique)
- `countryCode` - Country code in format +1 to +9999 (e.g., +91, +1, +44)
- `mobileNumber` - Mobile number (6-15 digits, unique)
- `password` - Password (minimum 6 characters)

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Test User",
    "email": "test@example.com",
    "countryCode": "+91",
    "mobileNumber": "9876543210",
    "password": "Test@1234"
  }'
```

**⚠️ Common Mistakes:**
- ❌ Using `name` instead of `fullName`
- ❌ Missing `+` in country code (must be `+91`, not `91`)
- ❌ Mobile number less than 6 digits or more than 15 digits
- ❌ Mobile number contains non-numeric characters
- ❌ Password too short (less than 6 characters)
- ❌ Missing any required field

**Validation Rules:**
- Email must be unique across all users
- Mobile number must be unique across all users
- Email and mobile number combination is validated
- Country code must start with `+` followed by 1-4 digits
- Mobile number must be 6-15 numeric digits only

Expected response:
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzM4NCJ9...",
    "user": {
      "id": "bba945b2-efc5-449a-9ed0-6c2d7ef7b755",
      "name": "Test User",
      "email": "test@example.com",
      "countryCode": "+91",
      "mobileNumber": "9876543210",
      "role": "ROLE_USER",
      "createdAt": "2025-11-04T20:08:07.593873"
    }
  },
  "message": "User registered successfully",
  "timestamp": "2025-11-04T20:08:07.655693"
}
```

**Error Responses:**

If email already exists:
```json
{
  "success": false,
  "data": null,
  "message": "An account with this email already exists",
  "timestamp": "2025-11-04T20:07:59.868285"
}
```

If mobile number already exists:
```json
{
  "success": false,
  "data": null,
  "message": "An account with this mobile number already exists",
  "timestamp": "2025-11-04T20:08:16.048233"
}
```

If validation fails:
```json
{
  "success": false,
  "data": {
    "mobileNumber": "Mobile number must be 6-15 digits",
    "countryCode": "Country code must be in format +1 to +9999"
  },
  "message": "Validation failed",
  "timestamp": "2025-11-04T20:08:23.537014"
}
```

### 9. Login and Get JWT Token

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test@1234"
  }'
```

Copy the `token` from the response for subsequent API calls.

### 10. Change Password (Optional)

```bash
curl -X PUT http://localhost:8080/api/v1/auth/change-password \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "currentPassword": "Test@1234",
    "newPassword": "NewPass@1234",
    "confirmPassword": "NewPass@1234"
  }'
```

**Validation Rules:**
- Current password must be correct
- New password must be different from current password
- New password and confirm password must match
- New password must be at least 6 characters

### 11. Create an Invoice

```bash
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "clientName": "ABC Corp",
    "amount": 1500.00,
    "dateIssued": "2025-11-04",
    "dueDate": "2025-12-31",
    "status": "PENDING"
  }'
```

### 12. List All Invoices

```bash
curl -X GET "http://localhost:8080/api/v1/invoices?page=0&size=10" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Stopping the Application

Press `Ctrl+C` in the terminal where the application is running.

Or if running as a background process:
```bash
# Find the process
ps aux | grep business-management-api

# Kill the process
kill <PID>
```

## Available API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login user
- `GET /api/v1/auth/me` - Get current user information
- `PUT /api/v1/auth/change-password` - Change password

### Invoices
- `GET /api/v1/invoices` - List all invoices (paginated)
- `POST /api/v1/invoices` - Create invoice
- `GET /api/v1/invoices/{id}` - Get invoice by ID
- `PUT /api/v1/invoices/{id}` - Update invoice
- `DELETE /api/v1/invoices/{id}` - Delete invoice

### Quotations
- `GET /api/v1/quotations` - List all quotations
- `POST /api/v1/quotations` - Create quotation
- `GET /api/v1/quotations/{id}` - Get quotation by ID
- `PUT /api/v1/quotations/{id}` - Update quotation
- `DELETE /api/v1/quotations/{id}` - Delete quotation

### E-Way Bills
- `GET /api/v1/eway-bills` - List all e-way bills
- `POST /api/v1/eway-bills` - Create e-way bill
- `GET /api/v1/eway-bills/{id}` - Get e-way bill by ID
- `PUT /api/v1/eway-bills/{id}` - Update e-way bill
- `DELETE /api/v1/eway-bills/{id}` - Delete e-way bill

### Payments
- `GET /api/v1/payments` - List all payments
- `POST /api/v1/payments` - Record payment
- `GET /api/v1/payments/{id}` - Get payment by ID
- `PUT /api/v1/payments/{id}` - Update payment
- `DELETE /api/v1/payments/{id}` - Delete payment

## Next Steps

- ✅ Explore all endpoints using **Swagger UI**
- ✅ Read the full [README.md](readme.md) for detailed documentation
- ✅ Check [API_EXAMPLES.md](API_EXAMPLES.md) for more API usage examples
- ✅ Review [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) to understand the codebase
- ✅ See [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for deployment instructions

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, change it in `application.yml`:

```yaml
server:
  port: 8081  # Change to any available port
```

### Database Connection Issues

**Check if PostgreSQL is running:**
```bash
# macOS (Homebrew)
brew services list | grep postgresql

# Linux
sudo systemctl status postgresql

# Check connection
psql -U your_username -d businessdb -c "SELECT version();"
```

**Common fixes:**
- Verify database name is `businessdb`
- Check username and password are correct
- Ensure PostgreSQL is listening on port 5432
- Check `application.yml` database configuration

### Build Failures

**Clean and rebuild:**
```bash
./gradlew clean build --refresh-dependencies
```

**Check Java version:**
```bash
java -version  # Should be 17 or later
```

### Flyway Migration Errors

**Reset database (WARNING: This deletes all data):**
```bash
psql -U your_username -d businessdb -c "DROP SCHEMA public CASCADE; CREATE SCHEMA public;"
```

Then restart the application to rerun migrations.

### Application Logs

View detailed logs by setting log level in `application.yml`:

```yaml
logging:
  level:
    com.example.businessapp: DEBUG
    org.springframework.security: DEBUG
```

## Configuration

### Database Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/businessdb
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:postgres}
```

### JWT Configuration

Edit JWT secret in `application.yml`:

```yaml
jwt:
  secret: ${JWT_SECRET:your-256-bit-secret-key-here-change-in-production}
  expiration: 86400000  # 24 hours in milliseconds
```

### CORS Configuration

Edit allowed origins in `src/main/kotlin/com/example/businessapp/config/CorsConfig.kt`

## Production Deployment

For production deployment, see [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) for:
- Render deployment
- Railway deployment
- AWS ECS deployment
- Environment variable configuration
- Security best practices

## Support

For issues or questions:
- Open an issue on [GitHub](https://github.com/jaijaish98/invoice-generator-business-management-backend/issues)
- Check existing documentation files
- Review Swagger UI for API details

