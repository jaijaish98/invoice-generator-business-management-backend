package com.example.businessapp.controller

import com.example.businessapp.dto.*
import com.example.businessapp.service.QuotationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse as SwaggerApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/quotations")
@Tag(name = "Quotations", description = "Quotation management endpoints")
@SecurityRequirement(name = "bearerAuth")
class QuotationController(
    private val quotationService: QuotationService
) {
    
    @PostMapping
    @Operation(summary = "Create quotation", description = "Create a new quotation")
    @SwaggerApiResponse(responseCode = "201", description = "Quotation created successfully")
    @SwaggerApiResponse(responseCode = "400", description = "Invalid input")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun createQuotation(@Valid @RequestBody request: CreateQuotationRequest): ResponseEntity<ApiResponse<QuotationResponse>> {
        val quotation = quotationService.createQuotation(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                success = true,
                data = quotation,
                message = "Quotation created successfully"
            )
        )
    }
    
    @GetMapping
    @Operation(summary = "Get all quotations", description = "Get all quotations for authenticated user with pagination")
    @SwaggerApiResponse(responseCode = "200", description = "Quotations retrieved successfully")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getQuotations(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<QuotationResponse>>> {
        val quotations = quotationService.getQuotations(pageable)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = quotations,
                message = "Quotations retrieved successfully"
            )
        )
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get quotation by ID", description = "Retrieve a specific quotation by ID")
    @SwaggerApiResponse(responseCode = "200", description = "Quotation retrieved successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Quotation not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getQuotationById(@PathVariable id: UUID): ResponseEntity<ApiResponse<QuotationResponse>> {
        val quotation = quotationService.getQuotationById(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = quotation,
                message = "Quotation retrieved successfully"
            )
        )
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update quotation", description = "Update an existing quotation")
    @SwaggerApiResponse(responseCode = "200", description = "Quotation updated successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Quotation not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun updateQuotation(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateQuotationRequest
    ): ResponseEntity<ApiResponse<QuotationResponse>> {
        val quotation = quotationService.updateQuotation(id, request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = quotation,
                message = "Quotation updated successfully"
            )
        )
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete quotation", description = "Delete a quotation")
    @SwaggerApiResponse(responseCode = "200", description = "Quotation deleted successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Quotation not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun deleteQuotation(@PathVariable id: UUID): ResponseEntity<ApiResponse<Nothing>> {
        quotationService.deleteQuotation(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = null,
                message = "Quotation deleted successfully"
            )
        )
    }
}

