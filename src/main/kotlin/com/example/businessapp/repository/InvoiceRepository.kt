package com.example.businessapp.repository

import com.example.businessapp.entity.Invoice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface InvoiceRepository : JpaRepository<Invoice, UUID> {
    fun findByUserId(userId: UUID, pageable: Pageable): Page<Invoice>
    fun findByUserIdAndId(userId: UUID, id: UUID): Invoice?
}

