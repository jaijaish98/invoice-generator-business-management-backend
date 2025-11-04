package com.example.businessapp.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank(message = "Full name is required")
    @field:Size(min = 2, max = 255, message = "Full name must be between 2 and 255 characters")
    val fullName: String,

    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String,

    @field:NotBlank(message = "Mobile number is required")
    @field:Pattern(
        regexp = "^\\+[1-9]\\d{0,3}[0-9]{6,15}$",
        message = "Mobile number must be in format +[country code][number] (e.g., +919876543210)"
    )
    val mobileNumber: String,

    @field:NotBlank(message = "Password is required")
    @field:Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    val password: String
)

data class LoginRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Email must be valid")
    val email: String,

    @field:NotBlank(message = "Password is required")
    val password: String
)

data class ChangePasswordRequest(
    @field:NotBlank(message = "Current password is required")
    val currentPassword: String,

    @field:NotBlank(message = "New password is required")
    @field:Size(min = 6, max = 100, message = "New password must be between 6 and 100 characters")
    val newPassword: String,

    @field:NotBlank(message = "Confirm password is required")
    val confirmPassword: String
)

data class UpdateAccountRequest(
    @field:Size(min = 2, max = 255, message = "Full name must be between 2 and 255 characters")
    val fullName: String?,

    @field:Email(message = "Email must be valid")
    val email: String?,

    @field:Pattern(
        regexp = "^\\+[1-9]\\d{0,3}[0-9]{6,15}$",
        message = "Mobile number must be in format +[country code][number] (e.g., +919876543210)"
    )
    val mobileNumber: String?
)

