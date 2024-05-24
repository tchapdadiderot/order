package com.jagaad.technical_test.order.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public record DeliveryAddress(
        @Embedded City city,
        @Embedded PostalCode postalCode,
        @Embedded Street street
) {
}
