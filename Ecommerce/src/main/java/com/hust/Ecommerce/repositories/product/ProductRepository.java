package com.hust.Ecommerce.repositories.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.hust.Ecommerce.constants.SearchFields;
import com.hust.Ecommerce.entities.inventory.Inventory;

import com.hust.Ecommerce.entities.product.Product;
import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.util.SearchUtils;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import io.github.perplexhub.rsql.RSQLCustomPredicate;
import io.github.perplexhub.rsql.RSQLJPASupport;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Optional<Product> findBySlug(String slug);

    @Query("SELECT COUNT(p.id) FROM Product p")
    int countByProductId();

    default Page<Product> findByParams(String filter,
            String sort,
            String search,
            boolean saleable,
            boolean newable,
            Pageable pageable) {
        // Xử lý `filter` thành Specification
        RSQLCustomPredicate<String> jsonPredicate = new RSQLCustomPredicate<>(
                new ComparisonOperator("=json=", true),
                String.class,
                input -> {
                    CriteriaBuilder cb = input.getCriteriaBuilder();

                    // Lấy phần còn lại của danh sách `input.getArguments()` sau khi bỏ qua phần tử
                    // đầu tiên
                    Object[] values = input.getArguments().stream().skip(1).toArray();

                    return cb.function("JSON_EXTRACT", String.class,
                            input.getPath(),
                            cb.function("REPLACE", String.class,
                                    cb.function("JSON_UNQUOTE", String.class,
                                            cb.function("JSON_SEARCH", String.class,
                                                    input.getPath(),
                                                    cb.literal("one"),
                                                    cb.literal(input.getArguments().get(0)))),
                                    cb.literal(".code"),
                                    cb.literal(".value")))
                            .in(values);
                });

        Specification<Product> filterable = RSQLJPASupport.toSpecification(filter, List.of(jsonPredicate));
        Specification<Product> searchable = SearchUtils.parse(search, SearchFields.CLIENT_PRODUCT);

        // Lọc theo `saleable` (có thể bán) và `newable` (thứ tự mới nhất)
        Specification<Product> inventoryable = (root, query, cb) -> {
            List<Predicate> wheres = new ArrayList<>();
            List<Order> orders = new ArrayList<>();

            Join<Product, Variant> variant = root.join("variants");
            Join<Variant, Inventory> inventory = variant.join("inventory");

            if (saleable) {
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<Variant> variantSq = subquery.from(Variant.class);
                Join<Variant, Inventory> inventorySq = variantSq.join("inventory");

                subquery.select(
                        cb.sum(inventorySq.get("available")));

                subquery.where(cb.equal(variantSq.get("product").get("id"), root.get("id")));
                subquery.groupBy(variantSq.get("product").get("id"));

                wheres.add(cb.greaterThan(subquery, 0));
            }

            if ("lowest-price".equals(sort)) {
                orders.add(cb.asc(cb.min(variant.get("price"))));
            }

            if ("highest-price".equals(sort)) {
                orders.add(cb.desc(cb.max(variant.get("price"))));
            }

            if ("random".equals(sort)) {
                orders.add(cb.asc(cb.function("RAND", Void.class)));
            }

            if (newable) {
                orders.add(cb.desc(cb.max(variant.get("createdAt"))));
                orders.add(cb.asc(root.get("id")));
            }

            Optional.ofNullable(filterable.toPredicate(root, query, cb)).ifPresent(wheres::add);
            Optional.ofNullable(searchable.toPredicate(root, query, cb)).ifPresent(wheres::add);

            query.where(wheres.toArray(Predicate[]::new));
            query.groupBy(root.get("id"));
            query.orderBy(orders);

            return query.getRestriction();
        };

        List<Product> products = findAll(inventoryable);
        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), products.size());

        return new PageImpl<>(products.subList(start, end), pageable, products.size());
    }

}
