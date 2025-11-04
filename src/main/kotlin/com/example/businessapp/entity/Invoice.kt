package com.example.businessapp.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "invoices")
data class Invoice(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @Column(nullable = false, name = "user_id")
    val userId: UUID,
    
    @Column(nullable = false, name = "client_name")
    val clientName: String,
    
    @Column(nullable = false, precision = 19, scale = 2)
    val amount: BigDecimal,
    
    @Column(nullable = false, name = "date_issued")
    val dateIssued: LocalDate,
    
    @Column(nullable = false, name = "due_date")
    val dueDate: LocalDate,
    
    @Column(nullable = false)
    val status: String = "PENDING",
    
    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false, name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

