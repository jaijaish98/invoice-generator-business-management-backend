package com.example.businessapp.service

import com.example.businessapp.dto.CreatePaymentRequest
import com.example.businessapp.dto.PaymentResponse
import com.example.businessapp.dto.UpdatePaymentRequest
import com.example.businessapp.entity.Payment
import com.example.businessapp.exception.EntityNotFoundException
import com.example.businessapp.exception.ForbiddenException
import com.example.businessapp.repository.PaymentRepository
import com.example.businessapp.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class PaymentService(
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository
) {
    
    @Transactional
    fun createPayment(request: CreatePaymentRequest): PaymentResponse {
        val userId = getCurrentUserId()
        
        val payment = Payment(
            userId = userId,
            invoiceId = request.invoiceId,
            payerName = request.payerName,
            amount = request.amount,
            paymentDate = request.paymentDate,
            paymentMethod = request.paymentMethod,
            transactionReference = request.transactionReference,
            notes = request.notes
        )
        
        val savedPayment = paymentRepository.save(payment)
        return savedPayment.toResponse()
    }
    
    fun getPayments(pageable: Pageable): Page<PaymentResponse> {
        val userId = getCurrentUserId()
        return paymentRepository.findByUserId(userId, pageable)
            .map { it.toResponse() }
    }
    
    fun getPaymentById(id: UUID): PaymentResponse {
        val userId = getCurrentUserId()
        val payment = paymentRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Payment not found with id: $id")
        
        return payment.toResponse()
    }
    
    @Transactional
    fun updatePayment(id: UUID, request: UpdatePaymentRequest): PaymentResponse {
        val userId = getCurrentUserId()
        val payment = paymentRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Payment not found with id: $id")
        
        val updatedPayment = payment.copy(
            payerName = request.payerName ?: payment.payerName,
            amount = request.amount ?: payment.amount,
            paymentDate = request.paymentDate ?: payment.paymentDate,
            paymentMethod = request.paymentMethod ?: payment.paymentMethod,
            transactionReference = request.transactionReference ?: payment.transactionReference,
            notes = request.notes ?: payment.notes,
            updatedAt = LocalDateTime.now()
        )
        
        val savedPayment = paymentRepository.save(updatedPayment)
        return savedPayment.toResponse()
    }
    
    @Transactional
    fun deletePayment(id: UUID) {
        val userId = getCurrentUserId()
        val payment = paymentRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Payment not found with id: $id")
        
        paymentRepository.delete(payment)
    }
    
    private fun getCurrentUserId(): UUID {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw ForbiddenException("User not found")
        return user.id
    }
    
    private fun Payment.toResponse() = PaymentResponse(
        id = id,
        userId = userId,
        invoiceId = invoiceId,
        payerName = payerName,
        amount = amount,
        paymentDate = paymentDate,
        paymentMethod = paymentMethod,
        transactionReference = transactionReference,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

