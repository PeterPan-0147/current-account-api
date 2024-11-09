package com.banking.current_account_api.service;


import com.banking.current_account_api.dto.AccountDetailsResponse;
import com.banking.current_account_api.dto.TransactionRequest;
import com.banking.current_account_api.exception.AppException;
import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Customer;
import com.banking.current_account_api.model.Transaction;
import com.banking.current_account_api.model.enums.TransactionType;
import com.banking.current_account_api.repository.AccountRepository;
import com.banking.current_account_api.repository.CustomerRepository;
import com.banking.current_account_api.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

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

    @InjectMocks
    private AccountServiceImpl accountService;

    private UUID customerId;
    private UUID accountId;
    private Account testAccount;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        accountId = UUID.randomUUID();

        testAccount = Account.builder()
                .id(accountId)
                .customerId(customerId)
                .balance(BigDecimal.valueOf(1000))
                .build();

        testTransaction = Transaction.builder()
                .transactionType(TransactionType.CREDIT)
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100))
                .build();
    }

    @Test
    void createAccount_WithValidCustomerAndInitialCredit_ShouldCreateAccountAndTransaction() {
        // Given
        UUID customerID = UUID.randomUUID();
        BigDecimal initialCredit = new BigDecimal("100.00");

        Customer customer = Customer.builder()
                .id(customerID)
                .name("John")
                .surname("Doe")
                .accounts(new ArrayList<>())
                .build();

        Account account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(customerID)
                .balance(BigDecimal.ZERO)
                .transactions(new ArrayList<>())
                .build();

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .accountId(account.getId())
                .amount(initialCredit)
                .transactionType(TransactionType.CREDIT)
                .build();

        when(customerRepository.findById(customerID)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionService.createTransaction(any(), any(), any(), any())).thenReturn(transaction);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // When
        Account result = accountService.createAccount(customerID, initialCredit);

        // Then
        assertNotNull(result);
        verify(transactionService).createTransaction(account.getId(), initialCredit, "Initial credit", TransactionType.CREDIT);
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(customerRepository).save(customer);
    }

    @Test
    void createAccount_WithNonExistentCustomer_ShouldThrowException() {
        // Given
        UUID customerID = UUID.randomUUID();
        when(customerRepository.findById(customerID)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(AppException.class,
                () -> accountService.createAccount(customerID, BigDecimal.ZERO));
    }

    @Test
    void createAccount_WithNegativeInitialCredit_ShouldThrowException() {
        // Given
        UUID customerID = UUID.randomUUID();
        BigDecimal initialCredit = new BigDecimal("-1.00");

        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(customerID, initialCredit));
    }


    @Test
    void createAccount_WithNullCustomerId_ShouldThrowException() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> accountService.createAccount(null, BigDecimal.ZERO));
    }

    @Nested
    @DisplayName("getAllAccountsByCustomer Tests")
    class GetAllAccountsByCustomerTests {

        @Test
        @DisplayName("Should return all accounts for a customer")
        void shouldReturnAllAccountsForCustomer() {
            // Arrange
            Account account1 = Account.builder()
                    .id(UUID.randomUUID())
                    .balance(BigDecimal.valueOf(1000))
                    .build();

            Account account2 = Account.builder()
                    .id(UUID.randomUUID())
                    .balance(BigDecimal.valueOf(2000))
                    .build();

            List<Account> accounts = Arrays.asList(account1, account2);
            when(accountRepository.findByCustomerId(customerId)).thenReturn(accounts);

            // Act
            List<AccountDetailsResponse> result = accountService.getAllAccountsByCustomer(customerId);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(account1.getId(), result.get(0).getAccountId());
            assertEquals(account1.getBalance(), result.get(0).getBalance());
            assertEquals(account2.getId(), result.get(1).getAccountId());
            assertEquals(account2.getBalance(), result.get(1).getBalance());
        }

        @Test
        @DisplayName("Should return empty list when customer has no accounts")
        void shouldReturnEmptyListWhenNoAccounts() {
            // Arrange
            when(accountRepository.findByCustomerId(customerId)).thenReturn(List.of());

            // Act
            List<AccountDetailsResponse> result = accountService.getAllAccountsByCustomer(customerId);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("getTransactionsByAccount Tests")
    class GetTransactionsByAccountTests {

        @Test
        @DisplayName("Should return transactions for valid account")
        void shouldReturnTransactionsForValidAccount() {
            // Arrange
            List<Transaction> transactions = Arrays.asList(
                    testTransaction,
                    new Transaction()
            );
            testAccount.setTransactions(transactions);
            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

            // Act
            List<Transaction> result = accountService.getTransactionsByAccount(accountId);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(transactions, result);
        }

        @Test
        @DisplayName("Should throw exception when account not found")
        void shouldThrowExceptionWhenAccountNotFound() {
            // Arrange
            when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

            // Act & Assert
            AppException exception = assertThrows(AppException.class,
                    () -> accountService.getTransactionsByAccount(accountId));
            assertEquals("404", exception.getCode());
            assertEquals("Account not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("performTransaction Tests")
    class PerformTransactionTests {

        @Test
        @DisplayName("Should perform credit transaction successfully")
        void shouldPerformCreditTransactionSuccessfully() {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(100));
            request.setType(TransactionType.CREDIT);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.createTransaction(eq(accountId), any(), any(), any()))
                    .thenReturn(testTransaction);
            when(accountRepository.save(any())).thenReturn(testAccount);

            // Act
            AccountDetailsResponse result = accountService.performTransaction(accountId, request);

            // Assert
            assertNotNull(result);
            assertEquals(accountId, result.getAccountId());
            assertEquals(BigDecimal.valueOf(1100), result.getBalance());
            verify(accountRepository).save(any());
        }

        @Test
        @DisplayName("Should perform debit transaction successfully")
        void shouldPerformDebitTransactionSuccessfully() {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(100));
            request.setType(TransactionType.DEBIT);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
            when(transactionService.createTransaction(eq(accountId), any(), any(), any()))
                    .thenReturn(testTransaction);
            when(accountRepository.save(any())).thenReturn(testAccount);

            // Act
            AccountDetailsResponse result = accountService.performTransaction(accountId, request);

            // Assert
            assertNotNull(result);
            assertEquals(accountId, result.getAccountId());
            assertEquals(BigDecimal.valueOf(900), result.getBalance());
            verify(accountRepository).save(any());
        }

        @Test
        @DisplayName("Should throw exception when insufficient funds for debit")
        void shouldThrowExceptionWhenInsufficientFunds() {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(2000));
            request.setType(TransactionType.DEBIT);

            when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));

            // Act & Assert
            AppException exception = assertThrows(AppException.class,
                    () -> accountService.performTransaction(accountId, request));
            assertEquals("400", exception.getCode());
            assertEquals("Insufficient funds", exception.getMessage());
            verify(accountRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when account not found")
        void shouldThrowExceptionWhenAccountNotFoundForTransaction() {
            // Arrange
            TransactionRequest request = new TransactionRequest();
            request.setAmount(BigDecimal.valueOf(100));
            request.setType(TransactionType.CREDIT);

            when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

            // Act & Assert
            AppException exception = assertThrows(AppException.class,
                    () -> accountService.performTransaction(accountId, request));
            assertEquals("404", exception.getCode());
            assertEquals("Account not found", exception.getMessage());
            verify(accountRepository, never()).save(any());
        }
    }
}