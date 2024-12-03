package com.hust.Ecommerce.dtos.client.review;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientReviewRequest {
    private Long userId;
    private Long productId;
    private Integer rate;
    private String content;
}
