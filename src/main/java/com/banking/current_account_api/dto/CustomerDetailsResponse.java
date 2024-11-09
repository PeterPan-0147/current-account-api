package com.banking.current_account_api.dto;

import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Data
@Builder
public class CustomerDetailsResponse {
    private String name;
    private String surname;
    private BigDecimal totalBalance;
    private List<AccountDetails> accounts;

    @Data
    @Builder
    public static class AccountDetails {
        private UUID accountId;
        private BigDecimal balance;
        private List<TransactionDetails> transactions;
    }

    @Data
    @Builder
    public static class TransactionDetails {
        private BigDecimal amount;
        private String timestamp;
    }
}