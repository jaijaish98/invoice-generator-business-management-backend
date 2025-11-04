package com.example.businessapp.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "payments")
data class Payment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @Column(nullable = false, name = "user_id")
    val userId: UUID,
    
    @Column(name = "invoice_id")
    val invoiceId: UUID? = null,
    
    @Column(nullable = false, name = "payer_name")
    val payerName: String,
    
    @Column(nullable = false, precision = 19, scale = 2)
    val amount: BigDecimal,
    
    @Column(nullable = false, name = "payment_date")
    val paymentDate: LocalDate,
    
    @Column(nullable = false, name = "payment_method")
    val paymentMethod: String,
    
    @Column(name = "transaction_reference")
    val transactionReference: String? = null,
    
    @Column(columnDefinition = "TEXT")
    val notes: String? = null,
    
    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false, name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

