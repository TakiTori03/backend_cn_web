package com.hust.Ecommerce.dtos.review;

import io.micrometer.common.lang.Nullable;
import lombok.Data;

@Data
public class ReviewRequest {
    private Long userId;
    private Long productId;
    private Integer rate;
    private String content;
    @Nullable
    private String reply;
}
