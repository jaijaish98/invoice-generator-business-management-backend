package com.example.businessapp.service

import com.example.businessapp.dto.*
import com.example.businessapp.entity.User
import com.example.businessapp.exception.BadRequestException
import com.example.businessapp.exception.UnauthorizedException
import com.example.businessapp.repository.UserRepository
import com.example.businessapp.security.JwtTokenProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val authenticationManager: AuthenticationManager
) {
    
    @Transactional
    fun register(request: RegisterRequest): AuthResponse {
        // Validate email uniqueness
        if (userRepository.existsByEmail(request.email)) {
            throw BadRequestException("An account with this email already exists")
        }

        // Validate mobile number uniqueness
        if (userRepository.existsByMobileNumber(request.mobileNumber)) {
            throw BadRequestException("An account with this mobile number already exists")
        }

        // Validate email and mobile number combination
        if (userRepository.existsByEmailAndMobileNumber(request.email, request.mobileNumber)) {
            throw BadRequestException("An account with this email and mobile number combination already exists")
        }

        // Check if any account exists with either email or mobile number
        val existingUser = userRepository.findByEmailOrMobileNumber(request.email, request.mobileNumber)
        if (existingUser != null) {
            if (existingUser.email == request.email) {
                throw BadRequestException("An account with this email already exists")
            }
            if (existingUser.mobileNumber == request.mobileNumber) {
                throw BadRequestException("An account with this mobile number already exists")
            }
        }

        val user = User(
            name = request.fullName,
            email = request.email,
            mobileNumber = request.mobileNumber,
            password = passwordEncoder.encode(request.password),
            role = "ROLE_USER"
        )

        val savedUser = userRepository.save(user)
        val token = jwtTokenProvider.generateToken(savedUser.email)

        return AuthResponse(
            token = token,
            user = savedUser.toUserResponse()
        )
    }
    
    fun login(request: LoginRequest): AuthResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        
        SecurityContextHolder.getContext().authentication = authentication
        
        val user = userRepository.findByEmail(request.email)
            ?: throw UnauthorizedException("Invalid credentials")
        
        val token = jwtTokenProvider.generateToken(authentication)
        
        return AuthResponse(
            token = token,
            user = user.toUserResponse()
        )
    }
    
    fun getCurrentUser(): UserResponse {
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw UnauthorizedException("User not found")

        return user.toUserResponse()
    }

    @Transactional
    fun changePassword(request: ChangePasswordRequest): UserResponse {
        // Get current authenticated user
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw UnauthorizedException("User not found")

        // Validate new password and confirm password match
        if (request.newPassword != request.confirmPassword) {
            throw BadRequestException("New password and confirm password do not match")
        }

        // Validate current password
        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw BadRequestException("Current password is incorrect")
        }

        // Validate new password is different from current password
        if (passwordEncoder.matches(request.newPassword, user.password)) {
            throw BadRequestException("New password must be different from current password")
        }

        // Update password
        val updatedUser = user.copy(password = passwordEncoder.encode(request.newPassword))
        val savedUser = userRepository.save(updatedUser)

        return savedUser.toUserResponse()
    }

    @Transactional
    fun updateAccount(request: UpdateAccountRequest): UserResponse {
        // Get current authenticated user
        val email = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByEmail(email)
            ?: throw UnauthorizedException("User not found")

        // Validate at least one field is being updated
        if (request.fullName == null && request.email == null && request.mobileNumber == null) {
            throw BadRequestException("At least one field must be provided for update")
        }

        // Validate email uniqueness if email is being changed
        if (request.email != null && request.email != user.email) {
            if (userRepository.existsByEmail(request.email)) {
                throw BadRequestException("An account with this email already exists")
            }
        }

        // Validate mobile number uniqueness if mobile number is being changed
        if (request.mobileNumber != null && request.mobileNumber != user.mobileNumber) {
            if (userRepository.existsByMobileNumber(request.mobileNumber)) {
                throw BadRequestException("An account with this mobile number already exists")
            }
        }

        // Update user with new values
        val updatedUser = user.copy(
            name = request.fullName ?: user.name,
            email = request.email ?: user.email,
            mobileNumber = request.mobileNumber ?: user.mobileNumber
        )

        val savedUser = userRepository.save(updatedUser)
        return savedUser.toUserResponse()
    }

    private fun User.toUserResponse() = UserResponse(
        id = id,
        name = name,
        email = email,
        mobileNumber = mobileNumber,
        role = role,
        createdAt = createdAt
    )
}

