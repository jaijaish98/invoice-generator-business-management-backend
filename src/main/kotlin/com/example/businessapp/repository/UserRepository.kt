package com.example.businessapp.repository

import com.example.businessapp.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun existsByMobileNumber(mobileNumber: String): Boolean
    fun existsByEmailAndMobileNumber(email: String, mobileNumber: String): Boolean
    fun findByEmailOrMobileNumber(email: String, mobileNumber: String): User?
}

