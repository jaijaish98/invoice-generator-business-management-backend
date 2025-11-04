package com.example.businessapp.entity

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(
    name = "users",
    indexes = [
        Index(name = "idx_users_email_mobile", columnList = "email,mobile_number", unique = true)
    ]
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "mobile_number", nullable = false, length = 20, unique = true)
    val mobileNumber: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val role: String = "ROLE_USER",

    @Column(nullable = false, name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    // Extract country code from mobile number (e.g., +919876543210 -> +91)
    val countryCode: String
        get() {
            if (!mobileNumber.startsWith("+")) return ""

            // Extract digits after + until we have 1-4 digits for country code
            // Common country codes: +1 (US), +44 (UK), +91 (India), +86 (China), +971 (UAE)
            val digits = mobileNumber.substring(1).takeWhile { it.isDigit() }

            // Try to match known country code patterns
            return when {
                digits.length >= 3 && digits.take(3) in listOf("971") -> "+${digits.take(3)}" // UAE
                digits.length >= 2 && digits.take(2) in listOf("91", "44", "86", "61", "81", "82", "65", "60", "66", "84", "62", "63", "92", "94", "95", "98") -> "+${digits.take(2)}"
                digits.length >= 1 && digits.take(1) in listOf("1", "7") -> "+${digits.take(1)}" // US/Canada, Russia
                digits.length >= 2 -> "+${digits.take(2)}" // Default to 2 digits
                else -> "+$digits"
            }
        }
}

