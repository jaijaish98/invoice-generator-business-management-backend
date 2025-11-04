package com.example.businessapp.dto

import java.time.LocalDateTime

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

