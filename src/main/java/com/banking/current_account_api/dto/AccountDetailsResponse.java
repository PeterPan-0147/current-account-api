package com.banking.current_account_api.dto;

import com.banking.current_account_api.model.Transaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class AccountDetailsResponse {
    private UUID accountId;
    private BigDecimal balance;
    private List<Transaction> transactions;
}