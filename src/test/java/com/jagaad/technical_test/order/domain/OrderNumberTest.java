package com.jagaad.technical_test.order.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderNumberTest {

    OrderNumber objectUnderTest;

    @Test
    void givenOrderNumberIsXAndSmallerThanTen_thenBusinessOrderNumberIsB_0000_000_00X() {
        objectUnderTest = new OrderNumber(5);
        assertThat(objectUnderTest.businessValue()).isEqualTo("B 0000 000 005");
    }

    @Test
    void givenOrderNumberIsXAndSmallerThanHundrex_thenBusinessOrderNumberIsB_0000_000_0XX() {
        objectUnderTest = new OrderNumber(98);
        assertThat(objectUnderTest.businessValue()).isEqualTo("B 0000 000 098");
    }
}