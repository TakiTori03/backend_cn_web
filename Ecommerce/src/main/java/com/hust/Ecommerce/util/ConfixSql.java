package com.hust.Ecommerce.util;

public class ConfixSql {
    public interface User {
        // lấy ra tất cả user với truyền admin
        String GET_ALL_USER = "SELECT o FROM User o WHERE o.status != 0 AND (:keyword IS NULL OR :keyword = '' "
                +
                "OR o.name LIKE %:keyword% " +
                "OR o.address LIKE %:keyword% " +
                "OR o.email LIKE %:keyword%) ";
    }

    public interface Product {
        // Tìm kiếm sản phẩm theo từ khóa và id danh mục
        String SEARCH_PRODUCT_BY_KEYWORD = "SELECT p FROM Product p WHERE " +
                "(:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId) " +
                "AND (:keyword IS NULL OR :keyword = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))";

        // lấy ra chi tiết sản phẩm theo id
        String GET_DETAIL_PRODUCT = "SELECT p FROM Product p LEFT JOIN FETCH p.imageList where p.id = :productId";

        // tìm kiếm sản phẩm theo danh sách id
        String FIND_PRODUCT_BY_IDS = "SELECT p FROM Product p where p.id IN :productIds";
    }
}
