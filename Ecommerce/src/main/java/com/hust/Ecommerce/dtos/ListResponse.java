package com.hust.Ecommerce.dtos;

import lombok.*;

import java.util.List;

import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ListResponse<T> {
    List<T> content;
    Integer pageNumber;
    Integer pageSize;
    long totalElements;
    int totalPages;
    boolean isLast;

    public <E> ListResponse(List<T> content, Page<E> page) {
        this.content = content;
        this.pageNumber = page.getNumber() + 1;
        this.pageSize = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.isLast = page.isLast();
    }

    public static <T, E> ListResponse<T> of(List<T> content, Page<E> page) {
        return new ListResponse<>(content, page);
    }
}
