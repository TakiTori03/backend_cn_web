package com.hust.Ecommerce.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;

import io.github.perplexhub.rsql.RSQLJPASupport;

public class SearchUtils {

    public static <T> Specification<T> parse(String search, List<String> searchFields) {
        // Nếu `search` là null, rỗng hoặc không có search fields, trả về Specification
        // trống
        if (search == null || search.isBlank() || searchFields == null || searchFields.isEmpty()) {
            return Specification.where(null);
        }

        // Kết hợp search fields với giá trị tìm kiếm vào định dạng RSQL
        String rsqlQuery = searchFields.stream()
                .map(field -> field + "=like='" + search.trim() + "'")
                .collect(Collectors.joining(","));

        // Trả về Specification với query từ RSQLJPASupport
        return RSQLJPASupport.toSpecification(rsqlQuery);
    }

}
