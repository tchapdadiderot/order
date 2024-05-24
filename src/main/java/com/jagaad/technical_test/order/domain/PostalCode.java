package com.jagaad.technical_test.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record PostalCode(@Column(name = "c_postal_code") String value) implements SingleValueObject<String> {
}
