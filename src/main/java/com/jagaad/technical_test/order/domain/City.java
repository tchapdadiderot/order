package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record City(@Column(name = "c_city") String value) implements SingleValueObject<String> {
    public City {
        if (value.length() > 100) {
            throw new BadFieldLongerException("City length exceeds 100 characters");
        }
    }
}
