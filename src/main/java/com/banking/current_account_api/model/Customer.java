package com.banking.current_account_api.model;

import lombok.Data;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Customer {
    private UUID id;
    private String name;
    private String surname;
    @Builder.Default
    private List<Account> accounts = new ArrayList<>();
}