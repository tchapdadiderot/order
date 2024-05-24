package com.jagaad.technical_test.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record OrderNumber(@Column(name = "c_order_number") Integer value) implements SingleValueObject<Integer> {
    public OrderNumber(String value) {
        this(Integer.parseInt(
                new StringBuffer(value)
                        .deleteCharAt(0)
                        .toString()
                        .replaceAll(" ", "")
        ));
    }

    public String businessValue() {
        return new StringBuilder(String.format("%010d", value))
                .insert(4, " ")
                .insert(8, " ")
                .insert(0, "B ")
                .toString();
    }
}
