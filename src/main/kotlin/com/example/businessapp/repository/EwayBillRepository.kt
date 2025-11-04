package com.example.businessapp.repository

import com.example.businessapp.entity.EwayBill
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface EwayBillRepository : JpaRepository<EwayBill, UUID> {
    fun findByUserId(userId: UUID, pageable: Pageable): Page<EwayBill>
    fun findByUserIdAndId(userId: UUID, id: UUID): EwayBill?
    fun existsByBillNumber(billNumber: String): Boolean
}

