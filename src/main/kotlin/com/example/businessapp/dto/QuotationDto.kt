package com.example.businessapp.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class CreateQuotationRequest(
    @field:NotBlank(message = "Client name is required")
    @field:Size(max = 255, message = "Client name must not exceed 255 characters")
    val clientName: String,
    
    @field:NotNull(message = "Amount is required")
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    val amount: BigDecimal,
    
    @field:NotNull(message = "Valid until date is required")
    val validUntil: LocalDate,
    
    @field:NotBlank(message = "Status is required")
    @field:Pattern(regexp = "DRAFT|SENT|ACCEPTED|REJECTED|EXPIRED", message = "Status must be DRAFT, SENT, ACCEPTED, REJECTED, or EXPIRED")
    val status: String = "DRAFT",
    
    val description: String? = null
)

data class UpdateQuotationRequest(
    @field:Size(max = 255, message = "Client name must not exceed 255 characters")
    val clientName: String?,
    
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    val amount: BigDecimal?,
    
    val validUntil: LocalDate?,
    
    @field:Pattern(regexp = "DRAFT|SENT|ACCEPTED|REJECTED|EXPIRED", message = "Status must be DRAFT, SENT, ACCEPTED, REJECTED, or EXPIRED")
    val status: String?,
    
    val description: String?
)

data class QuotationResponse(
    val id: UUID,
    val userId: UUID,
    val clientName: String,
    val amount: BigDecimal,
    val validUntil: LocalDate,
    val status: String,
    val description: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

