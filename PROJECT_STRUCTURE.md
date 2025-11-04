# Project Structure

This document provides an overview of the project structure and organization.

## Directory Structure

```
business-management-api/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/example/businessapp/
│   │   │       ├── config/              # Configuration classes
│   │   │       │   ├── CorsConfig.kt
│   │   │       │   ├── OpenApiConfig.kt
│   │   │       │   └── SecurityConfig.kt
│   │   │       ├── controller/          # REST API controllers
│   │   │       │   ├── AuthController.kt
│   │   │       │   ├── InvoiceController.kt
│   │   │       │   ├── QuotationController.kt
│   │   │       │   ├── EwayBillController.kt
│   │   │       │   └── PaymentController.kt
│   │   │       ├── dto/                 # Data Transfer Objects
│   │   │       │   ├── ApiResponse.kt
│   │   │       │   ├── AuthRequest.kt
│   │   │       │   ├── AuthResponse.kt
│   │   │       │   ├── InvoiceDto.kt
│   │   │       │   ├── QuotationDto.kt
│   │   │       │   ├── EwayBillDto.kt
│   │   │       │   └── PaymentDto.kt
│   │   │       ├── entity/              # JPA entities
│   │   │       │   ├── User.kt
│   │   │       │   ├── Invoice.kt
│   │   │       │   ├── Quotation.kt
│   │   │       │   ├── EwayBill.kt
│   │   │       │   └── Payment.kt
│   │   │       ├── repository/          # Spring Data JPA repositories
│   │   │       │   ├── UserRepository.kt
│   │   │       │   ├── InvoiceRepository.kt
│   │   │       │   ├── QuotationRepository.kt
│   │   │       │   ├── EwayBillRepository.kt
│   │   │       │   └── PaymentRepository.kt
│   │   │       ├── service/             # Business logic layer
│   │   │       │   ├── AuthService.kt
│   │   │       │   ├── InvoiceService.kt
│   │   │       │   ├── QuotationService.kt
│   │   │       │   ├── EwayBillService.kt
│   │   │       │   └── PaymentService.kt
│   │   │       ├── security/            # Security components
│   │   │       │   ├── JwtTokenProvider.kt
│   │   │       │   ├── JwtAuthenticationFilter.kt
│   │   │       │   └── CustomUserDetailsService.kt
│   │   │       ├── exception/           # Exception handling
│   │   │       │   ├── Exceptions.kt
│   │   │       │   └── GlobalExceptionHandler.kt
│   │   │       └── BusinessApp.kt       # Application entry point
│   │   └── resources/
│   │       ├── application.yml          # Main configuration
│   │       └── db/migration/            # Flyway migrations
│   │           ├── V1__create_users_table.sql
│   │           ├── V2__create_invoices_table.sql
│   │           ├── V3__create_quotations_table.sql
│   │           ├── V4__create_eway_bills_table.sql
│   │           └── V5__create_payments_table.sql
│   └── test/                            # Test files (to be implemented)
├── build.gradle.kts                     # Gradle build configuration
├── settings.gradle.kts                  # Gradle settings
├── gradle.properties                    # Gradle properties
├── Dockerfile                           # Docker image configuration
├── docker-compose.yml                   # Docker Compose configuration
├── .dockerignore                        # Docker ignore file
├── .gitignore                           # Git ignore file
├── .env.example                         # Environment variables template
├── readme.md                            # Main documentation
├── QUICKSTART.md                        # Quick start guide
└── PROJECT_STRUCTURE.md                 # This file
```

## Module Overview

### 1. Configuration (`config/`)
- **SecurityConfig.kt**: Spring Security configuration with JWT authentication
- **CorsConfig.kt**: CORS configuration for cross-origin requests
- **OpenApiConfig.kt**: Swagger/OpenAPI documentation configuration

### 2. Controllers (`controller/`)
REST API endpoints organized by domain:
- **AuthController**: User registration, login, and authentication
- **InvoiceController**: Invoice CRUD operations
- **QuotationController**: Quotation CRUD operations
- **EwayBillController**: E-way bill CRUD operations
- **PaymentController**: Payment receipt CRUD operations

### 3. DTOs (`dto/`)
Request and response models for API communication:
- **ApiResponse**: Standardized API response wrapper
- **AuthRequest/AuthResponse**: Authentication DTOs
- **InvoiceDto**: Invoice request/response models
- **QuotationDto**: Quotation request/response models
- **EwayBillDto**: E-way bill request/response models
- **PaymentDto**: Payment request/response models

### 4. Entities (`entity/`)
JPA entities representing database tables:
- **User**: User accounts and authentication
- **Invoice**: Invoice records
- **Quotation**: Business quotations
- **EwayBill**: E-way bill records
- **Payment**: Payment receipts

