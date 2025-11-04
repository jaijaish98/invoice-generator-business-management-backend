package com.example.businessapp.controller

import com.example.businessapp.dto.*
import com.example.businessapp.service.InvoiceService
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
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoices", description = "Invoice management endpoints")
@SecurityRequirement(name = "bearerAuth")
class InvoiceController(
    private val invoiceService: InvoiceService
) {
    
    @PostMapping
    @Operation(summary = "Create invoice", description = "Create a new invoice")
    @SwaggerApiResponse(responseCode = "201", description = "Invoice created successfully")
    @SwaggerApiResponse(responseCode = "400", description = "Invalid input")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun createInvoice(@Valid @RequestBody request: CreateInvoiceRequest): ResponseEntity<ApiResponse<InvoiceResponse>> {
        val invoice = invoiceService.createInvoice(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                success = true,
                data = invoice,
                message = "Invoice created successfully"
            )
        )
    }
    
    @GetMapping
    @Operation(summary = "Get all invoices", description = "Get all invoices for authenticated user with pagination")
    @SwaggerApiResponse(responseCode = "200", description = "Invoices retrieved successfully")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getInvoices(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<InvoiceResponse>>> {
        val invoices = invoiceService.getInvoices(pageable)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = invoices,
                message = "Invoices retrieved successfully"
            )
        )
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID", description = "Retrieve a specific invoice by ID")
    @SwaggerApiResponse(responseCode = "200", description = "Invoice retrieved successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Invoice not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getInvoiceById(@PathVariable id: UUID): ResponseEntity<ApiResponse<InvoiceResponse>> {
        val invoice = invoiceService.getInvoiceById(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = invoice,
                message = "Invoice retrieved successfully"
            )
        )
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update invoice", description = "Update an existing invoice")
    @SwaggerApiResponse(responseCode = "200", description = "Invoice updated successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Invoice not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun updateInvoice(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateInvoiceRequest
    ): ResponseEntity<ApiResponse<InvoiceResponse>> {
        val invoice = invoiceService.updateInvoice(id, request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = invoice,
                message = "Invoice updated successfully"
            )
        )
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice", description = "Delete an invoice")
    @SwaggerApiResponse(responseCode = "200", description = "Invoice deleted successfully")
    @SwaggerApiResponse(responseCode = "404", description = "Invoice not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun deleteInvoice(@PathVariable id: UUID): ResponseEntity<ApiResponse<Nothing>> {
        invoiceService.deleteInvoice(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = null,
                message = "Invoice deleted successfully"
            )
        )
    }
}

