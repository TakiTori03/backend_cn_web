package com.hust.Ecommerce.services.order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hust.Ecommerce.constants.AppConstants;
import com.hust.Ecommerce.constants.FieldName;
import com.hust.Ecommerce.constants.MessageKeys;
import com.hust.Ecommerce.constants.ResourceName;
import com.hust.Ecommerce.constants.SearchFields;
import com.hust.Ecommerce.dtos.ListResponse;
import com.hust.Ecommerce.dtos.client.order.ClientConfirmedOrderResponse;
import com.hust.Ecommerce.dtos.client.order.ClientSimpleOrderRequest;
import com.hust.Ecommerce.dtos.client.order.OrderRequest;
import com.hust.Ecommerce.dtos.client.order.OrderResponse;
import com.hust.Ecommerce.dtos.payment.PaymentRequest;
import com.hust.Ecommerce.dtos.payment.PaymentResponse;
import com.hust.Ecommerce.entities.authentication.User;
import com.hust.Ecommerce.entities.cart.Cart;
import com.hust.Ecommerce.entities.cart.CartVariant;
import com.hust.Ecommerce.entities.cashbook.PaymentMethodType;
import com.hust.Ecommerce.entities.cashbook.PaymentStatus;
import com.hust.Ecommerce.entities.inventory.Inventory;
import com.hust.Ecommerce.entities.order.Order;
import com.hust.Ecommerce.entities.order.OrderVariant;
import com.hust.Ecommerce.entities.product.Variant;

import com.hust.Ecommerce.exceptions.payload.ResourceNotFoundException;
import com.hust.Ecommerce.mappers.order.OrderMapper;
import com.hust.Ecommerce.repositories.cart.CartRepository;
import com.hust.Ecommerce.repositories.inventory.InventoryRepository;
import com.hust.Ecommerce.repositories.order.OrderRepository;
import com.hust.Ecommerce.services.authentication.AuthenticationService;
import com.hust.Ecommerce.services.payment.PaymentService;
import com.hust.Ecommerce.util.RandomUtil;

