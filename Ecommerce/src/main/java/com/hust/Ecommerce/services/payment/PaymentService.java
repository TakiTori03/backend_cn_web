package com.hust.Ecommerce.services.payment;

import com.hust.Ecommerce.dtos.payment.PaymentRequest;
import com.hust.Ecommerce.dtos.payment.PaymentResponse;

public interface PaymentService {
    PaymentResponse init(PaymentRequest request);
}
