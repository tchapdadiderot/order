package com.jagaad.technical_test.order.service;

import com.jagaad.technical_test.order.domain.Order;
import com.jagaad.technical_test.order.domain.OrderNumber;
import com.jagaad.technical_test.order.repository.OrderSpringRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderRepositoryImplTest {

    OrderRepositoryImpl objectUnderTest;
    private OrderSpringRepository orderSpringRepository;

    @BeforeEach
    void setUp() {
        orderSpringRepository = mock(OrderSpringRepository.class);
        objectUnderTest = new OrderRepositoryImpl(orderSpringRepository);
    }

    @Test
    public void givenThereIsRequestToSaveAnOrder_thenThisRequestIsForwardedToTheSpringRepository() {
        var order = mock(Order.class);

        objectUnderTest.save(order);

        verify(orderSpringRepository).save(order);
    }

    @Test
    public void givenTheNextOrderNumberIsRequested_thenThisRequestIsForwardedToSpringRepository() {
        var expected = 2;
        when(orderSpringRepository.nextFreeOrderNumberForH2()).thenReturn(expected);

        var result = objectUnderTest.nextFreeOrderNumber();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void givenTheOrderNumberAsString_whenIsCorrect_thenThisRequestIsForwardedToSpringRepository() {
        String orderNumber = "B 0000 100 001";
        objectUnderTest.findOrderByOrderNumber(orderNumber);

        verify(orderSpringRepository).findByOrderNumber(new OrderNumber(orderNumber));
    }

    @Test
    void givenAllOrdersAreRequest_thenForwardItToSpringRepository() {
        var order1 = mock(Order.class);
        var order2 = mock(Order.class);

        when(orderSpringRepository.findAll()).thenReturn(List.of(order1, order2));

        var orders = objectUnderTest.allOrders();

        assertThat(orders)
                .hasSize(2)
                .contains(order1)
                .contains(order2)
        ;
    }

    @Test
    void givenOrdersAreRequestedByCustomerData_thenForwardItToSpringRepository() {
        var order1 = mock(Order.class);
        var order2 = mock(Order.class);

        var searchText = "rod";

        when(orderSpringRepository.searchOrderByCustomerData(searchText)).thenReturn(List.of(order1, order2));

        var orders = objectUnderTest.searchOrdersByCustomerData(searchText);
        assertThat(orders)
                .hasSize(2)
                .contains(order1)
                .contains(order2);
    }

}