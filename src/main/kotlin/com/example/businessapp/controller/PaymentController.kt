package com.example.businessapp.controller

import com.example.businessapp.dto.*
import com.example.businessapp.service.PaymentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Payment receipt management endpoints")
@SecurityRequirement(name = "bearerAuth")
class PaymentController(
    private val paymentService: PaymentService
) {
    
    @PostMapping
    @Operation(summary = "Create payment receipt", description = "Record a new payment receipt")
    @SwaggerApiResponse(responseCode = "201", description = "Payment created successfully")
    @SwaggerApiResponse(responseCode = "400", description = "Invalid input")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun createPayment(@Valid @RequestBody request: CreatePaymentRequest): ResponseEntity<ApiResponse<PaymentResponse>> {
        val payment = paymentService.createPayment(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                success = true,
                data = payment,
                message = "Payment created successfully"
            )
        )
    }
    
    @GetMapping
    @Operation(summary = "Get all payments", description = "Get all payment receipts for authenticated user with pagination")
    @SwaggerApiResponse(responseCode = "200", description = "Payments retrieved successfully")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getPayments(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<PaymentResponse>>> {
        val payments = paymentService.getPayments(pageable)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = payments,
                message = "Payments retrieved successfully"
            )
        )
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Retrieve a specific payment receipt by ID")
    @SwaggerApiResponse(responseCode = "200", description = "Payment retrieved successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Payment not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getPaymentById(@PathVariable id: UUID): ResponseEntity<ApiResponse<PaymentResponse>> {
        val payment = paymentService.getPaymentById(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = payment,
                message = "Payment retrieved successfully"
            )
        )
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update payment", description = "Update an existing payment receipt")
    @SwaggerApiResponse(responseCode = "200", description = "Payment updated successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Payment not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun updatePayment(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdatePaymentRequest
    ): ResponseEntity<ApiResponse<PaymentResponse>> {
        val payment = paymentService.updatePayment(id, request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = payment,
                message = "Payment updated successfully"
            )
        )
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment", description = "Delete a payment receipt")
    @SwaggerApiResponse(responseCode = "200", description = "Payment deleted successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Payment not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun deletePayment(@PathVariable id: UUID): ResponseEntity<ApiResponse<Nothing>> {
        paymentService.deletePayment(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = null,
                message = "Payment deleted successfully"
            )
        )
    }
}

