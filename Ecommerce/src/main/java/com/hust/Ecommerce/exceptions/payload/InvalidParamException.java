package com.hust.Ecommerce.exceptions.payload;

public class InvalidParamException extends RuntimeException {
    public InvalidParamException(String message) {
        super(message);
    }
}
