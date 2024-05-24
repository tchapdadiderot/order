package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StreetTest {

    @Test
    void givenValue_whenIsCorrect_thenBuildSuccessfully() {
        String value = "Logpom";
        Street street = new Street(value);

        assertThat(street)
                .isNotNull()
                .returns(value, Street::value);
    }

    @Test
    void givenValue_whenBadLonger_thenThrowException() {
        String value = "LogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogp" +
                "omLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogp" +
                "omLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpom" +
                "LogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpom" +
                "LogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpomLogpom";

        assertThatThrownBy(() -> new Street(value))
                .isExactlyInstanceOf(BadFieldLongerException.class)
                .hasMessage("Street length exceeds 100 characters");
    }
}