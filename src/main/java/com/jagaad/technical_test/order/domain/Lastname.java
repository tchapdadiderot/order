package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

public record Lastname(@NotNull @Column(name = "c_lastname") String value) implements SingleValueObject<String> {
    public Lastname {
        if (value.length() > 100) {
            throw new BadFieldLongerException("Lastname length exceeds 100 characters");
        }
    }
}
