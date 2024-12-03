package com.hust.Ecommerce.dtos;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CollectionWrapper<T> {
    private List<T> content;
    private long totalElements;

    public CollectionWrapper(List<T> content) {
        this.content = content;
        this.totalElements = content.size();
    }

    public static <T> CollectionWrapper<T> of(List<T> content) {
        return new CollectionWrapper<>(content);
    }
}