package com.hust.Ecommerce.repositories.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hust.Ecommerce.entities.cart.CartVariant;
import com.hust.Ecommerce.entities.cart.CartVariantKey;

public interface CartVariantRepository extends JpaRepository<CartVariant, CartVariantKey>,
        JpaSpecificationExecutor<CartVariant> {
}
