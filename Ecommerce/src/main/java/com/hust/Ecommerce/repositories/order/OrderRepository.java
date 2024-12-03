package com.hust.Ecommerce.repositories.order;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hust.Ecommerce.entities.order.Order;

import io.github.perplexhub.rsql.RSQLJPASupport;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    default Page<Order> findAllByEmail(String email, String sort, String filter, Pageable pageable) {
        Specification<Order> sortable = RSQLJPASupport.toSort(sort);
        Specification<Order> filterable = RSQLJPASupport.toSpecification(filter);
        Specification<Order> usernameSpec = RSQLJPASupport.toSpecification("user.email==" + email);
        return findAll(sortable.and(filterable).and(usernameSpec), pageable);
    }

    Optional<Order> findByCode(String code);

    Optional<Order> findByVnPayOrderId(String vnPayOrderId);

    @Query("SELECT COUNT(o.id) FROM Order o ")
    int countByOrderId();

}
