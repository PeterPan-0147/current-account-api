package com.banking.current_account_api.service.impl;


import com.banking.current_account_api.model.Transaction;
import com.banking.current_account_api.model.enums.TransactionType;
import com.banking.current_account_api.repository.TransactionRepository;
import com.banking.current_account_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    @Override
    public Transaction createTransaction(UUID accountId, BigDecimal amount, String description, TransactionType type) {
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .accountId(accountId)
                .amount(amount)
                .description(description)
                .timestamp(LocalDateTime.now())
                .transactionType(type)
                .build();

        return transactionRepository.save(transaction);
    }
}