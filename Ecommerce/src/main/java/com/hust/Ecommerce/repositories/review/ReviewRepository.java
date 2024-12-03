package com.hust.Ecommerce.repositories.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hust.Ecommerce.entities.review.Review;

import io.github.perplexhub.rsql.RSQLJPASupport;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {

    default Page<Review> findAllByProductSlug(String productSlug, String sort, String filter, Pageable pageable) {
        Specification<Review> sortable = RSQLJPASupport.toSort(sort);
        Specification<Review> filterable = RSQLJPASupport.toSpecification(filter);
        Specification<Review> productIdSpec = RSQLJPASupport.toSpecification("product.slug==" + productSlug);
        return findAll(sortable.and(filterable).and(productIdSpec), pageable);
    }

    default Page<Review> findAllByEmail(String email, String sort, String filter, Pageable pageable) {
        Specification<Review> sortable = RSQLJPASupport.toSort(sort);
        Specification<Review> filterable = RSQLJPASupport.toSpecification(filter);
        Specification<Review> usernameSpec = RSQLJPASupport.toSpecification("user.email==" + email);
        return findAll(sortable.and(filterable).and(usernameSpec), pageable);
    }

    // tinh trung binh chua lam tron
    @Query("SELECT COALESCE(AVG(r.rate), 5) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingScoreByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.product.id = :productId")
    int countByProductId(@Param("productId") Long productId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Review r JOIN r.user u WHERE r.product.id = :productId AND u.id = :userId")
    boolean existsByProductIdAndUserId(@Param("productId") Long productId, @Param("userId") Long userId);

    @Query("SELECT COUNT(r.id) FROM Review r")
    int countByReviewId();
}
