# API Examples

This document provides practical examples for using the Business Management API.

## Base URL

```
Development: http://localhost:8080
Production: https://your-domain.com
```

## Authentication Examples

### 1. Register a New User

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "John Doe",
      "email": "john.doe@example.com",
      "role": "ROLE_USER",
      "createdAt": "2024-01-15T10:30:00"
    }
  },
  "message": "User registered successfully",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 2. Login

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "SecurePass123"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "name": "John Doe",
      "email": "john.doe@example.com",
      "role": "ROLE_USER",
      "createdAt": "2024-01-15T10:30:00"
    }
  },
  "message": "Login successful",
  "timestamp": "2024-01-15T10:35:00"
}
```

### 3. Get Current User

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/auth/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "role": "ROLE_USER",
    "createdAt": "2024-01-15T10:30:00"
  },
  "message": "User information retrieved successfully",
  "timestamp": "2024-01-15T10:40:00"
}
```

## Invoice Examples

### 1. Create Invoice

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/invoices \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "clientName": "ABC Corporation",
    "amount": 15000.50,
    "dateIssued": "2024-01-15",
    "dueDate": "2024-02-15",
    "status": "PENDING"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "clientName": "ABC Corporation",
    "amount": 15000.50,
    "dateIssued": "2024-01-15",
    "dueDate": "2024-02-15",
    "status": "PENDING",
    "createdAt": "2024-01-15T11:00:00",
    "updatedAt": "2024-01-15T11:00:00"
  },
  "message": "Invoice created successfully",
  "timestamp": "2024-01-15T11:00:00"
}
```

### 2. List Invoices (Paginated)

**Request:**
```bash
curl -X GET "http://localhost:8080/api/v1/invoices?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

**Response:**
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": "660e8400-e29b-41d4-a716-446655440001",
        "userId": "550e8400-e29b-41d4-a716-446655440000",
        "clientName": "ABC Corporation",
        "amount": 15000.50,
        "dateIssued": "2024-01-15",
        "dueDate": "2024-02-15",
        "status": "PENDING",
        "createdAt": "2024-01-15T11:00:00",
        "updatedAt": "2024-01-15T11:00:00"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true
  },
  "message": "Invoices retrieved successfully",
  "timestamp": "2024-01-15T11:05:00"
}
```

### 3. Get Invoice by ID

**Request:**
```bash
curl -X GET http://localhost:8080/api/v1/invoices/660e8400-e29b-41d4-a716-446655440001 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### 4. Update Invoice

**Request:**
```bash
curl -X PUT http://localhost:8080/api/v1/invoices/660e8400-e29b-41d4-a716-446655440001 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "status": "PAID"
  }'
```

### 5. Delete Invoice

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/v1/invoices/660e8400-e29b-41d4-a716-446655440001 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## Quotation Examples

### 1. Create Quotation

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/quotations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "clientName": "XYZ Ltd",
    "amount": 25000.00,
    "validUntil": "2024-03-15",
    "status": "DRAFT",
    "description": "Website development project quotation"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "770e8400-e29b-41d4-a716-446655440002",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "clientName": "XYZ Ltd",
    "amount": 25000.00,
    "validUntil": "2024-03-15",
    "status": "DRAFT",
    "description": "Website development project quotation",
    "createdAt": "2024-01-15T12:00:00",
    "updatedAt": "2024-01-15T12:00:00"
  },
  "message": "Quotation created successfully",
  "timestamp": "2024-01-15T12:00:00"
}
```

### 2. Update Quotation Status

**Request:**
```bash
curl -X PUT http://localhost:8080/api/v1/quotations/770e8400-e29b-41d4-a716-446655440002 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "status": "SENT"
  }'
```

## E-Way Bill Examples

### 1. Create E-Way Bill

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/eway-bills \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "billNumber": "EWB123456789",
    "consignorName": "ABC Suppliers",
    "consigneeName": "XYZ Retailers",
    "goodsValue": 50000.00,
    "transportMode": "ROAD",
    "vehicleNumber": "MH12AB1234",
    "distanceKm": 250,
    "validFrom": "2024-01-15",
    "validUntil": "2024-01-20",
    "status": "ACTIVE"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "880e8400-e29b-41d4-a716-446655440003",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "billNumber": "EWB123456789",
    "consignorName": "ABC Suppliers",
    "consigneeName": "XYZ Retailers",
    "goodsValue": 50000.00,
    "transportMode": "ROAD",
    "vehicleNumber": "MH12AB1234",
    "distanceKm": 250,
    "validFrom": "2024-01-15",
    "validUntil": "2024-01-20",
    "status": "ACTIVE",
    "createdAt": "2024-01-15T13:00:00",
    "updatedAt": "2024-01-15T13:00:00"
  },
  "message": "E-way bill created successfully",
  "timestamp": "2024-01-15T13:00:00"
}
```

## Payment Examples

### 1. Create Payment Receipt

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/payments \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "invoiceId": "660e8400-e29b-41d4-a716-446655440001",
    "payerName": "ABC Corporation",
    "amount": 15000.50,
    "paymentDate": "2024-01-20",
    "paymentMethod": "BANK_TRANSFER",
    "transactionReference": "TXN987654321",
    "notes": "Payment received for Invoice #INV-001"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": "990e8400-e29b-41d4-a716-446655440004",
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "invoiceId": "660e8400-e29b-41d4-a716-446655440001",
    "payerName": "ABC Corporation",
    "amount": 15000.50,
    "paymentDate": "2024-01-20",
    "paymentMethod": "BANK_TRANSFER",
    "transactionReference": "TXN987654321",
    "notes": "Payment received for Invoice #INV-001",
    "createdAt": "2024-01-20T14:00:00",
    "updatedAt": "2024-01-20T14:00:00"
  },
  "message": "Payment created successfully",
  "timestamp": "2024-01-20T14:00:00"
}
```

## Error Response Examples

### 400 Bad Request - Validation Error

```json
{
  "success": false,
  "data": {
    "email": "Email must be valid",
    "password": "Password must be between 6 and 100 characters"
  },
  "message": "Validation failed",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 401 Unauthorized

```json
{
  "success": false,
  "data": null,
  "message": "Invalid credentials",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 404 Not Found

```json
{
  "success": false,
  "data": null,
  "message": "Invoice not found with id: 660e8400-e29b-41d4-a716-446655440001",
  "timestamp": "2024-01-15T11:00:00"
}
```

### 409 Conflict

```json
{
  "success": false,
  "data": null,
  "message": "Email already exists",
  "timestamp": "2024-01-15T10:30:00"
}
```

## Postman Collection

You can import these examples into Postman by creating a collection with the following structure:

1. Create environment variables:
   - `base_url`: http://localhost:8080
   - `jwt_token`: (will be set after login)

2. Add a pre-request script to automatically include the JWT token:
```javascript
pm.request.headers.add({
    key: 'Authorization',
    value: 'Bearer ' + pm.environment.get('jwt_token')
});
```

3. Add a test script to save the JWT token after login:
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    if (jsonData.data && jsonData.data.token) {
        pm.environment.set('jwt_token', jsonData.data.token);
    }
}
```

## Testing Tips

1. **Use environment variables** for base URL and tokens
2. **Save tokens** after successful login
3. **Test pagination** with different page sizes
4. **Test validation** by sending invalid data
5. **Test authorization** by using expired or invalid tokens
6. **Test edge cases** like empty lists, non-existent IDs
7. **Monitor response times** for performance testing

## Rate Limiting (Future Enhancement)

When rate limiting is implemented, responses will include headers:
```
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 99
X-RateLimit-Reset: 1642252800
```

