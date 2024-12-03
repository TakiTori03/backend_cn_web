package com.hust.Ecommerce.entities.cart;

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
public class CartVariantKey implements Serializable {
    @Column(name = "cart_id", nullable = false)
    Long cartId;

    @Column(name = "variant_id", nullable = false)
    Long variantId;
}
