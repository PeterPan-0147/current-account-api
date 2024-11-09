package com.banking.current_account_api.repository;


import com.banking.current_account_api.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TransactionRepository {
    private final Map<UUID, Transaction> transactions = new ConcurrentHashMap<>();

    public Transaction save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
        return transaction;
    }

    public Optional<Transaction> findById(UUID id) {
        return Optional.ofNullable(transactions.get(id));
    }
}