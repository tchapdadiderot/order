package com.jagaad.technical_test.order.common;

public record OrderData(
        String firstname,
        String lastname,
        String phoneCountryCode,
        String phoneNumber,
        String product,
        String street,
        String postalCode,
        String city
) {
}
