package com.banking.current_account_api.controller;


import com.banking.current_account_api.dto.CreateCustomerRequest;
import com.banking.current_account_api.dto.CustomerDetailsResponse;
import com.banking.current_account_api.model.Customer;
import com.banking.current_account_api.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Information", description = "APIs for retrieving customer information")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{customerId}")
    @Operation(summary = "Get customer details", description = "Retrieves customer details including account balance and transactions")
    public ResponseEntity<CustomerDetailsResponse> getCustomerDetails(@PathVariable UUID customerId) {
        CustomerDetailsResponse response = customerService.getCustomerDetails(customerId);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Create a new customer", description = "Creates a new customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody CreateCustomerRequest request) {
        Customer customer = customerService.createCustomer(request);
        return ResponseEntity.ok(customer);
    }
}