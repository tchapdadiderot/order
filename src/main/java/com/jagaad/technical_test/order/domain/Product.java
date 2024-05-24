package com.jagaad.technical_test.order.domain;


import org.javamoney.moneta.Money;

public enum Product {
    FIVE(5), TEN(10), FIFTEEN(15);

    private final int value;

    Product(int value) {
        this.value = value;
    }

    public Money price() {
        return Money.of(1.33d, "EUR").multiply(value);
    }
}
