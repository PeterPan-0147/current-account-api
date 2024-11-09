package com.banking.current_account_api.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends RuntimeException {
    private final String message;
    private final String code;
    public AppException(String message, String code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
