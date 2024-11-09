package com.banking.current_account_api.dto;

import com.banking.current_account_api.model.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {
    private BigDecimal amount;
    private TransactionType type;
}