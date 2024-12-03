package com.hust.Ecommerce.entities.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hust.Ecommerce.entities.BaseEntity;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.cashbook.PaymentMethodType;
import com.hust.Ecommerce.entities.cashbook.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    // trang thai order (1) : pending, (2): confirmed, (3): shipping, (4):
    // delivered, (5): cancelled, (6): finished
    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @Column(name = "total_amount", nullable = false, columnDefinition = "DECIMAL(15,5)")
    private BigDecimal totalAmount;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "tax", nullable = false, columnDefinition = "DECIMAL(15,5)")
    private BigDecimal tax;

    @Column(name = "shipping_cost", nullable = false, columnDefinition = "DECIMAL(15,5)")
    private BigDecimal shippingCost;

    @Column(name = "total_pay", nullable = false, columnDefinition = "DECIMAL(15,5)")
    private BigDecimal totalPay;

    @Column(name = "payment_method_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethodType paymentMethodType;

    // 2 trạng thái: (0) Chưa thanh toán, (1) Đã thanh toán
    @Column(name = "payment_status", nullable = false, columnDefinition = "TINYINT")
    private Integer paymentStatus;

    @Column(name = "vnpay_order_id")
    private String vnPayOrderId;

    @Column(name = "vnpay_order_status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus vnPayOrderStatus;

    // shipping info
    @Column(name = "to_name", nullable = false)
    private String toName;

    @Column(name = "to_phone", nullable = false)
    private String toPhone;

    @Column(name = "to_address", nullable = false)
    private String toAddress;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<OrderVariant> orderVariants = new ArrayList<>();

}
