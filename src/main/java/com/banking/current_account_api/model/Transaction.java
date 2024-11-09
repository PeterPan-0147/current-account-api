package com.banking.current_account_api.model;

import com.banking.current_account_api.model.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private String description;
    private LocalDateTime timestamp;
    private TransactionType transactionType;
}