package com.example.businessapp.repository

import com.example.businessapp.entity.Quotation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface QuotationRepository : JpaRepository<Quotation, UUID> {
    fun findByUserId(userId: UUID, pageable: Pageable): Page<Quotation>
    fun findByUserIdAndId(userId: UUID, id: UUID): Quotation?
}

