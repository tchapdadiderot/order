package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FirstnameTest {

    @Test
    void givenValue_whenIsCorrect_thenBuildSuccessfully() {
        String value = "Rodrigue";
        Firstname firstname = new Firstname(value);

        assertThat(firstname)
                .isNotNull()
                .returns(value, Firstname::value);
    }

    @Test
    void givenValue_whenBadLonger_thenThrowException() {
        String value = "RodrigueRodrigueRodrigueRodrigueRodrigueRodr" +
                "igueRodrigueRodrigueRodrigueRodrigueRodrigueRodrigueRo" +
                "drigueRodrigueRodrigueRodrigueRodrigueRodrigueRodrigueRod" +
                "rigueRodrigueRodrigueRodrigueRodrigueRodrigueRodri" +
                "gueRodrigueRodrigueRodrigueRodrigueRodrigueR" +
                "odrigueRodrigueRodrigueRodrigueRodrigueRodrigueRodrigue";
        assertThatThrownBy(() -> new Firstname(value))
                .isExactlyInstanceOf(BadFieldLongerException.class)
                .hasMessage("Firstname length exceeds 100 characters");
    }
}