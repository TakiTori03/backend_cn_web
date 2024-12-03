package com.hust.Ecommerce.entities.order;

import java.math.BigDecimal;

import com.hust.Ecommerce.entities.product.Variant;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "order_variant")
public class OrderVariant {

    @EmbeddedId
    private OrderVariantKey orderVariantKey = new OrderVariantKey();

    @Column(name = "price", nullable = false, columnDefinition = "DECIMAL(15,5)")
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "amount", nullable = false, columnDefinition = "DECIMAL(15,5)")
    private BigDecimal amount;

    @ManyToOne
    @MapsId("variantId")
    @JoinColumn(name = "variant_id", nullable = false)
    private Variant variant;

    @ManyToOne()
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

}
