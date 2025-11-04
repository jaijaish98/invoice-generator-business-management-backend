package com.example.businessapp.repository

import com.example.businessapp.entity.Payment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PaymentRepository : JpaRepository<Payment, UUID> {
    fun findByUserId(userId: UUID, pageable: Pageable): Page<Payment>
    fun findByUserIdAndId(userId: UUID, id: UUID): Payment?
}

