package com.hust.Ecommerce.dtos.cashbook;

import java.time.Instant;

import com.hust.Ecommerce.entities.cashbook.PaymentMethodType;

import lombok.Data;

@Data
public class PaymentMethodResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private PaymentMethodType code;
    private Integer status;
}