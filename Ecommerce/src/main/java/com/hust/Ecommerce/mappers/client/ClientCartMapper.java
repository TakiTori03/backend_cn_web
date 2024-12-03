package com.hust.Ecommerce.mappers.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.hust.Ecommerce.dtos.client.cart.ClientCartRequest;
import com.hust.Ecommerce.dtos.client.cart.ClientCartResponse;
import com.hust.Ecommerce.dtos.client.cart.ClientCartVariantRequest;
import com.hust.Ecommerce.dtos.client.cart.ClientCartVariantResponse;
import com.hust.Ecommerce.dtos.client.cart.UpdateQuantityType;
import com.hust.Ecommerce.entities.cart.Cart;
import com.hust.Ecommerce.entities.cart.CartVariant;
import com.hust.Ecommerce.entities.cart.CartVariantKey;
import com.hust.Ecommerce.entities.general.Image;
import com.hust.Ecommerce.entities.product.Product;
import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.repositories.authentication.UserRepository;
import com.hust.Ecommerce.repositories.product.VariantRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ClientCartMapper {

    private UserRepository userRepository;
    private VariantRepository variantRepository;

    public ClientCartResponse entityToResponse(Cart entity) {
        var response = new ClientCartResponse();
        response.setCartId(entity.getId());

        response.setCartItems(entity.getCartVariants().stream()
                .sorted(Comparator.comparing(CartVariant::getCreatedAt))
                .map(this::entityToResponse)
                .collect(Collectors.toList()));
        return response;
    }

    private ClientCartVariantResponse.ClientVariantResponse.ClientProductResponse entityToResponse(Product entity) {
        var response = new ClientCartVariantResponse.ClientVariantResponse.ClientProductResponse();
        response.setProductId(entity.getId());
        response.setProductName(entity.getName());
        response.setProductSlug(entity.getSlug());
        response.setProductThumbnail(Optional.ofNullable(entity.getImages())
                .orElse(Collections.emptyList())
                .stream()
                .filter(image -> image.getIsThumbnail() == true)
                .findFirst()
                .map(Image::getPath)
                .orElse(null));
        return response;
    }

    private ClientCartVariantResponse.ClientVariantResponse entityToResponse(Variant entity) {
        var response = new ClientCartVariantResponse.ClientVariantResponse();
        response.setVariantId(entity.getId());
        response.setVariantProduct(entityToResponse(entity.getProduct()));
        response.setVariantPrice(entity.getPrice());
        response.setVariantProperties(entity.getProperties());

        return response;
    }

    private ClientCartVariantResponse entityToResponse(CartVariant entity) {
        var response = new ClientCartVariantResponse();
        response.setCartItemVariant(entityToResponse(entity.getVariant()));
        response.setCartItemQuantity(entity.getQuantity());
        return response;
    }

    public Cart requestToEntity(ClientCartRequest request) {
        var entity = new Cart();
        entity.setUser(userRepository.getById(request.getUserId()));
        entity.setCartVariants(request.getCartItems().stream().map(this::requestToEntity).collect(Collectors.toList()));
        entity.setStatus(request.getStatus());
        attach(entity);
        return entity;
    }

    private CartVariant requestToEntity(ClientCartVariantRequest request) {
        var entity = new CartVariant();
        entity.setVariant(variantRepository.getById(request.getVariantId()));
        entity.setQuantity(request.getQuantity());
        return entity;
    }

    // cap nhat truong CartVariantId va Cart cho CartVariant sau khi cap nhat Cart
    private void attach(Cart cart) {
        cart.getCartVariants().forEach(cartVariant -> {
            cartVariant.setCartVariantKey(new CartVariantKey(cart.getId(), cartVariant.getVariant().getId()));
            cartVariant.setCart(cart);
        });
    }

    // update tung phan cua cart
    public Cart partialUpdate(Cart entity, ClientCartRequest request) {
        List<Long> currentVariantIds = entity.getCartVariants().stream()
                .map(CartVariant::getCartVariantKey)
                .map(CartVariantKey::getVariantId)
                .collect(Collectors.toList());
        List<CartVariant> newCartVariants = new ArrayList<>();

        // (1) Cập nhật các cartVariant đang có trong cart
        for (CartVariant cartVariant : entity.getCartVariants()) {
            for (ClientCartVariantRequest clientCartVariantRequest : request.getCartItems()) {
                if (Objects.equals(cartVariant.getCartVariantKey().getVariantId(),
                        clientCartVariantRequest.getVariantId())) {
                    if (request.getUpdateQuantityType() == UpdateQuantityType.OVERRIDE) {
                        cartVariant.setQuantity(clientCartVariantRequest.getQuantity());
                    } else {
                        cartVariant.setQuantity(cartVariant.getQuantity() + clientCartVariantRequest.getQuantity());
                    }
                    break;
                }
            }
        }

        // (2) Thêm những cartVariant mới từ request
        for (ClientCartVariantRequest clientCartVariantRequest : request.getCartItems()) {
            if (!currentVariantIds.contains(clientCartVariantRequest.getVariantId())) {
                newCartVariants.add(requestToEntity(clientCartVariantRequest));
            }
        }

        entity.getCartVariants().addAll(newCartVariants);
        entity.setStatus(request.getStatus());
        attach(entity);
        return entity;
    }
}
