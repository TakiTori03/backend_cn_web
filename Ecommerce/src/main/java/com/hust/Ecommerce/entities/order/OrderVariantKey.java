package com.hust.Ecommerce.entities.order;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
public class OrderVariantKey implements Serializable {
    @Column(name = "order_id", nullable = false)
    Long orderId;

    @Column(name = "variant_id", nullable = false)
    Long variantId;
}
