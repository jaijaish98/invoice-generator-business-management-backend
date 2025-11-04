package com.example.businessapp.service

import com.example.businessapp.dto.CreateEwayBillRequest
import com.example.businessapp.dto.EwayBillResponse
import com.example.businessapp.dto.UpdateEwayBillRequest
import com.example.businessapp.entity.EwayBill
import com.example.businessapp.exception.BadRequestException
import com.example.businessapp.exception.EntityNotFoundException
import com.example.businessapp.exception.ForbiddenException
import com.example.businessapp.repository.EwayBillRepository
import com.example.businessapp.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class EwayBillService(
    private val ewayBillRepository: EwayBillRepository,
    private val userRepository: UserRepository
) {
    
    @Transactional
    fun createEwayBill(request: CreateEwayBillRequest): EwayBillResponse {
        val userId = getCurrentUserId()
        
        if (ewayBillRepository.existsByBillNumber(request.billNumber)) {
            throw BadRequestException("E-way bill number already exists")
        }
        
        val ewayBill = EwayBill(
            userId = userId,
            billNumber = request.billNumber,
            consignorName = request.consignorName,
            consigneeName = request.consigneeName,
            goodsValue = request.goodsValue,
            transportMode = request.transportMode,
            vehicleNumber = request.vehicleNumber,
            distanceKm = request.distanceKm,
            validFrom = request.validFrom,
            validUntil = request.validUntil,
            status = request.status
        )
        
        val savedEwayBill = ewayBillRepository.save(ewayBill)
        return savedEwayBill.toResponse()
    }
    
    fun getEwayBills(pageable: Pageable): Page<EwayBillResponse> {
        val userId = getCurrentUserId()
        return ewayBillRepository.findByUserId(userId, pageable)
            .map { it.toResponse() }
    }
    
    fun getEwayBillById(id: UUID): EwayBillResponse {
        val userId = getCurrentUserId()
        val ewayBill = ewayBillRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("E-way bill not found with id: $id")
        
        return ewayBill.toResponse()
    }
    
    @Transactional
    fun updateEwayBill(id: UUID, request: UpdateEwayBillRequest): EwayBillResponse {
        val userId = getCurrentUserId()
        val ewayBill = ewayBillRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("E-way bill not found with id: $id")
        
        val updatedEwayBill = ewayBill.copy(
            consignorName = request.consignorName ?: ewayBill.consignorName,
            consigneeName = request.consigneeName ?: ewayBill.consigneeName,
            goodsValue = request.goodsValue ?: ewayBill.goodsValue,
            transportMode = request.transportMode ?: ewayBill.transportMode,
            vehicleNumber = request.vehicleNumber ?: ewayBill.vehicleNumber,
            distanceKm = request.distanceKm ?: ewayBill.distanceKm,
            validFrom = request.validFrom ?: ewayBill.validFrom,
            validUntil = request.validUntil ?: ewayBill.validUntil,
            status = request.status ?: ewayBill.status,
            updatedAt = LocalDateTime.now()
        )
        
        val savedEwayBill = ewayBillRepository.save(updatedEwayBill)
        return savedEwayBill.toResponse()
    }
    
    @Transactional
    fun deleteEwayBill(id: UUID) {
        val userId = getCurrentUserId()
        val ewayBill = ewayBillRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("E-way bill not found with id: $id")
        
        ewayBillRepository.delete(ewayBill)
    }
    
    private fun getCurrentUserId(): UUID {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw ForbiddenException("User not found")
        return user.id
    }
    
    private fun EwayBill.toResponse() = EwayBillResponse(
        id = id,
        userId = userId,
        billNumber = billNumber,
        consignorName = consignorName,
        consigneeName = consigneeName,
        goodsValue = goodsValue,
        transportMode = transportMode,
        vehicleNumber = vehicleNumber,
        distanceKm = distanceKm,
        validFrom = validFrom,
        validUntil = validUntil,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

