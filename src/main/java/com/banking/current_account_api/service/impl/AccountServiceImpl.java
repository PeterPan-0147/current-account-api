package com.banking.current_account_api.service.impl;


import com.banking.current_account_api.dto.AccountDetailsResponse;
import com.banking.current_account_api.dto.TransactionRequest;
import com.banking.current_account_api.exception.AppException;
import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Customer;
import com.banking.current_account_api.model.Transaction;
import com.banking.current_account_api.model.enums.TransactionType;
import com.banking.current_account_api.repository.AccountRepository;
import com.banking.current_account_api.repository.CustomerRepository;
import com.banking.current_account_api.service.AccountService;
import com.banking.current_account_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final CustomerRepository customerRepository;
    @Override
    public Account createAccount(UUID customerId, BigDecimal initialCredit) {
        // Validate input
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }
        if (initialCredit == null) {
            throw new IllegalArgumentException("Initial credit cannot be null");
        }
        if (initialCredit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial credit cannot be negative");
        }

        // Find customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException("","Customer with ID " + customerId + " not found"));

        // Create new account
        Account account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(customerId)
                .balance(BigDecimal.ZERO)
                .build();
        account = accountRepository.save(account);

        if (initialCredit.compareTo(BigDecimal.ZERO) > 0) {
            Transaction transaction = transactionService.createTransaction(
                    account.getId(),
                    initialCredit,
                    "Initial credit", TransactionType.CREDIT
            );

            account.getTransactions().add(transaction);
            account.setBalance(initialCredit);
            account = accountRepository.save(account);
        }

        customer.getAccounts().add(account);
        customerRepository.save(customer);

        return account;
    }

    @Override
    public List<AccountDetailsResponse> getAllAccountsByCustomer(UUID customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        return accounts.stream()
                .map(account -> AccountDetailsResponse.builder()
                        .accountId(account.getId())
                        .balance(account.getBalance())
                        .transactions(account.getTransactions())
                        .build()).toList();
    }
    @Override
    public List<Transaction> getTransactionsByAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("404","Account not found"));
        return account.getTransactions();
    }
    @Override
    public AccountDetailsResponse performTransaction(UUID accountId, TransactionRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("404","Account not found"));

        BigDecimal amount = request.getAmount();
        if (request.getType() == TransactionType.DEBIT) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new AppException("400","Insufficient funds");
            }
            account.setBalance(account.getBalance().subtract(amount));
        } else if (request.getType() == TransactionType.CREDIT) {
            account.setBalance(account.getBalance().add(amount));
        }

        Transaction transaction = transactionService.createTransaction(
                accountId, amount,request.getType().name(), request.getType()
        );
        account.getTransactions().add(transaction);
        accountRepository.save(account);

        return AccountDetailsResponse.builder()
                .accountId(account.getId())
                .balance(account.getBalance())
                .transactions(account.getTransactions())
                .build();
    }
    @Override
    public Account getAccount(UUID accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }

        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("404","Account with ID " + accountId + " not found"));
    }
    @Override
    public List<Account> getAccountByCustomerId(UUID customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        return accountRepository.findByCustomerId(customerId);
    }
}