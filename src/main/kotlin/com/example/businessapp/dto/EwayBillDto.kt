package com.example.businessapp.dto

import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class CreateEwayBillRequest(
    @field:NotBlank(message = "Bill number is required")
    @field:Size(max = 100, message = "Bill number must not exceed 100 characters")
    val billNumber: String,
    
    @field:NotBlank(message = "Consignor name is required")
    @field:Size(max = 255, message = "Consignor name must not exceed 255 characters")
    val consignorName: String,
    
    @field:NotBlank(message = "Consignee name is required")
    @field:Size(max = 255, message = "Consignee name must not exceed 255 characters")
    val consigneeName: String,
    
    @field:NotNull(message = "Goods value is required")
    @field:DecimalMin(value = "0.01", message = "Goods value must be greater than 0")
    val goodsValue: BigDecimal,
    
    @field:NotBlank(message = "Transport mode is required")
    @field:Pattern(regexp = "ROAD|RAIL|AIR|SHIP", message = "Transport mode must be ROAD, RAIL, AIR, or SHIP")
    val transportMode: String,
    
    @field:Size(max = 50, message = "Vehicle number must not exceed 50 characters")
    val vehicleNumber: String? = null,
    
    @field:NotNull(message = "Distance is required")
    @field:Min(value = 1, message = "Distance must be at least 1 km")
    val distanceKm: Int,
    
    @field:NotNull(message = "Valid from date is required")
    val validFrom: LocalDate,
    
    @field:NotNull(message = "Valid until date is required")
    val validUntil: LocalDate,
    
    @field:NotBlank(message = "Status is required")
    @field:Pattern(regexp = "ACTIVE|EXPIRED|CANCELLED", message = "Status must be ACTIVE, EXPIRED, or CANCELLED")
    val status: String = "ACTIVE"
)

data class UpdateEwayBillRequest(
    @field:Size(max = 100, message = "Bill number must not exceed 100 characters")
    val billNumber: String? = null,

    @field:Size(max = 255, message = "Consignor name must not exceed 255 characters")
    val consignorName: String? = null,

    @field:Size(max = 255, message = "Consignee name must not exceed 255 characters")
    val consigneeName: String? = null,

    @field:DecimalMin(value = "0.01", message = "Goods value must be greater than 0")
    val goodsValue: BigDecimal? = null,

    @field:Pattern(regexp = "ROAD|RAIL|AIR|SHIP", message = "Transport mode must be ROAD, RAIL, AIR, or SHIP")
    val transportMode: String? = null,

    @field:Size(max = 50, message = "Vehicle number must not exceed 50 characters")
    val vehicleNumber: String? = null,

    @field:Min(value = 1, message = "Distance must be at least 1 km")
    val distanceKm: Int? = null,

    val validFrom: LocalDate? = null,

    val validUntil: LocalDate? = null,

    @field:Pattern(regexp = "ACTIVE|EXPIRED|CANCELLED", message = "Status must be ACTIVE, EXPIRED, or CANCELLED")
    val status: String? = null
)

data class EwayBillResponse(
    val id: UUID,
    val userId: UUID,
    val billNumber: String,
    val consignorName: String,
    val consigneeName: String,
    val goodsValue: BigDecimal,
    val transportMode: String,
    val vehicleNumber: String?,
    val distanceKm: Int,
    val validFrom: LocalDate,
    val validUntil: LocalDate,
    val status: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

