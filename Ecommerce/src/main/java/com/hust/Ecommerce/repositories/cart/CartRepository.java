package com.hust.Ecommerce.repositories.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.cart.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    @Query("SELECT c FROM Cart c JOIN c.user u WHERE u.name = :name AND c.status = 1")
    Optional<Cart> findByUsername(@Param("name") String name);

    @Query("SELECT c FROM Cart c JOIN c.user u WHERE u = user AND c.status = 1")
    Optional<Cart> findByUser(User user);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Cart c WHERE c.user = :user AND c.status = 1")
    boolean existsByUser(User user);
}
