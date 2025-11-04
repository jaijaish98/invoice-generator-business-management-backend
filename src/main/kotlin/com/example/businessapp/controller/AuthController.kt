package com.example.businessapp.controller

import com.example.businessapp.dto.*
import com.example.businessapp.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication and user management endpoints")
class AuthController(
    private val authService: AuthService
) {
    
    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Create a new user account")
    @SwaggerApiResponse(responseCode = "201", description = "User registered successfully")
    @SwaggerApiResponse(responseCode = "400", description = "Invalid input or email already exists")
    fun register(@Valid @RequestBody request: RegisterRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val authResponse = authService.register(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                success = true,
                data = authResponse,
                message = "User registered successfully"
            )
        )
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    @SwaggerApiResponse(responseCode = "200", description = "Login successful")
    @SwaggerApiResponse(responseCode = "401", description = "Invalid credentials")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<AuthResponse>> {
        val authResponse = authService.login(request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = authResponse,
                message = "Login successful"
            )
        )
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get authenticated user information")
    @SwaggerApiResponse(responseCode = "200", description = "User information retrieved")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    fun getCurrentUser(): ResponseEntity<ApiResponse<UserResponse>> {
        val user = authService.getCurrentUser()
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = user,
                message = "User information retrieved successfully"
            )
        )
    }

    @PutMapping("/change-password")
    @Operation(summary = "Change password", description = "Change password for authenticated user")
    @SwaggerApiResponse(responseCode = "200", description = "Password changed successfully")
    @SwaggerApiResponse(responseCode = "400", description = "Invalid current password or validation error")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    fun changePassword(@Valid @RequestBody request: ChangePasswordRequest): ResponseEntity<ApiResponse<UserResponse>> {
        val user = authService.changePassword(request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = user,
                message = "Password changed successfully"
            )
        )
    }

    @PutMapping("/account")
    @Operation(summary = "Update account settings", description = "Update user account information (name, email, mobile number, password)")
    @SwaggerApiResponse(responseCode = "200", description = "Account updated successfully")
    @SwaggerApiResponse(responseCode = "400", description = "Invalid input or validation error")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    fun updateAccount(@Valid @RequestBody request: UpdateAccountRequest): ResponseEntity<ApiResponse<UserResponse>> {
        val user = authService.updateAccount(request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = user,
                message = "Account updated successfully"
            )
        )
    }
}

