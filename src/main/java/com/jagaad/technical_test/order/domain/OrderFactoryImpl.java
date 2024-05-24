package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.common.OrderData;

public class OrderFactoryImpl implements OrderFactory {
    public Order createOrder(OrderData data, OrderRepository repository) {
        var order = Order.builder()
                .firstname(new Firstname(data.firstname()))
                .lastname(new Lastname(data.lastname()))
                .product(Product.valueOf(data.product()))
                .phone(new PhoneNumber(data.phoneCountryCode(), data.phoneNumber()))
                .deliveryAddress(new DeliveryAddress(
                        new City(data.city()),
                        new PostalCode(data.postalCode()),
                        new Street(data.street())
                ))
                .orderNumber(repository.nextFreeOrderNumber())
                .build();
        repository.save(order);
        return order;
    }
}
