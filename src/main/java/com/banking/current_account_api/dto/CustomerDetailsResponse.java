package com.banking.current_account_api.dto;

import com.banking.current_account_api.model.Transaction;
import lombok.Data;
import lombok.Builder;
import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
public class CustomerDetailsResponse {
    private String name;
    private String surname;
    private BigDecimal balance;
    private List<Transaction> transactions;
}