package com.jagaad.technical_test.order.domain;

import java.util.Optional;

import java.util.List;

public interface OrderRepository {
    void save(Order order);

    Integer nextFreeOrderNumber();

    Optional<Order> findOrderByOrderNumber(String orderNumberAsString);

    List<Order> searchOrdersByCustomerData(String searchText);

    List<Order> allOrders();
}
