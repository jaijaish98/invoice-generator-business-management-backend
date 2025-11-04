package com.example.businessapp.controller

import com.example.businessapp.dto.*
import com.example.businessapp.service.EwayBillService
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
@RequestMapping("/api/v1/eway-bills")
@Tag(name = "E-Way Bills", description = "E-Way Bill management endpoints")
@SecurityRequirement(name = "bearerAuth")
class EwayBillController(
    private val ewayBillService: EwayBillService
) {
    
    @PostMapping
    @Operation(summary = "Create e-way bill", description = "Create a new e-way bill")
    @SwaggerApiResponse(responseCode = "201", description = "E-way bill created successfully")
    @SwaggerApiResponse(responseCode = "400", description = "Invalid input or bill number already exists")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun createEwayBill(@Valid @RequestBody request: CreateEwayBillRequest): ResponseEntity<ApiResponse<EwayBillResponse>> {
        val ewayBill = ewayBillService.createEwayBill(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse(
                success = true,
                data = ewayBill,
                message = "E-way bill created successfully"
            )
        )
    }
    
    @GetMapping
    @Operation(summary = "Get all e-way bills", description = "Get all e-way bills for authenticated user with pagination")
    @SwaggerApiResponse(responseCode = "200", description = "E-way bills retrieved successfully")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getEwayBills(
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<EwayBillResponse>>> {
        val ewayBills = ewayBillService.getEwayBills(pageable)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = ewayBills,
                message = "E-way bills retrieved successfully"
            )
        )
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get e-way bill by ID", description = "Retrieve a specific e-way bill by ID")
    @SwaggerApiResponse(responseCode = "200", description = "E-way bill retrieved successfully")
    @SwaggerApiResponse(responseCode = "404", description = "E-way bill not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getEwayBillById(@PathVariable id: UUID): ResponseEntity<ApiResponse<EwayBillResponse>> {
        val ewayBill = ewayBillService.getEwayBillById(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = ewayBill,
                message = "E-way bill retrieved successfully"
            )
        )
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update e-way bill", description = "Update an existing e-way bill")
    @SwaggerApiResponse(responseCode = "200", description = "E-way bill updated successfully")
    @SwaggerApiResponse(responseCode = "404", description = "E-way bill not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun updateEwayBill(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateEwayBillRequest
    ): ResponseEntity<ApiResponse<EwayBillResponse>> {
        val ewayBill = ewayBillService.updateEwayBill(id, request)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = ewayBill,
                message = "E-way bill updated successfully"
            )
        )
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete e-way bill", description = "Delete an e-way bill")
    @SwaggerApiResponse(responseCode = "200", description = "E-way bill deleted successfully")
    @SwaggerApiResponse(responseCode = "404", description = "E-way bill not found")
    @SwaggerApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun deleteEwayBill(@PathVariable id: UUID): ResponseEntity<ApiResponse<Nothing>> {
        ewayBillService.deleteEwayBill(id)
        return ResponseEntity.ok(
            ApiResponse(
                success = true,
                data = null,
                message = "E-way bill deleted successfully"
            )
        )
    }
}

