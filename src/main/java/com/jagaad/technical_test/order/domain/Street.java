package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record Street(@Column(name = "c_street") String value) implements SingleValueObject<String> {
    public Street {
        if (value.length() > 100) {
            throw new BadFieldLongerException("Street length exceeds 100 characters");
        }
    }
}
