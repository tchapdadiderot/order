package com.jagaad.technical_test.order.domain;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void givenFiveIsChoosen_ThenPriceShould6_65() {
        assertThat(Product.FIVE.price()).isEqualTo(Money.of(6.65d, "EUR"));
    }

    @Test
    void givenTenIsChoosen_ThenPriceShould13_30() {
        assertThat(Product.TEN.price()).isEqualTo(Money.of(13.30d, "EUR"));
    }

    @Test
    void givenFifteenIsChoosen_ThenPriceShould19_95() {
        assertThat(Product.FIFTEEN.price()).isEqualTo(Money.of(19.95d, "EUR"));
    }
}