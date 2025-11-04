package com.example.businessapp.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class CreatePaymentRequest(
    val invoiceId: UUID? = null,
    
    @field:NotBlank(message = "Payer name is required")
    @field:Size(max = 255, message = "Payer name must not exceed 255 characters")
    val payerName: String,
    
    @field:NotNull(message = "Amount is required")
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    val amount: BigDecimal,
    
    @field:NotNull(message = "Payment date is required")
    val paymentDate: LocalDate,
    
    @field:NotBlank(message = "Payment method is required")
    @field:Pattern(
        regexp = "CASH|CREDIT_CARD|DEBIT_CARD|BANK_TRANSFER|UPI|CHEQUE|OTHER",
        message = "Payment method must be CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, UPI, CHEQUE, or OTHER"
    )
    val paymentMethod: String,
    
    @field:Size(max = 255, message = "Transaction reference must not exceed 255 characters")
    val transactionReference: String? = null,
    
    val notes: String? = null
)

data class UpdatePaymentRequest(
    @field:Size(max = 255, message = "Payer name must not exceed 255 characters")
    val payerName: String?,
    
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    val amount: BigDecimal?,
    
    val paymentDate: LocalDate?,
    
    @field:Pattern(
        regexp = "CASH|CREDIT_CARD|DEBIT_CARD|BANK_TRANSFER|UPI|CHEQUE|OTHER",
        message = "Payment method must be CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, UPI, CHEQUE, or OTHER"
    )
    val paymentMethod: String?,
    
    @field:Size(max = 255, message = "Transaction reference must not exceed 255 characters")
    val transactionReference: String?,
    
    val notes: String?
)

data class PaymentResponse(
    val id: UUID,
    val userId: UUID,
    val invoiceId: UUID?,
    val payerName: String,
    val amount: BigDecimal,
    val paymentDate: LocalDate,
    val paymentMethod: String,
    val transactionReference: String?,
    val notes: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

