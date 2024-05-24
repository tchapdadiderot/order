package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.common.OrderData;

public interface OrderFactory {
    Order createOrder(OrderData data, OrderRepository repository);
}
