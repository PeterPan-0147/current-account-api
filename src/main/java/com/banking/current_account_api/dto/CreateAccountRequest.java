package com.banking.current_account_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateAccountRequest {
    private UUID customerId;
    private BigDecimal initialCredit;
}