package com.hust.Ecommerce.exceptions.payload;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException(String message) {
        super(message);
    }

}
