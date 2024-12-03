package com.hust.Ecommerce.dtos.cashbook;

import com.hust.Ecommerce.entities.cashbook.PaymentMethodType;

import lombok.Data;

@Data
public class ClientPaymentMethodResponse {
    private Long paymentMethodId;
    private String paymentMethodName;
    private PaymentMethodType paymentMethodCode;
}
