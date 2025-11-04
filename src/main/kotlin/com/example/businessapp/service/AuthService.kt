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
        if (userRepository.existsByEmail(request.email)) {
            throw BadRequestException("Email already exists")
        }

        val user = User(
            name = request.fullName,
            email = request.email,
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
    
    private fun User.toUserResponse() = UserResponse(
        id = id,
        name = name,
        email = email,
        role = role,
        createdAt = createdAt
    )
}

