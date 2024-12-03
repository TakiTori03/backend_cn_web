package com.hust.Ecommerce.services.order;

import com.hust.Ecommerce.dtos.client.order.ClientConfirmedOrderResponse;
import com.hust.Ecommerce.dtos.client.order.ClientSimpleOrderRequest;
import com.hust.Ecommerce.dtos.client.order.OrderRequest;
import com.hust.Ecommerce.dtos.client.order.OrderResponse;

import com.hust.Ecommerce.services.CrudService;

public interface OrderService extends CrudService<Long, OrderRequest, OrderResponse> {
    // public void cancelOrder(String code);

    ClientConfirmedOrderResponse createClientOrder(ClientSimpleOrderRequest request);
}
