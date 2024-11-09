package com.banking.current_account_api.service;


import com.banking.current_account_api.exception.AppException;
import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Customer;
import com.banking.current_account_api.model.Transaction;
import com.banking.current_account_api.model.enums.TransactionType;
import com.banking.current_account_api.repository.AccountRepository;
import com.banking.current_account_api.repository.CustomerRepository;
import com.banking.current_account_api.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionService transactionService;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl(accountRepository, transactionService, customerRepository);
    }

    @Test
    void createAccount_WithValidCustomerAndInitialCredit_ShouldCreateAccountAndTransaction() {
        // Given
        UUID customerId = UUID.randomUUID();
        BigDecimal initialCredit = new BigDecimal("100.00");

        Customer customer = Customer.builder()
                .id(customerId)
                .name("John")
                .surname("Doe")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(customerId)
                .balance(BigDecimal.ZERO)
                .transactions(new ArrayList<>())
                .build();

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .accountId(account.getId())
                .amount(initialCredit)
                .transactionType(TransactionType.CREDIT)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionService.createTransaction(any(), any(), any(), any())).thenReturn(transaction);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        Account result = accountService.createAccount(customerId, initialCredit);

        // Then
        assertNotNull(result);
        verify(transactionService).createTransaction(account.getId(), initialCredit, "Initial credit", TransactionType.CREDIT);
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(customerRepository).save(customer);
    }

    @Test
    void createAccount_WithNonExistentCustomer_ShouldThrowException() {
        // Given
        UUID customerId = UUID.randomUUID();
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(AppException.class,
                () -> accountService.createAccount(customerId, BigDecimal.ZERO));
    }

    @Test
    void createAccount_WithNegativeInitialCredit_ShouldThrowException() {
        // Given
        UUID customerId = UUID.randomUUID();
        BigDecimal initialCredit = new BigDecimal("-1.00");

        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(customerId, initialCredit));
    }


    @Test
    void createAccount_WithNullCustomerId_ShouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(null, BigDecimal.ZERO));
    }
}