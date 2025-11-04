package com.example.businessapp.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "eway_bills")
data class EwayBill(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),
    
    @Column(nullable = false, name = "user_id")
    val userId: UUID,
    
    @Column(nullable = false, unique = true, name = "bill_number")
    val billNumber: String,
    
    @Column(nullable = false, name = "consignor_name")
    val consignorName: String,
    
    @Column(nullable = false, name = "consignee_name")
    val consigneeName: String,
    
    @Column(nullable = false, precision = 19, scale = 2, name = "goods_value")
    val goodsValue: BigDecimal,
    
    @Column(nullable = false, name = "transport_mode")
    val transportMode: String,
    
    @Column(name = "vehicle_number")
    val vehicleNumber: String? = null,
    
    @Column(nullable = false, name = "distance_km")
    val distanceKm: Int,
    
    @Column(nullable = false, name = "valid_from")
    val validFrom: LocalDate,
    
    @Column(nullable = false, name = "valid_until")
    val validUntil: LocalDate,
    
    @Column(nullable = false)
    val status: String = "ACTIVE",
    
    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false, name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

