package com.jagaad.technical_test.order.service;

import com.jagaad.technical_test.order.domain.Order;
import com.jagaad.technical_test.order.domain.OrderNumber;
import com.jagaad.technical_test.order.domain.OrderRepository;
import com.jagaad.technical_test.order.repository.OrderSpringRepository;
import lombok.AllArgsConstructor;

import java.util.Optional;

import java.util.List;

@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderSpringRepository orderSpringRepository;

    @Override
    public void save(Order order) {
        orderSpringRepository.save(order);
    }

    @Override
    public Integer nextFreeOrderNumber() {
        return orderSpringRepository.nextFreeOrderNumberForH2();
    }

    @Override
    public Optional<Order> findOrderByOrderNumber(String orderNumberAsString) {
        return orderSpringRepository.findByOrderNumber(new OrderNumber(orderNumberAsString));
    }

    @Override
    public List<Order> searchOrdersByCustomerData(String searchText) {
        return orderSpringRepository.searchOrderByCustomerData(searchText);
    }

    @Override
    public List<Order> allOrders() {
        return orderSpringRepository.findAll();
    }
}
