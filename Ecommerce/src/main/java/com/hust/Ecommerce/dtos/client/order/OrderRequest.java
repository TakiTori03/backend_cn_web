package com.hust.Ecommerce.dtos.client.order;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.lang.Nullable;

import com.hust.Ecommerce.entities.cashbook.PaymentMethodType;

import lombok.Data;

@Data
public class OrderRequest {
    private String code;
    private Integer status;
    private String toName;
    private String toPhone;
    private String toAddress;

    @Nullable
    private String note;
    private Long userId;
    private List<OrderVariantRequest> orderVariants;
    private BigDecimal totalAmount;
    private BigDecimal tax;
    private BigDecimal shippingCost;
    private BigDecimal totalPay;
    private PaymentMethodType paymentMethodType;
    private Integer paymentStatus;
}
