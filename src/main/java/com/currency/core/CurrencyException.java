package com.currency.core;

public class CurrencyException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4011352194370862283L;

    public CurrencyException(String message) {
        super(message);
    }

    public CurrencyException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
