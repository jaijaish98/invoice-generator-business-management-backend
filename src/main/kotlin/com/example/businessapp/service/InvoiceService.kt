package com.example.businessapp.service

import com.example.businessapp.dto.CreateInvoiceRequest
import com.example.businessapp.dto.InvoiceResponse
import com.example.businessapp.dto.UpdateInvoiceRequest
import com.example.businessapp.entity.Invoice
import com.example.businessapp.exception.EntityNotFoundException
import com.example.businessapp.exception.ForbiddenException
import com.example.businessapp.repository.InvoiceRepository
import com.example.businessapp.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class InvoiceService(
    private val invoiceRepository: InvoiceRepository,
    private val userRepository: UserRepository
) {
    
    @Transactional
    fun createInvoice(request: CreateInvoiceRequest): InvoiceResponse {
        val userId = getCurrentUserId()
        
        val invoice = Invoice(
            userId = userId,
            clientName = request.clientName,
            amount = request.amount,
            dateIssued = request.dateIssued,
            dueDate = request.dueDate,
            status = request.status
        )
        
        val savedInvoice = invoiceRepository.save(invoice)
        return savedInvoice.toResponse()
    }
    
    fun getInvoices(pageable: Pageable): Page<InvoiceResponse> {
        val userId = getCurrentUserId()
        return invoiceRepository.findByUserId(userId, pageable)
            .map { it.toResponse() }
    }
    
    fun getInvoiceById(id: UUID): InvoiceResponse {
        val userId = getCurrentUserId()
        val invoice = invoiceRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Invoice not found with id: $id")
        
        return invoice.toResponse()
    }
    
    @Transactional
    fun updateInvoice(id: UUID, request: UpdateInvoiceRequest): InvoiceResponse {
        val userId = getCurrentUserId()
        val invoice = invoiceRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Invoice not found with id: $id")
        
        val updatedInvoice = invoice.copy(
            clientName = request.clientName ?: invoice.clientName,
            amount = request.amount ?: invoice.amount,
            dateIssued = request.dateIssued ?: invoice.dateIssued,
            dueDate = request.dueDate ?: invoice.dueDate,
            status = request.status ?: invoice.status,
            updatedAt = LocalDateTime.now()
        )
        
        val savedInvoice = invoiceRepository.save(updatedInvoice)
        return savedInvoice.toResponse()
    }
    
    @Transactional
    fun deleteInvoice(id: UUID) {
        val userId = getCurrentUserId()
        val invoice = invoiceRepository.findByUserIdAndId(userId, id)
            ?: throw EntityNotFoundException("Invoice not found with id: $id")
        
        invoiceRepository.delete(invoice)
    }
    
    private fun getCurrentUserId(): UUID {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw ForbiddenException("User not found")
        return user.id
    }
    
    private fun Invoice.toResponse() = InvoiceResponse(
        id = id,
        userId = userId,
        clientName = clientName,
        amount = amount,
        dateIssued = dateIssued,
        dueDate = dueDate,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

