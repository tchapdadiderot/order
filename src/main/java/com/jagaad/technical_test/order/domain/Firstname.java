package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record Firstname(@NotNull @Column(name = "c_firstname") String value) implements SingleValueObject<String> {
    public Firstname {
        if (value.length() > 100) {
            throw new BadFieldLongerException("Firstname length exceeds 100 characters");
        }
    }
}
