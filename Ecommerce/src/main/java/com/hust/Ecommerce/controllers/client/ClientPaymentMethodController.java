package com.hust.Ecommerce.controllers.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.CollectionWrapper;
import com.hust.Ecommerce.dtos.cashbook.ClientPaymentMethodResponse;
import com.hust.Ecommerce.mappers.cashbook.PaymentMethodMapper;
import com.hust.Ecommerce.repositories.cashbook.PaymentMethodRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/client-api/payment-methods")
@AllArgsConstructor
public class ClientPaymentMethodController {

    private PaymentMethodRepository paymentMethodRepository;
    private PaymentMethodMapper paymentMethodMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllPaymentMethods() {
        List<ClientPaymentMethodResponse> clientPaymentMethodResponses = paymentMethodMapper
                .entityToClientResponse(paymentMethodRepository.findAllByStatus(1));
        return ResponseEntity.ok(ApiResponse.<CollectionWrapper<ClientPaymentMethodResponse>>builder().success(true)
                .payload(CollectionWrapper.of(clientPaymentMethodResponses)).build());
    }

}
