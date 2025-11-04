package com.example.businessapp.dto

import java.time.LocalDateTime
import java.util.*

data class AuthResponse(
    val token: String,
    val user: UserResponse
)

data class UserResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val role: String,
    val createdAt: LocalDateTime
)

