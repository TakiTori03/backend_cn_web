package com.hust.Ecommerce.controllers.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.constants.FieldName;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.CollectionWrapper;
import com.hust.Ecommerce.dtos.client.ClientCategoryResponse;
import com.hust.Ecommerce.entities.product.Category;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.client.ClientCategoryMapper;
import com.hust.Ecommerce.repositories.product.CategoryRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/client-api/categories")
@RequiredArgsConstructor
public class ClientCategoryController {

    private final CategoryRepository categoryRepository;
    private final ClientCategoryMapper clientCategoryMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<ClientCategoryResponse> clientCategoryResponses = categories.stream()
                .map(clientCategoryMapper::entityToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .payload(CollectionWrapper.of(clientCategoryResponses))
                .build());
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<?>> getCategory(@PathVariable("slug") String slug) {
        ClientCategoryResponse clientCategoryResponse = categoryRepository.findBySlug(slug)
                .map(category -> clientCategoryMapper.entityToResponse(category))
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.CATEGORY, FieldName.SLUG, slug));
        return ResponseEntity.ok(
                ApiResponse.<ClientCategoryResponse>builder().success(true).payload(clientCategoryResponse).build());
    }

}