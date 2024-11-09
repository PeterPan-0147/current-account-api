package com.banking.current_account_api.service.impl;


import com.banking.current_account_api.dto.CreateCustomerRequest;
import com.banking.current_account_api.dto.CustomerDetailsResponse;
import com.banking.current_account_api.exception.AppException;
import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Customer;
import com.banking.current_account_api.model.Transaction;
import com.banking.current_account_api.repository.AccountRepository;
import com.banking.current_account_api.repository.CustomerRepository;
import com.banking.current_account_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    @Override
    public CustomerDetailsResponse getCustomerDetails(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException("404", "Customer not found"));

        List<Account> accounts = accountRepository.findByCustomerId(customerId);

        // Calculate total balance by summing account balances
        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Map accounts to AccountDetails DTOs
        List<CustomerDetailsResponse.AccountDetails> accountDetails = accounts.stream()
                .map(this::mapToAccountDetails)
                .toList();

        return CustomerDetailsResponse.builder()
                .name(customer.getName())
                .surname(customer.getSurname())
                .accounts(accountDetails)
                .totalBalance(totalBalance)
                .build();
    }

    private CustomerDetailsResponse.AccountDetails mapToAccountDetails(Account account) {
        return CustomerDetailsResponse.AccountDetails.builder()
                .accountId(account.getId())
                .balance(account.getBalance())
                .transactions(mapToTransactionDetails(account.getTransactions()))
                .build();
    }

    private List<CustomerDetailsResponse.TransactionDetails> mapToTransactionDetails(List<Transaction> transactions) {
        return transactions.stream()
                .map(transaction -> CustomerDetailsResponse.TransactionDetails.builder()
                        .amount(transaction.getAmount())
                        .timestamp(transaction.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        .build())
                .toList();
    }

    @Override
    public Customer createCustomer(CreateCustomerRequest request) {
        Optional<Customer> optionalCustomer = customerRepository.findById(request.getId());
        if(optionalCustomer.isEmpty()){
            Customer customer = Customer.builder()
                    .id(request.getId())
                    .name(request.getFirstName())
                    .surname(request.getSurname())
                    .build();
            customerRepository.save(customer);
            return customer;
        }
        throw new AppException("409","Customer with Id already exist");
    }
}