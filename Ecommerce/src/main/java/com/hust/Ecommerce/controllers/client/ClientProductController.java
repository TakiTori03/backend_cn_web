package com.hust.Ecommerce.controllers.client;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.constants.FieldName;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.ListResponse;
import com.hust.Ecommerce.dtos.client.product.ClientListedProductResponse;
import com.hust.Ecommerce.dtos.client.product.ClientProductResponse;
import com.hust.Ecommerce.entities.product.Product;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.client.ClientProductMapper;
import com.hust.Ecommerce.mappers.projection.SimpleProductInventory;
import com.hust.Ecommerce.repositories.ProjectionRepository;
import com.hust.Ecommerce.repositories.product.ProductRepository;
import com.hust.Ecommerce.repositories.review.ReviewRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/client-api/products")
@AllArgsConstructor
public class ClientProductController {
        private ProductRepository productRepository;
        private ClientProductMapper clientProductMapper;
        private ReviewRepository reviewRepository;
        private ProjectionRepository projectionRepository;

        // saleable: co the ban, newable: moi nhat
        @GetMapping
        public ResponseEntity<ApiResponse<?>> getAllProducts(
                        @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                        @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                        @RequestParam(name = "filter", required = false) @Nullable String filter,
                        @RequestParam(name = "sort", required = false) @Nullable String sort,
                        @RequestParam(name = "search", required = false) @Nullable String search,
                        @RequestParam(name = "saleable", required = false) boolean saleable,
                        @RequestParam(name = "newable", required = false) boolean newable) {
                // Phân trang
                Pageable pageable = PageRequest.of(page - 1, size);

                // Lấy danh sách sản phẩm theo điều kiện lọc và phân trang
                Page<Product> products = productRepository.findByParams(filter, sort, search, saleable, newable,
                                pageable);

                // Lấy thông tin tồn kho của sản phẩm
                List<Long> productIds = products.map(Product::getId).toList();
                List<SimpleProductInventory> productInventories = projectionRepository
                                .findSimpleProductInventories(productIds);

                List<ClientListedProductResponse> clientListedProductResponses = products
                                .map(product -> clientProductMapper.entityToListedResponse(product, productInventories))
                                .toList();

                return ResponseEntity.ok(ApiResponse.<ListResponse<ClientListedProductResponse>>builder()
                                .success(true)
                                .payload(ListResponse.of(clientListedProductResponses, products))
                                .build());
        }

        @GetMapping("/{slug}")
        public ResponseEntity<ApiResponse<?>> getProduct(@PathVariable String slug) {
                Product product = productRepository.findBySlug(slug)
                                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.PRODUCT, FieldName.SLUG,
                                                slug));

                List<SimpleProductInventory> productInventories = projectionRepository
                                .findSimpleProductInventories(List.of(product.getId()));

                double averageRatingScore = reviewRepository.findAverageRatingScoreByProductId(product.getId());
                int countReviews = reviewRepository.countByProductId(product.getId());

                // tim kiem san phamr lien quan, dua vao category
                // Related Products
                // Page<Product> relatedProducts = productRepository.findByParams(
                // String.format("category.id==%s;id!=%s",
                // Optional.ofNullable(product.getCategory())
                // .map(BaseEntity::getId)
                // .map(Object::toString)
                // .orElse("0"),
                // product.getId()),
                // "random",
                // null,
                // false,
                // false,
                // PageRequest.of(0, 4));

                // Result
                ClientProductResponse clientProductResponse = clientProductMapper.entityToResponse(product,
                                productInventories, averageRatingScore, countReviews);

                return ResponseEntity.ok(ApiResponse.<ClientProductResponse>builder()
                                .success(true)
                                .payload(clientProductResponse)
                                .build());
        }
}
