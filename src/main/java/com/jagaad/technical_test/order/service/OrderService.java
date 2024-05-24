package com.jagaad.technical_test.order.service;

import com.jagaad.technical_test.order.domain.Order;
import com.jagaad.technical_test.order.domain.OrderFactory;
import com.jagaad.technical_test.order.domain.OrderRepository;
import com.jagaad.technical_test.order.dto.OrderDTO;
import com.jagaad.technical_test.order.dto.PlaceAnOrderDTO;
import com.jagaad.technical_test.order.dto.UpdateAnOrderDTO;
import com.jagaad.technical_test.order.domain.UpdateTimeOverException;
import com.jagaad.technical_test.order.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;

    @Transactional
    public OrderDTO placeAnOrder(PlaceAnOrderDTO orderData) {
        var order = orderFactory.createOrder(
                orderMapper.placeAnOrderDataToOrderData(orderData),
                orderRepository
        );
        return orderMapper.orderToDto(order);
    }

    @Transactional
    public void updateOrder(String invoiceNumber, UpdateAnOrderDTO updateAnOrderDTO) throws UpdateTimeOverException {
        Order order = Optional.ofNullable(invoiceNumber)
                .flatMap(orderRepository::findOrderByOrderNumber)
                .orElseThrow(() -> new ResourceNotFoundException(STR."Order number \{invoiceNumber} not found"));

        order.update(
                orderMapper.UpdateAnOrderDtoToOrderData(updateAnOrderDTO),
                orderRepository
        );
    }

    public List<OrderDTO> searchOrdersByCustomerData(String searchText) {
        if (StringUtils.isEmpty(searchText)) {
            return fetchAllOrders();
        }
        return mapOrdersToDto(orderRepository.searchOrdersByCustomerData(searchText));
    }

    private @NotNull List<OrderDTO> fetchAllOrders() {
        return mapOrdersToDto(orderRepository.allOrders());
    }

    private @NotNull List<OrderDTO> mapOrdersToDto(List<Order> orders) {
        return orders
                .stream()
                .map(orderMapper::orderToDto)
                .toList();
    }
}
