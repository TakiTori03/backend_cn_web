package com.hust.Ecommerce.repositories.general;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hust.Ecommerce.entities.general.Image;

public interface ImageRepository extends JpaRepository<Image, Long>, JpaSpecificationExecutor<Image> {

    List<Image> findByProductId(Long productId);
}
