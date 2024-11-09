package com.banking.current_account_api.controller;


import com.banking.current_account_api.dto.AccountDetailsResponse;
import com.banking.current_account_api.dto.CreateAccountRequest;
import com.banking.current_account_api.dto.TransactionRequest;
import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Transaction;
import com.banking.current_account_api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "APIs for managing customer accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @Operation(summary = "Create a new account", description = "Creates a new account for an existing customer with optional initial credit")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(request.getCustomerId(), request.getInitialCredit());
        return ResponseEntity.ok(account);
    }

    @Operation(summary = "Get all accounts for a specific customer",
            description = "Fetches a list of all accounts associated with the specified customer")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountDetailsResponse>> getAllCustomerAccounts(@PathVariable UUID customerId) {
        List<AccountDetailsResponse> accounts = accountService.getAllAccountsByCustomer(customerId);
        return ResponseEntity.ok(accounts);
    }

    @Operation(summary = "Get all transactions in a particular account",
            description = "Fetches all transactions associated with a specific account")
    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable UUID accountId) {
        List<Transaction> transactions = accountService.getTransactionsByAccount(accountId);
        return ResponseEntity.ok(transactions);
    }

    @Operation(summary = "Perform a transaction (credit or debit) on an account",
            description = "Performs a deposit or withdrawal transaction on the specified account")
    @PostMapping("/{accountId}/transaction")
    public ResponseEntity<AccountDetailsResponse> performTransaction(
            @PathVariable UUID accountId,
            @RequestBody TransactionRequest request) {
        AccountDetailsResponse account = accountService.performTransaction(accountId, request);
        return ResponseEntity.ok(account);
    }
}