package com.hust.Ecommerce.controllers.client;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.constants.FieldName;
import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.dtos.ApiResponse;
import com.hust.Ecommerce.dtos.ListResponse;
import com.hust.Ecommerce.dtos.client.order.ClientConfirmedOrderResponse;
import com.hust.Ecommerce.dtos.client.order.ClientOrderDetailResponse;
import com.hust.Ecommerce.dtos.client.order.ClientSimpleOrderRequest;
import com.hust.Ecommerce.dtos.client.order.ClientSimpleOrderResponse;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.cashbook.PaymentStatus;
import com.hust.Ecommerce.entities.inventory.Inventory;
import com.hust.Ecommerce.entities.order.Order;
import com.hust.Ecommerce.entities.product.Variant;
import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.client.ClientOrderMapper;
import com.hust.Ecommerce.repositories.inventory.InventoryRepository;
import com.hust.Ecommerce.repositories.order.OrderRepository;
import com.hust.Ecommerce.services.authentication.AuthenticationService;
import com.hust.Ecommerce.services.order.OrderService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/client-api/orders")
@AllArgsConstructor
public class ClientOrderController {

        private OrderRepository orderRepository;
        private InventoryRepository inventoryRepository;
        private AuthenticationService authenticationService;
        private ClientOrderMapper clientOrderMapper;
        private OrderService orderService;

        @GetMapping
        public ResponseEntity<ApiResponse<?>> getAllOrders(
                        @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
                        @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                        @RequestParam(name = "sort", defaultValue = AppConstants.DEFAULT_SORT) String sort,
                        @RequestParam(name = "filter", required = false) @Nullable String filter) {
                User user = authenticationService.getUserWithAuthorities()
                                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));

                Page<Order> orders = orderRepository.findAllByEmail(user.getEmail(), sort, filter,
                                PageRequest.of(page - 1, size));
                List<ClientSimpleOrderResponse> clientReviewResponses = orders.map(clientOrderMapper::entityToResponse)
                                .toList();
                return ResponseEntity.ok(ApiResponse.<ListResponse<ClientSimpleOrderResponse>>builder().success(true)
                                .payload(ListResponse.of(clientReviewResponses, orders)).build());
        }

        @GetMapping("/{code}")
        public ResponseEntity<ApiResponse<?>> getOrder(@PathVariable String code) {
                ClientOrderDetailResponse clientOrderDetailResponse = orderRepository.findByCode(code)
                                .map(clientOrderMapper::entityToDetailResponse)
                                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.ORDER,
                                                FieldName.ORDER_CODE, code));
                return ResponseEntity.ok(ApiResponse.<ClientOrderDetailResponse>builder().success(true)
                                .payload(clientOrderDetailResponse).build());
        }

        @PostMapping
        public ResponseEntity<ApiResponse<?>> createClientOrder(
                        @RequestBody ClientSimpleOrderRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.<ClientConfirmedOrderResponse>builder()
                                                .success(true).payload(orderService.createClientOrder(request))
                                                .build());
        }

        // frontend gui len xac nhan thanh toan don hang thanh cong hay that bai

        @GetMapping("/confirm")
        public void paymentConfirm(@RequestParam Map<String, String> queryParams) {

                // ket qua cua giao dich
                String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");

                // id cua transaction; cung nhu la code cua order
                String vnp_TxnRef = queryParams.get("vnp_TxnRef");

                Order order = orderRepository.findByCode(vnp_TxnRef).orElseThrow(
                                () -> new ResourceNotFoundException(ResourceName.ORDER, FieldName.CODE, vnp_TxnRef));

                if (vnp_ResponseCode.equals("00")) {

                        // xu ly khi thanh toan thanh cong
                        // neu trang thai don hang hien tai khong phai pending thi khong lam gi
                        if (order.getStatus() != 1)
                                return;
                        // chueyn trang thai order sang confirm va xac nhan da thanh toan don hang
                        order.setStatus(2);
                        order.setPaymentStatus(1);
                        order.setVnPayOrderStatus(PaymentStatus.Paid);

                        // cap nhat inventory
                        order.getOrderVariants().stream().forEach(orderVariant -> {
                                Variant variant = orderVariant.getVariant();
                                Inventory inventory = variant.getInventory();
                                if (inventory == null)
                                        throw new ResourceNotFoundException("inventory khong ton tai");

                                if (inventory.getAvailable() < orderVariant.getQuantity())
                                        throw new RuntimeException("inventory khong du");
                                Integer newInventory = inventory.getAvailable() - orderVariant.getQuantity();
                                inventory.setAvailable(newInventory);
                                inventoryRepository.save(inventory);
                        });
                        // cap nhat order
                        orderRepository.save(order);
                } else {
                        // xu ly khi that bai thanh toan
                        // neu trang thai don hang hien tai khong phai pending thi khong lam gi
                        if (order.getStatus() != 1)
                                return;
                        // chuyen trang thai order sang cancel
                        order.setStatus(5);
                        order.setVnPayOrderStatus(PaymentStatus.Failed);

                        // cap nhat order
                        orderRepository.save(order);
                }
        }
}
