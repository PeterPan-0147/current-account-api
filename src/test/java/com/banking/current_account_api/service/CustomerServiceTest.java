package com.banking.current_account_api.service;


import com.banking.current_account_api.dto.CustomerDetailsResponse;
import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Customer;
import com.banking.current_account_api.repository.AccountRepository;
import com.banking.current_account_api.repository.CustomerRepository;
import com.banking.current_account_api.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AccountRepository accountRepository;

    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerServiceImpl(customerRepository, accountRepository);
    }

    @Test
    void getCustomerDetails_ShouldReturnCorrectDetails() {
        // Given
        UUID customerId = UUID.randomUUID();
        Customer customer = Customer.builder()
                .id(customerId)
                .name("John")
                .surname("Doe")
                .build();

        Account account = Account.builder()
                .id(UUID.randomUUID())
                .customerId(customerId)
                .balance(new BigDecimal("100.00"))
                .transactions(new ArrayList<>())
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomerId(customerId)).thenReturn(Collections.singletonList(account));

        // When
        CustomerDetailsResponse response = customerService.getCustomerDetails(customerId);

        // Then
        assertNotNull(response);
        assertEquals("John", response.getName());
        assertEquals("Doe", response.getSurname());
        assertEquals(new BigDecimal("100.00"), response.getBalance());
        assertNotNull(response.getTransactions());
    }
}