### 5. Repositories (`repository/`)
Spring Data JPA repositories for database operations:
- Extends `JpaRepository` for standard CRUD operations
- Custom query methods for specific use cases
- Pagination support

### 6. Services (`service/`)
Business logic layer:
- Transaction management
- Data validation
- Business rules enforcement
- Entity-to-DTO mapping

### 7. Security (`security/`)
Security components:
- **JwtTokenProvider**: JWT token generation and validation
- **JwtAuthenticationFilter**: Request filter for JWT authentication
- **CustomUserDetailsService**: User details loading for authentication

### 8. Exception Handling (`exception/`)
- **Exceptions.kt**: Custom exception classes
- **GlobalExceptionHandler**: Centralized exception handling with `@ControllerAdvice`

### 9. Database Migrations (`db/migration/`)
Flyway migration scripts for database schema versioning:
- V1: Users table
- V2: Invoices table
- V3: Quotations table
- V4: E-way bills table
- V5: Payments table

## Architecture Patterns

### Clean Architecture
The project follows clean architecture principles with clear separation of concerns:
- **Presentation Layer**: Controllers
- **Business Logic Layer**: Services
- **Data Access Layer**: Repositories
- **Domain Layer**: Entities

### Dependency Flow
```
Controller → Service → Repository → Database
     ↓          ↓
    DTO      Entity
```

### Security Flow
```
Request → JwtAuthenticationFilter → SecurityFilterChain → Controller
```

## Key Design Decisions

1. **UUID for Primary Keys**: Using UUID instead of auto-increment for better distributed system support
2. **Immutable Data Classes**: Kotlin data classes for entities and DTOs
3. **Pagination**: All list endpoints support pagination using Spring Data's `Pageable`
4. **Soft Deletes**: Can be implemented by adding `deleted_at` column (currently hard deletes)
5. **Audit Fields**: `created_at` and `updated_at` timestamps on all entities
6. **Standardized Responses**: All API responses use `ApiResponse<T>` wrapper
7. **JWT Stateless Auth**: No session storage, fully stateless authentication

## Database Schema

### Relationships
- **User → Invoice**: One-to-Many (cascade delete)
- **User → Quotation**: One-to-Many (cascade delete)
- **User → EwayBill**: One-to-Many (cascade delete)
- **User → Payment**: One-to-Many (cascade delete)
- **Invoice → Payment**: One-to-Many (optional, set null on delete)

### Indexes
All tables have indexes on:
- Primary keys (UUID)
- Foreign keys (user_id)
- Frequently queried columns (status, dates)
- Unique constraints (email, bill_number)

## API Versioning

Current version: `v1`
- All endpoints prefixed with `/api/v1/`
- Future versions can be added without breaking existing clients

## Security Considerations

1. **Password Hashing**: BCrypt with default strength
2. **JWT Expiration**: 24 hours (configurable)
3. **CORS**: Configured for specific origins
4. **Input Validation**: Bean Validation on all DTOs
5. **SQL Injection**: Protected by JPA/Hibernate
6. **XSS**: JSON responses are automatically escaped

## Testing Strategy

### Unit Tests
- Service layer with mocked repositories
- Business logic validation

### Integration Tests
- Full application context
- Database integration with test containers

### Controller Tests
- MockMvc for endpoint testing
- Security context testing

## Deployment Architecture

```
┌─────────────────┐
│   Load Balancer │
└────────┬────────┘
         │
    ┌────┴────┐
    │   App   │ (Docker Container)
    └────┬────┘
         │
    ┌────┴────┐
    │PostgreSQL│ (Managed Database)
    └─────────┘
```

## Environment Configuration

### Development
- Local PostgreSQL
- SQL logging enabled
- Detailed error messages

### Production
- Managed PostgreSQL (RDS, Cloud SQL, etc.)
- SQL logging disabled
- Minimal error exposure
- HTTPS enforced
- Environment-based secrets

## Future Enhancements

1. **Caching**: Redis for frequently accessed data
2. **File Upload**: Support for invoice/receipt attachments
3. **Email Notifications**: Send invoice/payment notifications
4. **PDF Generation**: Generate PDF invoices
5. **Analytics**: Dashboard with business metrics
6. **Multi-tenancy**: Support for multiple organizations
7. **Audit Logging**: Track all changes for compliance
8. **Rate Limiting**: Prevent API abuse
9. **Webhooks**: Notify external systems of events
10. **GraphQL**: Alternative API interface

## Contributing

When adding new features:
1. Follow the existing package structure
2. Add Flyway migration for database changes
3. Implement all layers (entity, repository, service, controller)
4. Add validation on DTOs
5. Update Swagger annotations
6. Write tests
7. Update documentation

