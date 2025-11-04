package com.example.businessapp.service

import com.example.businessapp.dto.CreateQuotationRequest
import com.example.businessapp.dto.QuotationResponse
import com.example.businessapp.dto.UpdateQuotationRequest
import com.example.businessapp.entity.Quotation
import com.example.businessapp.exception.EntityNotFoundException
import com.example.businessapp.exception.ForbiddenException
import com.example.businessapp.repository.QuotationRepository
import com.example.businessapp.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class QuotationService(
    private val quotationRepository: QuotationRepository,
    private val userRepository: UserRepository
) {
    
    @Transactional
    fun createQuotation(request: CreateQuotationRequest): QuotationResponse {
        val userId = getCurrentUserId()
        
        val quotation = Quotation(
            userId = userId,
            clientName = request.clientName,
            amount = request.amount,
            validUntil = request.validUntil,
            status = request.status,
            description = request.description
        )
        
        val savedQuotation = quotationRepository.save(quotation)
        return savedQuotation.toResponse()
    }
    
    fun getQuotations(pageable: Pageable): Page<QuotationResponse> {
        val userId = getCurrentUserId()
        return quotationRepository.findByUserId(userId, pageable)
            .map { it.toResponse() }
    }
    
    fun getQuotationById(id: UUID): QuotationResponse {
        val userId = getCurrentUserId()
        val quotation = quotationRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Quotation not found with id: $id")
        
        return quotation.toResponse()
    }
    
    @Transactional
    fun updateQuotation(id: UUID, request: UpdateQuotationRequest): QuotationResponse {
        val userId = getCurrentUserId()
        val quotation = quotationRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Quotation not found with id: $id")
        
        val updatedQuotation = quotation.copy(
            clientName = request.clientName ?: quotation.clientName,
            amount = request.amount ?: quotation.amount,
            validUntil = request.validUntil ?: quotation.validUntil,
            status = request.status ?: quotation.status,
            description = request.description ?: quotation.description,
            updatedAt = LocalDateTime.now()
        )
        
        val savedQuotation = quotationRepository.save(updatedQuotation)
        return savedQuotation.toResponse()
    }
    
    @Transactional
    fun deleteQuotation(id: UUID) {
        val userId = getCurrentUserId()
        val quotation = quotationRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Quotation not found with id: $id")
        
        quotationRepository.delete(quotation)
    }
    
    private fun getCurrentUserId(): UUID {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw ForbiddenException("User not found")
        return user.id
    }
    
    private fun Quotation.toResponse() = QuotationResponse(
        id = id,
        userId = userId,
        clientName = clientName,
        amount = amount,
        validUntil = validUntil,
        status = status,
        description = description,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

