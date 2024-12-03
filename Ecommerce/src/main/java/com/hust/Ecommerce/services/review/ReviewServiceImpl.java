package com.hust.Ecommerce.services.review;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.Ecommerce.constants.FieldName;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.constants.SearchFields;
import com.hust.Ecommerce.dtos.ListResponse;
import com.hust.Ecommerce.dtos.review.ReviewRequest;
import com.hust.Ecommerce.dtos.review.ReviewResponse;
import com.hust.Ecommerce.entities.review.Review;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.review.ReviewMapper;
import com.hust.Ecommerce.repositories.review.ReviewRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private ReviewRepository reviewRepository;
    private ReviewMapper reviewMapper;

    @Override
    public ListResponse<ReviewResponse> findAll(int page, int size, String sort, String filter, String search,
            boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.REVIEW, reviewRepository,
                reviewMapper);
    }

    @Override
    public ReviewResponse findById(Long id) {
        return defaultFindById(id, reviewRepository, reviewMapper, ResourceName.REVIEW);
    }

    @Override
    public ReviewResponse save(ReviewRequest request) {
        if (reviewRepository.existsByProductIdAndUserId(request.getProductId(), request.getUserId())) {
            throw new RuntimeException("ban da review san pham nay roi !");
        }
        return defaultSave(request, reviewRepository, reviewMapper);
    }

    @Override
    public ReviewResponse save(Long id, ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .map(existingEntity -> reviewMapper.partialUpdate(existingEntity, request))
                .map(reviewRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.REVIEW, FieldName.ID, id));

        return reviewMapper.entityToResponse(review);
    }

    @Override
    public void delete(Long id) {
        reviewRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        reviewRepository.deleteAllById(ids);
    }

}
