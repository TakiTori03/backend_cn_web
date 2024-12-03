package com.hust.Ecommerce.dtos.client.review;

import java.time.Instant;

import org.springframework.lang.Nullable;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientReviewResponse {
    private Long reviewId;
    private Instant reviewCreatedAt;
    private Instant reviewUpdatedAt;
    // private ClientListedProductResponse reviewProduct;
    private Integer reviewRate;
    private String reviewContent;
    @Nullable
    private String reviewReply;
}
