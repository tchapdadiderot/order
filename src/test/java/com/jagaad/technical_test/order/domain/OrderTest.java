package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.common.OrderData;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

class OrderTest {
    Order objectUnderTest;

    @SneakyThrows
    @Test
    void givenOrderData_whenValidAndCreationIsBeforeFiveMinutes_thenUpdateSuccessfully() {
        var firstname = "Rodrigue";
        var lastname = "Lagoue";
        var phoneCountryCode = "237";
        var phoneNumber = "653492410";
        var product = "TEN";
        var street = "Basson";
        var postalCode = "9911";
        var city = "Douala";
        var orderNumber = 5;
        OrderId orderId = new OrderId();
        objectUnderTest = Order.builder()
                .firstname(new Firstname(firstname))
                .lastname(new Lastname(lastname))
                .product(Product.valueOf(product))
                .phone(new PhoneNumber(phoneCountryCode, phoneNumber))
                .deliveryAddress(new DeliveryAddress(
                        new City(city),
                        new PostalCode(postalCode),
                        new Street(street)
                ))
                .orderNumber(orderNumber)
                .build();
        objectUnderTest.setId(orderId);
        var newFirstname = "Rodrigue";
        var newLastname = "Lagoue";
        var newPhoneCountryCode = "237";
        var newPhoneNumber = "653492410";
        var newProduct = "FIFTEEN";
        var newStreet = "Basson";
        var newPostalCode = "9911";
        var newCity = "Douala";
        OrderData data = new OrderData(
                newFirstname,
                newLastname,
                newPhoneCountryCode,
                newPhoneNumber,
                newProduct,
                newStreet,
                newPostalCode,
                newCity
        );
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderTime orderTime = new OrderTime(LocalDateTime.now().minusMinutes(2));
        objectUnderTest.setCreatedAt(orderTime);

        objectUnderTest.update(data, orderRepository);

        verify(orderRepository).save(argThat(argument -> {
            Assertions.assertThat(argument)
                    .isNotNull()
                    .returns(orderId, Order::getId)
                    .returns(orderTime, Order::getCreatedAt)
                    .returns(new OrderNumber(orderNumber), Order::getOrderNumber)
                    .returns(new Firstname(newFirstname), Order::getFirstname)
                    .returns(new Lastname(newLastname), Order::getLastname)
                    .returns(new PhoneNumber(newPhoneCountryCode, newPhoneNumber), Order::getPhone)
                    .returns(Product.valueOf(newProduct), Order::getProduct)
                    .returns(Money.of(19.95d, "EUR"), Order::getTotal)
                    .returns(
                            new DeliveryAddress(new City(newCity), new PostalCode(newPostalCode), new Street(newStreet)),
                            Order::getDeliveryAddress
                    );

            return true;
        }));

    }

    @Test
    void givenOrderData_whenValidAndCreationIsAfterFiveMinutes_thenThrowException() {
        var firstname = "Rodrigue";
        var lastname = "Lagoue";
        var phoneCountryCode = "237";
        var phoneNumber = "653492410";
        var product = "FIVE";
        var street = "Basson";
        var postalCode = "9911";
        var city = "Douala";
        var orderNumber = 5;
        OrderId orderId = new OrderId();
        objectUnderTest = Order.builder()
                .firstname(new Firstname(firstname))
                .lastname(new Lastname(lastname))
                .product(Product.valueOf(product))
                .phone(new PhoneNumber(phoneCountryCode, phoneNumber))
                .deliveryAddress(new DeliveryAddress(
                        new City(city),
                        new PostalCode(postalCode),
                        new Street(street)
                ))
                .orderNumber(orderNumber)
                .build();
        objectUnderTest.setId(orderId);
        var newFirstname = "Rodrigue";
        var newLastname = "Lagoue";
        var newPhoneCountryCode = "237";
        var newPhoneNumber = "653492410";
        var newProduct = "TEN";
        var newStreet = "Basson";
        var newPostalCode = "9911";
        var newCity = "Douala";
        OrderData data = new OrderData(
                newFirstname,
                newLastname,
                newPhoneCountryCode,
                newPhoneNumber,
                newProduct,
                newStreet,
                newPostalCode,
                newCity
        );
        OrderRepository orderRepository = mock(OrderRepository.class);
        OrderTime orderTime = new OrderTime(LocalDateTime.now().minusMinutes(6));
        objectUnderTest.setCreatedAt(orderTime);

        assertThatThrownBy(() -> objectUnderTest.update(data, orderRepository))
                .isExactlyInstanceOf(UpdateTimeOverException.class)
                .hasMessage(STR."Order can not be changed after 5 minutes of creation");

        verifyNoInteractions(orderRepository);

    }

    @Test
    void givenProduct_whenValid_thenUpdateProductAndTotalFields() {
        objectUnderTest = new Order();

        objectUnderTest.setProduct(Product.FIFTEEN);

        assertThat(objectUnderTest.getProduct()).isEqualTo(Product.FIFTEEN);
        assertThat(objectUnderTest.getTotal()).isEqualTo(Money.of(19.95d, "EUR"));
    }
}