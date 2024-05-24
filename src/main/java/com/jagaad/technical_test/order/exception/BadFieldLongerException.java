package com.jagaad.technical_test.order.exception;

public class BadFieldLongerException extends IllegalArgumentException{
    public BadFieldLongerException(String message) {
        super(message);
    }
}
