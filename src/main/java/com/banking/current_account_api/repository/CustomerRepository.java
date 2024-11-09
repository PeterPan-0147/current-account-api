package com.banking.current_account_api.repository;


import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Customer;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CustomerRepository {
    private final Map<UUID, Customer> customers = new ConcurrentHashMap<>();

    public CustomerRepository() {
        // Add some sample customers for testing
        Customer customer = Customer.builder()
                .id(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .name("John")
                .surname("Doe")
                .build();
        customers.put(customer.getId(), customer);
    }
    public Customer save(Customer customer) {
        customers.put(customer.getId(), customer);
        return customer;
    }

    public Optional<Customer> findById(UUID id) {
        return Optional.ofNullable(customers.get(id));
    }
}