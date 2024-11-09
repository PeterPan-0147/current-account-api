package com.banking.current_account_api.service;

import com.banking.current_account_api.dto.AccountDetailsResponse;
import com.banking.current_account_api.dto.TransactionRequest;
import com.banking.current_account_api.model.Account;
import com.banking.current_account_api.model.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    Account createAccount(UUID customerId, BigDecimal initialCredit);
    Account getAccount(UUID accountId);
    List<Account> getAccountByCustomerId(UUID customerId);
    List<Transaction> getTransactionsByAccount(UUID accountId);
    AccountDetailsResponse performTransaction(UUID accountId, TransactionRequest request);
    List<AccountDetailsResponse> getAllAccountsByCustomer(UUID customerId);

}