import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {

    private AuthenticationService authenticationService;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private PaymentService vnPayService;
    private OrderMapper orderMapper;
    private InventoryRepository inventoryRepository;

    // order service cho phia client

    @Override
    public ClientConfirmedOrderResponse createClientOrder(ClientSimpleOrderRequest request) {
        User user = authenticationService.getUserWithAuthorities()
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(MessageKeys.CART_NOT_FOUND));

        // (1) Tạo đơn hàng
        Order order = new Order();

        order.setCode(RandomUtil.generateOrderCode());
        order.setStatus(1); // Status 1: Đơn hàng mới
        order.setToName(request.getShippingInfo().getName());
        order.setToPhone(request.getShippingInfo().getPhone());
        order.setToAddress(request.getShippingInfo().getAddress());
        order.setNote(request.getNote());
        order.setUser(user);

        order.setOrderVariants(cart.getCartVariants().stream()
                .map((CartVariant cartVariant) -> {
                    // xu ly promotion neu co
                    Double currentPrice = cartVariant.getVariant().getPrice();
                    return new OrderVariant()
                            .setOrder(order)
                            .setVariant(cartVariant.getVariant())
                            .setPrice(BigDecimal.valueOf(currentPrice))
                            .setQuantity(cartVariant.getQuantity())
                            .setAmount(BigDecimal.valueOf(currentPrice)
                                    .multiply(BigDecimal.valueOf(cartVariant.getQuantity())));
                })
                .collect(Collectors.toList()));

        // Calculate price values
        // TODO: Vấn đề khuyến mãi
        BigDecimal totalAmount = BigDecimal.valueOf(order.getOrderVariants().stream()
                .mapToDouble(orderVariant -> orderVariant.getAmount().doubleValue())
                .sum());

        BigDecimal tax = BigDecimal.valueOf(AppConstants.DEFAULT_TAX);

        BigDecimal shippingCost = BigDecimal.ZERO;

        BigDecimal totalPay = totalAmount
                .add(totalAmount.multiply(tax).setScale(0, RoundingMode.HALF_UP))
                .add(shippingCost);

        order.setTotalAmount(totalAmount);
        order.setTax(tax);
        order.setShippingCost(shippingCost);
        order.setTotalPay(totalPay);
        order.setPaymentMethodType(request.getPaymentMethodType());
        order.setPaymentStatus(0); // Status 0: Chưa thanh toán

        // (2) Tạo response
        ClientConfirmedOrderResponse response = new ClientConfirmedOrderResponse();

        response.setOrderCode(order.getCode());
        response.setOrderPaymentMethodType(order.getPaymentMethodType());

        // (3) Kiểm tra hình thức thanh toán
        if (request.getPaymentMethodType() == PaymentMethodType.CASH) {
            orderRepository.save(order);
        } else if (request.getPaymentMethodType() == PaymentMethodType.VNPAY) {
            try {

                // (3.2.2) Tạo một yêu cầu giao dịch vnpay
                PaymentRequest vnpRequest = new PaymentRequest();

                // vnpRequest.setAmount(order.getTotalPay().longValue());
                vnpRequest.setAmount(500000L);
                vnpRequest.setTxnRef(order.getCode());
                vnpRequest.setIpAddress(request.getIpAdress());
                vnpRequest.setUserId(user.getId());

                PaymentResponse vnpResponse = vnPayService.init(vnpRequest);

                // (3.2.3) Lưu order
                order.setVnPayOrderId(vnpResponse.getId());
                order.setVnPayOrderStatus(PaymentStatus.Pending);
                log.debug("tao order {}", order);
                orderRepository.save(order);

                // (3.2.4) Trả về đường dẫn checkout cho user
                response.setOrderVnpayCheckoutLink(vnpResponse.getVnpUrl());
            } catch (Exception e) {
                throw new RuntimeException("Cannot create vnPay transaction request!" + e);
            }
        } else {
            throw new RuntimeException("Cannot identify payment method");
        }

        // vo hieu luc cart hien tai ngay khi thuc hien checkout order
        // Vô hiệu cart
        cart.setStatus(2); // Status 2: Vô hiệu lực
        cartRepository.save(cart);

        return response;
    }

    // order service cho phia admin
    @Override
    public ListResponse<OrderResponse> findAll(int page, int size, String sort, String filter, String search,
            boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.REVIEW, orderRepository,
                orderMapper);
    }

    @Override
    public OrderResponse findById(Long id) {
        return defaultFindById(id, orderRepository, orderMapper, ResourceName.REVIEW);
    }

    @Override
    public OrderResponse save(OrderRequest request) {
        return defaultSave(request, orderRepository, orderMapper);
    }

    @Override
    public OrderResponse save(Long id, OrderRequest request) {
        Order order = orderRepository.findById(id)
                .map((Order existingEntity) -> {

                    // khong cap nhat cho don hang da cancel
                    if (existingEntity.getStatus() == 5)
                        throw new RuntimeException("khong cap nhat don hang CANCEL");

                    return orderMapper.partialUpdate(existingEntity, request);
                })
                .map((Order orderEntity) -> {

                    // neu khong cap nhat status
                    if (request.getStatus() == null)
                        return orderEntity;

                    // neu status moi < status hien tai cua order
                    if (request.getStatus() < orderEntity.getStatus())
                        throw new RuntimeException("khong the cap nhat trang thai quay lai");

                    // neu trang thai order hien tai la pending
                    if (orderEntity.getStatus() == 1) {

                        // neu trang thai moi khong phai cancel
                        if (request.getStatus() > 1 && request.getStatus() != 5) {

                            // cap nhat inventory (giam available)
                            orderEntity.getOrderVariants().stream().forEach(orderVariant -> {
                                Variant variant = orderVariant.getVariant();
                                Inventory inventory = variant.getInventory();
                                if (inventory == null)
                                    throw new ResourceNotFoundException("inventory khong ton tai");

                                if (inventory.getAvailable() < orderVariant.getQuantity())
                                    throw new RuntimeException("inventory khong du");
                                Integer newAvailable = inventory.getAvailable() - orderVariant.getQuantity();
                                inventory.setAvailable(newAvailable);
                                inventoryRepository.save(inventory);
                            });
                        }
                    } else {
                        // cap nhat order CANCEL (huy don)
                        if (request.getStatus() == 5) {
                            // cap nhat inventory (tang available)
                            orderEntity.getOrderVariants().stream().forEach(orderVariant -> {
                                Variant variant = orderVariant.getVariant();
                                Inventory inventory = variant.getInventory();
                                if (inventory == null)
                                    throw new ResourceNotFoundException("inventory khong ton tai");

                                Integer newAvailable = inventory.getAvailable() + orderVariant.getQuantity();
                                inventory.setAvailable(newAvailable);
                                inventoryRepository.save(inventory);
                            });
                        }
                    }

                    // cap nhat order FINISHED
                    if (request.getStatus() == 6) {
                        // cap nhat inventory (tang sold)
                        orderEntity.getOrderVariants().stream().forEach(orderVariant -> {
                            Variant variant = orderVariant.getVariant();
                            Inventory inventory = variant.getInventory();
                            if (inventory == null)
                                throw new ResourceNotFoundException("inventory khong ton tai");

                            Integer newSold = inventory.getSold() + orderVariant.getQuantity();
                            inventory.setSold(newSold);
                            inventoryRepository.save(inventory);
                        });
                    }

                    // cap nhat status order
                    orderEntity.setStatus(request.getStatus());
                    return orderEntity;

                })
                .map(orderRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.ORDER,
                        FieldName.ID, id));

        return orderMapper.entityToResponse(order);

    }

    @Override
    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }

}
