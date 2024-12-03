package com.hust.Ecommerce.dtos.client.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class ClientSimpleOrderResponse {
    private Long orderId;
    private Instant orderCreatedAt;
    private String orderCode;
    private Integer orderStatus;
    private BigDecimal orderTotalPay;
    private List<ClientOrderVariantResponse> orderItems;
    private Integer orderPaymentStatus;
}