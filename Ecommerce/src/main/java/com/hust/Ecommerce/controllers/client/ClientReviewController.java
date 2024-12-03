package com.hust.Ecommerce.controllers.client;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.ListResponse;
import com.hust.Ecommerce.dtos.client.review.ClientReviewRequest;
import com.hust.Ecommerce.dtos.client.review.ClientReviewResponse;
import com.hust.Ecommerce.dtos.client.review.ClientSimpleReviewResponse;
import com.hust.Ecommerce.entities.review.Review;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.client.ClientReviewMapper;
import com.hust.Ecommerce.repositories.review.ReviewRepository;
import com.hust.Ecommerce.security.SecurityUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/client-api/reviews")
@AllArgsConstructor
public class ClientReviewController {

        private ReviewRepository reviewRepository;
        private ClientReviewMapper clientReviewMapper;

        @GetMapping("/products/{productSlug}")
        public ResponseEntity<ApiResponse<?>> getAllReviewsByProduct(
                        @PathVariable String productSlug,
                        @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                        @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                        @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT) String sort,
                        @RequestParam(name = "filter", required = false) @Nullable String filter) {
                Page<Review> reviews = reviewRepository.findAllByProductSlug(productSlug, sort, filter,
                                PageRequest.of(page - 1, size));
                List<ClientSimpleReviewResponse> clientReviewResponses = reviews
                                .map(clientReviewMapper::entityToSimpleResponse)
                                .toList();
                return ResponseEntity.ok(ApiResponse
                                .builder()
                                .success(true)
                                .payload(
                                                ListResponse.of(clientReviewResponses, reviews))
                                .build());

        }

        @GetMapping
        public ResponseEntity<ApiResponse<?>> getAllReviewsByUser(
                        @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                        @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                        @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT) String sort,
                        @RequestParam(name = "filter", required = false) @Nullable String filter) {
                String email = SecurityUtils.getCurrentUserLogin()
                                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));

                Page<Review> reviews = reviewRepository.findAllByEmail(email, sort,
                                filter,
                                PageRequest.of(page - 1, size));
                List<ClientReviewResponse> clientReviewResponses = reviews.map(clientReviewMapper::entityToResponse)
                                .toList();
                return ResponseEntity.ok(ApiResponse.builder()
                                .success(true)
                                .payload(ListResponse.of(clientReviewResponses,
                                                reviews))
                                .build());
        }

        @PostMapping
        public ResponseEntity<ApiResponse<?>> createReview(
                        @RequestBody ClientReviewRequest request) {
                Review entity = reviewRepository.save(clientReviewMapper.requestToEntity(request));
                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ClientReviewResponse>builder()
                                .success(true)
                                .payload(clientReviewMapper.entityToResponse(entity))
                                .build());
        }
        // khong cho phep update review
        // @PutMapping("/{id}")
        // public ResponseEntity<ApiResponse<?>> updateReview(@PathVariable Long id,
        // @RequestBody ClientReviewRequest request) {
        // ClientReviewResponse clientReviewResponse = reviewRepository.findById(id)
        // .map(existingEntity -> clientReviewMapper.partialUpdate(existingEntity,
        // request))
        // .map(reviewRepository::save)
        // .map(clientReviewMapper::entityToResponse)
        // .orElseThrow(() -> new ResourceNotFoundException(ResourceName.REVIEW,
        // FieldName.ID,
        // id));
        // return ResponseEntity.ok(ApiResponse.<ClientReviewResponse>builder()
        // .success(true)
        // .payload(clientReviewResponse)
        // .build());
        // }

        @DeleteMapping
        public ResponseEntity<ApiResponse<?>> deleteReviews(@RequestBody List<Long> ids) {
                reviewRepository.deleteAllById(ids);
                return ResponseEntity.ok(ApiResponse.builder().success(true).build());
        }
}
