package com.hust.Ecommerce.repositories.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hust.Ecommerce.entities.product.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long>, JpaSpecificationExecutor<Brand> {
    @Query("SELECT COUNT(b.id) FROM Brand b")
    int countByBrandId();
}
