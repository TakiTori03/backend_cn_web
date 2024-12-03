package com.hust.Ecommerce.repositories.cashbook;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hust.Ecommerce.entities.cashbook.PaymentMethod;

public interface PaymentMethodRepository
        extends JpaRepository<PaymentMethod, Long>, JpaSpecificationExecutor<PaymentMethod> {

    List<PaymentMethod> findAllByStatus(Integer status);

}
