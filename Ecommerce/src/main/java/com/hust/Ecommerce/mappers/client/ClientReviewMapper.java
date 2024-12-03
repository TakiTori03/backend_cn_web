package com.hust.Ecommerce.mappers.client;

import org.springframework.stereotype.Component;

import com.hust.Ecommerce.dtos.client.review.ClientReviewRequest;
import com.hust.Ecommerce.dtos.client.review.ClientReviewResponse;
import com.hust.Ecommerce.dtos.client.review.ClientSimpleReviewResponse;
import com.hust.Ecommerce.entities.review.Review;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.repositories.product.ProductRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ClientReviewMapper {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    // private ClientProductMapper clientProductMapper;

    public Review requestToEntity(ClientReviewRequest request) {
        Review entity = new Review();
        entity.setUser(userRepository.getById(request.getUserId()));
        entity.setProduct(productRepository.getById(request.getProductId()));
        entity.setRate(request.getRate());
        entity.setContent(request.getContent());
        return entity;
    }

    public ClientReviewResponse entityToResponse(Review entity) {
        ClientReviewResponse response = new ClientReviewResponse();
        response.setReviewId(entity.getId());
        response.setReviewCreatedAt(entity.getCreatedAt());
        response.setReviewUpdatedAt(entity.getUpdatedAt());

        // response.setReviewProduct(clientProductMapper.entityToListedResponse(entity.getProduct(),
        // Collections.emptyList()));
        response.setReviewRate(entity.getRate());
        response.setReviewContent(entity.getContent());
        response.setReviewReply(entity.getReply());
        return response;
    }

    public ClientSimpleReviewResponse entityToSimpleResponse(Review entity) {
        ClientSimpleReviewResponse response = new ClientSimpleReviewResponse();
        response.setReviewId(entity.getId());
        response.setReviewCreatedAt(entity.getCreatedAt());
        response.setReviewUpdatedAt(entity.getUpdatedAt());
        response.setReviewUser(new ClientSimpleReviewResponse.UserResponse()
                .setUserId(entity.getUser().getId())
                .setUserName(entity.getUser().getName())
                .setUserEmail(entity.getUser().getEmail()));
        response.setReviewRate(entity.getRate());
        response.setReviewContent(entity.getContent());
        response.setReviewReply(entity.getReply());
        return response;
    }

    public Review partialUpdate(Review entity, ClientReviewRequest request) {
        entity.setRate(request.getRate());
        entity.setContent(request.getContent());
        return entity;
    }
}
