package com.banking.current_account_api.service;

import com.banking.current_account_api.dto.CreateCustomerRequest;
import com.banking.current_account_api.dto.CustomerDetailsResponse;
import com.banking.current_account_api.model.Customer;

import java.util.UUID;

public interface CustomerService {
    CustomerDetailsResponse getCustomerDetails(UUID customerId);

    Customer createCustomer(CreateCustomerRequest request);
}
