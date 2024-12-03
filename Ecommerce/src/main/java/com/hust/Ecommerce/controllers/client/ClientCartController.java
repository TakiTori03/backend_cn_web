package com.hust.Ecommerce.controllers.client;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hust.Ecommerce.constants.FieldName;
import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.client.cart.ClientCartRequest;
import com.hust.Ecommerce.dtos.client.cart.ClientCartResponse;
import com.hust.Ecommerce.dtos.client.cart.ClientCartVariantKeyRequest;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.cart.Cart;
import com.hust.Ecommerce.entities.cart.CartVariant;
import com.hust.Ecommerce.entities.cart.CartVariantKey;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.client.ClientCartMapper;
import com.hust.Ecommerce.repositories.cart.CartRepository;
import com.hust.Ecommerce.repositories.cart.CartVariantRepository;
import com.hust.Ecommerce.repositories.inventory.InventoryRepository;
import com.hust.Ecommerce.services.authentication.AuthenticationService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/client-api/carts")
@AllArgsConstructor
public class ClientCartController {

    private CartRepository cartRepository;
    private CartVariantRepository cartVariantRepository;
    private ClientCartMapper clientCartMapper;
    private AuthenticationService authenticationService;
    private InventoryRepository inventoryRepository;

    // lay cac cartVariant hien tai cua user ma co status = 1
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getCart() {
        User user = authenticationService.getUserWithAuthorities()
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.ACCOUNT_NOT_LOGIN));

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode response = cartRepository.findByUser(user)
                .map(clientCartMapper::entityToResponse)
                .map(clientCartResponse -> mapper.convertValue(clientCartResponse, ObjectNode.class))
                .orElse(mapper.createObjectNode());

        return ResponseEntity.ok(ApiResponse.<ObjectNode>builder().success(true).payload(response).build());
    }

    // create new cart khong can cartId, nhung can userId
    // update cart khong can userId, nhung can cartId
    @PostMapping
    public ResponseEntity<ApiResponse<?>> saveCart(@RequestBody ClientCartRequest request) {
        User user = authenticationService.getUserWithAuthorities()
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.ACCOUNT_NOT_LOGIN));

        final Cart cartBeforeSave;

        // neu trong request khong co cart_id tuc la tao cart moi
        if (request.getCartId() == null) {
            // kiem tra user do dang co cart chua
            if (cartRepository.existsByUser(user))
                throw new RuntimeException("cart is existed");

            cartBeforeSave = clientCartMapper.requestToEntity(request);
        } else {
            // nguoc lai update cart
            cartBeforeSave = cartRepository.findById(request.getCartId())
                    .map(existingEntity -> clientCartMapper.partialUpdate(existingEntity,
                            request))
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceName.CART,
                            FieldName.ID, request.getCartId()));
        }

        // kiem tra xem trong inventory > quantity cart hay khong
        for (CartVariant cartVariant : cartBeforeSave.getCartVariants()) {
            // phai cap nhat inventory cho variant moi cho phep add into cart
            Integer inventory = inventoryRepository.findAvailableByVariantId(cartVariant.getVariant().getId());

            if (cartVariant.getQuantity() > inventory) {
                throw new RuntimeException(MessageKeys.UPDATE_CART_FAILED);
            }
        }

        // update cart
        Cart cart = cartRepository.save(cartBeforeSave);
        // response tra ve cho user
        ClientCartResponse clientCartResponse = clientCartMapper.entityToResponse(cart);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<ClientCartResponse>builder().success(true).payload(clientCartResponse).build());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteCartItems(@RequestBody List<ClientCartVariantKeyRequest> idRequests) {
        List<CartVariantKey> ids = idRequests.stream()
                .map(idRequest -> new CartVariantKey(idRequest.getCartId(), idRequest.getVariantId()))
                .collect(Collectors.toList());
        cartVariantRepository.deleteAllById(ids);
        return ResponseEntity.ok(ApiResponse.builder().success(true).build());
    }
}
