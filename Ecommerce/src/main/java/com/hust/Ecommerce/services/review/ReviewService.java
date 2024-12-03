package com.hust.Ecommerce.services.review;

import com.hust.Ecommerce.dtos.review.ReviewRequest;
import com.hust.Ecommerce.dtos.review.ReviewResponse;
import com.hust.Ecommerce.services.CrudService;

public interface ReviewService extends CrudService<Long, ReviewRequest, ReviewResponse> {
}
