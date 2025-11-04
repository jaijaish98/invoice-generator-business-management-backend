package com.example.businessapp.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class CreateInvoiceRequest(
    @field:NotBlank(message = "Client name is required")
    @field:Size(max = 255, message = "Client name must not exceed 255 characters")
    val clientName: String,
    
    @field:NotNull(message = "Amount is required")
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    val amount: BigDecimal,
    
    @field:NotNull(message = "Date issued is required")
    val dateIssued: LocalDate,
    
    @field:NotNull(message = "Due date is required")
    val dueDate: LocalDate,
    
    @field:NotBlank(message = "Status is required")
    @field:Pattern(regexp = "PENDING|PAID|OVERDUE|CANCELLED", message = "Status must be PENDING, PAID, OVERDUE, or CANCELLED")
    val status: String = "PENDING"
)

data class UpdateInvoiceRequest(
    @field:Size(max = 255, message = "Client name must not exceed 255 characters")
    val clientName: String?,
    
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    val amount: BigDecimal?,
    
    val dateIssued: LocalDate?,
    
    val dueDate: LocalDate?,
    
    @field:Pattern(regexp = "PENDING|PAID|OVERDUE|CANCELLED", message = "Status must be PENDING, PAID, OVERDUE, or CANCELLED")
    val status: String?
)

data class InvoiceResponse(
    val id: UUID,
    val userId: UUID,
    val clientName: String,
    val amount: BigDecimal,
    val dateIssued: LocalDate,
    val dueDate: LocalDate,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

