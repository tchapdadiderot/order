package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LastnameTest {

    @Test
    void givenValue_whenIsCorrect_thenBuildSuccessfully() {
        String value = "Lagoue";
        Lastname lastname = new Lastname(value);

        assertThat(lastname)
                .isNotNull()
                .returns(value, Lastname::value);
    }

    @Test
    void givenValue_whenBadLonger_thenThrowException() {
        String value = "LagoueLagoueLagoueLagoueLagoueLagoueLagoueLagoueLag" +
                "oueLagoueLagoueLagoueLagoueLagoueLagoueLagoueLagoueLagou" +
                "eLagoueLagoueLagoueLagoueLagoueLagoueLagoueLagoueLa" +
                "goueLagoueLagoueLagoueLagoueLagoueLagoueLagoueLag" +
                "oueLagoueLagoueLagoueLagoueLagoue";
        assertThatThrownBy(() -> new Lastname(value))
                .isExactlyInstanceOf(BadFieldLongerException.class)
                .hasMessage("Lastname length exceeds 100 characters");
    }
}