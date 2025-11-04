# Business Management & Invoice Generator API

A comprehensive Spring Boot backend application built with Kotlin for managing business operations including invoices, quotations, e-way bills, and payment receipts. This API serves both Next.js web frontend and Android Jetpack Compose mobile applications.

## 🚀 Features

- **User Authentication & Authorization**
  - JWT-based stateless authentication
  - Role-based access control (RBAC)
  - Secure password hashing with BCrypt

- **Invoice Management**
  - Create, read, update, and delete invoices
  - Track invoice status (PENDING, PAID, OVERDUE, CANCELLED)
  - Pagination support for listing invoices

- **Quotation Management**
  - Manage business quotations
  - Track quotation status (DRAFT, SENT, ACCEPTED, REJECTED, EXPIRED)
  - Link quotations to clients

- **E-Way Bill Management**
  - Generate and manage e-way bills
  - Track transport details and validity
  - Support for multiple transport modes

- **Payment Receipt Management**
  - Record payment receipts
  - Link payments to invoices
  - Support multiple payment methods

## 🛠️ Technology Stack

- **Framework:** Spring Boot 3.2.0
- **Language:** Kotlin 1.9.20
- **JVM:** Java 17
- **Build Tool:** Gradle with Kotlin DSL
- **Database:** PostgreSQL 15
- **Migration:** Flyway
- **Security:** Spring Security with JWT
- **Documentation:** Swagger/OpenAPI 3.0
- **Containerization:** Docker & Docker Compose

## 📋 Prerequisites

- JDK 17 or later
- PostgreSQL 15 or later
- Docker & Docker Compose (optional, for containerized deployment)
- Gradle 8.x (wrapper included)

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd business-management-api
```

### 2. Configure Environment Variables

Create a `.env` file in the root directory:

```bash
cp .env.example .env
```

Edit `.env` with your configuration:

```env
DB_URL=jdbc:postgresql://localhost:5432/businessdb
DB_USER=postgres
DB_PASSWORD=your_password
JWT_SECRET=your-secret-key-minimum-256-bits
SPRING_PROFILE=dev
```

### 3. Setup PostgreSQL Database

**Option A: Using Docker**

```bash
docker run --name businessapp-postgres \
  -e POSTGRES_DB=businessdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15-alpine
```

**Option B: Local PostgreSQL**

```sql
CREATE DATABASE businessdb;
```

### 4. Build the Application

```bash
./gradlew clean build
```

### 5. Run the Application

**Development Mode:**

```bash
./gradlew bootRun
```

**Production Mode:**

```bash
java -jar build/libs/business-management-api-0.0.1-SNAPSHOT.jar
```

The API will be available at `http://localhost:8080`

## 🐳 Docker Deployment

### Using Docker Compose (Recommended)

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down

# Stop and remove volumes
docker-compose down -v
```

### Manual Docker Build

```bash
# Build the image
docker build -t business-management-api .

# Run the container
docker run -p 8080:8080 \
  -e SPRING_PROFILE=prod \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/businessdb \
  -e DB_USER=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_SECRET=your-secret-key \
  business-management-api
```

## 📚 API Documentation

### Swagger UI

Access interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification

JSON format:
```
http://localhost:8080/v3/api-docs
```

YAML format:
```
http://localhost:8080/v3/api-docs.yaml
```



## 🔐 Authentication

### Register a New User

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

Response:
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": "uuid",
      "name": "John Doe",
      "email": "john@example.com",
      "role": "ROLE_USER"
    }
  },
  "message": "Login successful"
}
```

### Using JWT Token

Include the token in the Authorization header for protected endpoints:

```bash
curl -X GET http://localhost:8080/api/v1/invoices \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📡 API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - Login user
- `GET /api/v1/auth/me` - Get current user info

### Invoices
- `POST /api/v1/invoices` - Create invoice
- `GET /api/v1/invoices` - List all invoices (paginated)
- `GET /api/v1/invoices/{id}` - Get invoice by ID
- `PUT /api/v1/invoices/{id}` - Update invoice
- `DELETE /api/v1/invoices/{id}` - Delete invoice

### Quotations
- `POST /api/v1/quotations` - Create quotation
- `GET /api/v1/quotations` - List all quotations (paginated)
- `GET /api/v1/quotations/{id}` - Get quotation by ID
- `PUT /api/v1/quotations/{id}` - Update quotation
- `DELETE /api/v1/quotations/{id}` - Delete quotation

### E-Way Bills
- `POST /api/v1/eway-bills` - Create e-way bill
- `GET /api/v1/eway-bills` - List all e-way bills (paginated)
- `GET /api/v1/eway-bills/{id}` - Get e-way bill by ID
- `PUT /api/v1/eway-bills/{id}` - Update e-way bill
- `DELETE /api/v1/eway-bills/{id}` - Delete e-way bill

### Payments
- `POST /api/v1/payments` - Create payment receipt
- `GET /api/v1/payments` - List all payments (paginated)
- `GET /api/v1/payments/{id}` - Get payment by ID
- `PUT /api/v1/payments/{id}` - Update payment
- `DELETE /api/v1/payments/{id}` - Delete payment

### Health Check
- `GET /actuator/health` - Application health status

## 🗄️ Database Schema

The application uses Flyway for database migrations. Migration scripts are located in `src/main/resources/db/migration/`.

### Tables

1. **users** - User accounts and authentication
2. **invoices** - Invoice records
3. **quotations** - Business quotations
4. **eway_bills** - E-way bill records
5. **payments** - Payment receipts

## 🚀 Deployment

### Deploy to Render

1. Create a new Web Service on Render
2. Connect your GitHub repository
3. Configure build command: `./gradlew clean build -x test`
4. Configure start command: `java -jar build/libs/*.jar`
5. Add environment variables:
   - `DB_URL`
   - `DB_USER`
   - `DB_PASSWORD`
   - `JWT_SECRET`
   - `SPRING_PROFILE=prod`

### Deploy to Railway

1. Create a new project on Railway
2. Add PostgreSQL database service
3. Add your application service
4. Configure environment variables
5. Deploy from GitHub

### Deploy to AWS ECS

1. Build and push Docker image to ECR
2. Create ECS task definition
3. Configure RDS PostgreSQL instance
4. Create ECS service
5. Configure load balancer and security groups

## 🧪 Testing

### Run All Tests

```bash
./gradlew test
```

### Run with Coverage

```bash
./gradlew test jacocoTestReport
```

Coverage report will be available at `build/reports/jacoco/test/html/index.html`

## 📝 Configuration Profiles

### Development Profile (`dev`)
- SQL logging enabled
- Detailed error messages
- H2 console enabled (if configured)

### Production Profile (`prod`)
- SQL logging disabled
- Minimal error exposure
- Optimized for performance

Switch profiles using:
```bash
export SPRING_PROFILE=prod
```

Or in application properties:
```yaml
spring:
  profiles:
    active: prod
```

## 🔒 Security Best Practices

1. **JWT Secret**: Use a strong, randomly generated secret (minimum 256 bits)
2. **Database Credentials**: Never commit credentials to version control
3. **HTTPS**: Always use HTTPS in production
4. **CORS**: Configure allowed origins appropriately
5. **Rate Limiting**: Consider implementing rate limiting for production
6. **Input Validation**: All inputs are validated using Bean Validation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For support, email support@businessapp.com or open an issue in the repository.

## 🙏 Acknowledgments

- Spring Boot Team
- Kotlin Community
- PostgreSQL Team
- All contributors
