package com.banking.current_account_api.model;

import lombok.Data;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Account {
    private UUID id;
    private UUID customerId;
    private BigDecimal balance;
    @Builder.Default
    private List<Transaction> transactions = new ArrayList<>();
}