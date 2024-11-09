package com.banking.current_account_api.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateCustomerRequest {

    private String firstName;
    private String surname;
    private UUID id;
}
