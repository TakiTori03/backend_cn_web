package com.hust.Ecommerce.mappers.client;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hust.Ecommerce.dtos.client.product.ClientListedProductResponse;
import com.hust.Ecommerce.dtos.client.product.ClientProductResponse;
import com.hust.Ecommerce.entities.general.Image;
import com.hust.Ecommerce.entities.product.Product;
import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.mappers.general.ImageMapper;
import com.hust.Ecommerce.mappers.projection.SimpleProductInventory;
import com.hust.Ecommerce.repositories.review.ReviewRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ClientProductMapper {

        private ImageMapper imageMapper;
        private ClientCategoryMapper clientCategoryMapper;
        private ReviewRepository reviewRepository;

        public ClientListedProductResponse entityToListedResponse(Product product,
                        List<SimpleProductInventory> productInventories) {
                ClientListedProductResponse clientListedProductResponse = new ClientListedProductResponse();

                clientListedProductResponse
                                .setProductId(product.getId())
                                .setProductName(product.getName())
                                .setProductSlug(product.getSlug())
                                .setProductThumbnail(Optional.ofNullable(product.getImages())
                                                .orElse(Collections.emptyList()) // Đảm bảo không bị null
                                                .stream()
                                                .filter(image -> image.getIsThumbnail() == true)
                                                .findFirst()
                                                .map(Image::getPath)
                                                .orElse(null));

                List<Double> prices = product.getVariants().stream()
                                .map(Variant::getPrice).distinct().sorted().collect(Collectors.toList());

                clientListedProductResponse.setProductPriceRange(
                                prices.size() == 0
                                                ? Collections.emptyList()
                                                : prices.size() == 1
                                                                ? List.of(prices.get(0))
                                                                : List.of(prices.get(0),
                                                                                prices.get(prices.size() - 1)));

                clientListedProductResponse.setProductVariants(product.getVariants().stream()
                                .map(variant -> new ClientListedProductResponse.ClientListedVariantResponse()
                                                .setVariantId(variant.getId())
                                                .setVariantPrice(variant.getPrice())
                                                .setVariantProperties(variant.getProperties()))
                                .collect(Collectors.toList()));

                clientListedProductResponse.setProductSaleable(productInventories.stream()
                                .filter(productInventory -> productInventory.getProductId().equals(product.getId()))
                                .findAny()
                                .map(productInventory -> productInventory.getAvailable() > 0)
                                .orElse(false));

                clientListedProductResponse.setProductAverageRate(
                                reviewRepository.findAverageRatingScoreByProductId(product.getId()));

                clientListedProductResponse.setProductSold(productInventories.stream()
                                .filter(productInventory -> productInventory.getProductId().equals(product.getId()))
                                .findAny()
                                .map(productInventory -> productInventory.getSold())
                                .orElse(0));

                return clientListedProductResponse;
        }

        public ClientProductResponse entityToResponse(Product product,
                        List<SimpleProductInventory> productInventories,
                        double averageRatingScore,
                        int countReviews) {
                ClientProductResponse clientProductResponse = new ClientProductResponse();

                clientProductResponse.setProductId(product.getId());
                clientProductResponse.setProductName(product.getName());
                clientProductResponse.setProductSlug(product.getSlug());
                clientProductResponse.setProductDescription(product.getDescription());
                clientProductResponse.setProductImages(imageMapper.entityToResponse(product.getImages()));
                clientProductResponse.setProductCategories(product.getCategories().stream()
                                .map(category -> clientCategoryMapper.entityToResponse(category))
                                .collect(Collectors.toList()));

                clientProductResponse.setProductBrand(product.getBrand() == null ? null
                                : new ClientProductResponse.ClientBrandResponse()
                                                .setBrandId(product.getBrand().getId())
                                                .setBrandName(product.getBrand().getName()));

                clientProductResponse.setProductSpecifications(product.getSpecifications());
                clientProductResponse.setProductVariants(product.getVariants().stream()
                                .map(variant -> new ClientProductResponse.ClientVariantResponse()
                                                .setVariantId(variant.getId())
                                                .setVariantPrice(variant.getPrice())
                                                .setVariantProperties(variant.getProperties())
                                                .setVariantAvailable(variant.getInventory().getAvailable()))
                                .collect(Collectors.toList()));

                clientProductResponse.setProductSaleable(productInventories.stream()
                                .filter(productInventory -> productInventory.getProductId().equals(product.getId()))
                                .findAny()
                                .map(productInventory -> productInventory.getAvailable() > 0)
                                .orElse(false));
                clientProductResponse.setProductWarrantyDuration(product.getWarrantyDuration());
                clientProductResponse.setProductAverageRate(averageRatingScore);
                clientProductResponse.setProductCountReviews(countReviews);

                return clientProductResponse;
        }

}
