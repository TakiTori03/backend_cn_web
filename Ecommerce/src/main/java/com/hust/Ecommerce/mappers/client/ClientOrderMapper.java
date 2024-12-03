package com.hust.Ecommerce.mappers.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import com.hust.Ecommerce.dtos.client.order.ClientOrderDetailResponse;
import com.hust.Ecommerce.dtos.client.order.ClientOrderVariantResponse;
import com.hust.Ecommerce.dtos.client.order.ClientSimpleOrderResponse;
import com.hust.Ecommerce.entities.order.Order;
import com.hust.Ecommerce.entities.order.OrderVariant;
import com.hust.Ecommerce.repositories.review.ReviewRepository;
import com.hust.Ecommerce.util.MapperUtils;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public abstract class ClientOrderMapper {

    @Autowired
    private ReviewRepository reviewRepository;

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "createdAt", target = "orderCreatedAt")
    @Mapping(source = "code", target = "orderCode")
    @Mapping(source = "status", target = "orderStatus")
    @Mapping(source = "totalPay", target = "orderTotalPay")
    @Mapping(source = "orderVariants", target = "orderItems")
    @Mapping(source = "paymentStatus", target = "orderPaymentStatus")
    public abstract ClientSimpleOrderResponse entityToResponse(Order order);

    @Mapping(source = "variant.id", target = "orderItemVariant.variantId")
    @Mapping(source = "variant.product.id", target = "orderItemVariant.variantProduct.productId")
    @Mapping(source = "variant.product.name", target = "orderItemVariant.variantProduct.productName")
    @Mapping(source = "variant.product.slug", target = "orderItemVariant.variantProduct.productSlug")
    @Mapping(source = "variant.properties", target = "orderItemVariant.variantProperties")
    @Mapping(source = "price", target = "orderItemPrice")
    @Mapping(source = "quantity", target = "orderItemQuantity")
    @Mapping(source = "amount", target = "orderItemAmount")
    public abstract ClientOrderVariantResponse entityToResponse(OrderVariant orderVariant);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "createdAt", target = "orderCreatedAt")
    @Mapping(source = "code", target = "orderCode")
    @Mapping(source = "status", target = "orderStatus")
    @Mapping(source = "toName", target = "orderToName")
    @Mapping(source = "toPhone", target = "orderToPhone")
    @Mapping(source = "toAddress", target = "orderToAddress")
    @Mapping(source = "totalAmount", target = "orderTotalAmount")
    @Mapping(source = "tax", target = "orderTax")
    @Mapping(source = "shippingCost", target = "orderShippingCost")
    @Mapping(source = "totalPay", target = "orderTotalPay")
    @Mapping(source = "paymentMethodType", target = "orderPaymentMethodType")
    @Mapping(source = "paymentStatus", target = "orderPaymentStatus")
    @Mapping(source = "orderVariants", target = "orderItems")
    public abstract ClientOrderDetailResponse entityToDetailResponse(Order order);
}
