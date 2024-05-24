package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.common.OrderData;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderFactoryTest {

    OrderFactory objectUnderTest;

    @BeforeEach
    void setUp() {
        objectUnderTest = new OrderFactoryImpl();
    }

    @Test
    void givenValidOrderData_WhenPlaced_ThenOrderIsCreatedSuccessfully() {
        var firstname = "Rodrigue";
        var lastname = "Lagoue";
        var phoneCountryCode = "237";
        var phoneNumber = "653492410";
        var product = "TEN";
        var street = "Basson";
        var postalCode = "9911";
        var city = "Douala";
        var orderRepository = mock(OrderRepository.class);
        var orderNumber = 5;
        when(orderRepository.nextFreeOrderNumber()).thenReturn(orderNumber);

        var order = objectUnderTest.createOrder(
                new OrderData(firstname, lastname, phoneCountryCode, phoneNumber, product, street, postalCode, city),
                orderRepository
        );
        verify(orderRepository).save(order);
        assertThat(order)
                .isNotNull()
                .returns(new Firstname(firstname), Order::getFirstname)
                .returns(new Lastname(lastname), Order::getLastname)
                .returns(new PhoneNumber(phoneCountryCode, phoneNumber), Order::getPhone)
                .returns(Product.valueOf(product), Order::getProduct)
                .returns(Money.of(13.3d, "EUR"), Order::getTotal)
                .returns(
                        new DeliveryAddress(new City(city), new PostalCode(postalCode), new Street(street)),
                        Order::getDeliveryAddress
                )
                .satisfies(
                        it -> assertThat(it.getOrderNumber())
                                .isNotNull()
                                .satisfies(o -> assertThat(o.value()).isEqualTo(orderNumber))
                )
                .satisfies(
                        it -> assertThat(it.getCreatedAt())
                                .isNotNull()
                                .satisfies(
                                        candidate -> assertThat(candidate.value())
                                                .isCloseTo(LocalDateTime.now(), within(2, ChronoUnit.SECONDS))
                                )
                )
        ;
    }


}