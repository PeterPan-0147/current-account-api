package com.banking.current_account_api.service;

import com.banking.current_account_api.model.Transaction;
import com.banking.current_account_api.model.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public interface TransactionService {
    Transaction createTransaction(UUID accountId, BigDecimal amount, String description, TransactionType type);
}
