package org.sid.comptecqrses.commonapi.exceptions;

public class BalanceNotSufficientException extends RuntimeException {
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
