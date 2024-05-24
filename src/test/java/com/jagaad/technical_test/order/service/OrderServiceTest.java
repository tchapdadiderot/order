package com.jagaad.technical_test.order.service;

import com.jagaad.technical_test.order.common.OrderData;
import com.jagaad.technical_test.order.domain.Order;
import com.jagaad.technical_test.order.domain.OrderFactory;
import com.jagaad.technical_test.order.domain.OrderRepository;
import com.jagaad.technical_test.order.dto.UpdateAnOrderDTO;
import com.jagaad.technical_test.order.dto.OrderDTO;
import com.jagaad.technical_test.order.dto.PlaceAnOrderDTO;
import com.jagaad.technical_test.order.exception.ResourceNotFoundException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest {
    OrderService objectUnderTest;
    private OrderMapper orderMapper;
    private OrderRepository repository;
    private OrderFactory orderFactory;

    @BeforeEach
    void setUp() {
        orderMapper = mock(OrderMapper.class);
        repository = mock(OrderRepository.class);
        orderFactory = mock(OrderFactory.class);
        objectUnderTest = new OrderService(orderMapper, repository, orderFactory);
    }

    @Test
    void givenOrderDataReceived_whenValid_thenCreateSuccessfully() {
        var order = mock(Order.class);
        var orderData = mock(OrderData.class);
        var placeAnOrderDTO = mock(PlaceAnOrderDTO.class);
        var orderDTO = mock(OrderDTO.class);

        when(orderMapper.placeAnOrderDataToOrderData(placeAnOrderDTO)).thenReturn(orderData);
        when(orderMapper.orderToDto(order)).thenReturn(orderDTO);
        when(orderFactory.createOrder(orderData, repository)).thenReturn(order);

        var response = objectUnderTest.placeAnOrder(placeAnOrderDTO);
        assertThat(response).isSameAs(orderDTO);
    }

    @SneakyThrows
    @Test
    void givenOrderNumberAndOrderData_whenOrderExistInsideDatabase_thenUpdateSuccessfully() {
        var order = mock(Order.class);
        var orderData = mock(OrderData.class);
        var updateAnOrderDTO = mock(UpdateAnOrderDTO.class);
        String invoiceNumber = "B 0000 100 001";

        when(repository.findOrderByOrderNumber(invoiceNumber)).thenReturn(Optional.of(order));
        when(orderMapper.UpdateAnOrderDtoToOrderData(updateAnOrderDTO)).thenReturn(orderData);

        objectUnderTest.updateOrder(invoiceNumber,updateAnOrderDTO);

        verify(order).update(orderData, repository);
    }

    @Test
    void givenOrderNumberAndOrderData_whenOrderNotExistInsideDatabase_thenThrowException() {
        var updateAnOrderDTO = mock(UpdateAnOrderDTO.class);
        String invoiceNumber = "B 0000 100 001";

        when(repository.findOrderByOrderNumber(invoiceNumber)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> objectUnderTest.updateOrder(invoiceNumber, updateAnOrderDTO))
                .isExactlyInstanceOf(ResourceNotFoundException.class)
                .hasMessage(STR."Order number \{invoiceNumber} not found");

    }

    @Test
    void givenOrderSearchRequestArrives_shouldBeForwardedToTheRepository() {
        var searchText = "rod";
        var orderDTO1 = mock(OrderDTO.class);
        var orderDTO2 = mock(OrderDTO.class);

        var order1 = mock(Order.class);
        var order2 = mock(Order.class);

        when(orderMapper.orderToDto(order1)).thenReturn(orderDTO1);
        when(orderMapper.orderToDto(order2)).thenReturn(orderDTO2);

        when(repository.searchOrdersByCustomerData(searchText)).thenReturn(List.of(order1, order2));

        var orders = objectUnderTest.searchOrdersByCustomerData(searchText);

        assertThat(orders)
                .hasSize(2)
                .contains(orderDTO1)
                .contains(orderDTO2)
        ;
    }

    @Test
    void givenOrderSearchRequestArrives_whenSearchTextIsEmpty_thenReturnAllOrders() {
        var searchText = "";
        var orderDTO1 = mock(OrderDTO.class);
        var orderDTO2 = mock(OrderDTO.class);

        var order1 = mock(Order.class);
        var order2 = mock(Order.class);

        when(orderMapper.orderToDto(order1)).thenReturn(orderDTO1);
        when(orderMapper.orderToDto(order2)).thenReturn(orderDTO2);

        when(repository.allOrders()).thenReturn(List.of(order1, order2));

        var orders = objectUnderTest.searchOrdersByCustomerData(searchText);

        assertThat(orders)
                .hasSize(2)
                .contains(orderDTO1)
                .contains(orderDTO2)
        ;
    }
}