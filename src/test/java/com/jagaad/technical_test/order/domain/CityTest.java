package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.BadFieldLongerException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CityTest {

    @Test
    void givenValue_whenIsCorrect_thenBuildSuccessfully() {
        String value = "Douala";
        City city = new City(value);

        assertThat(city)
                .isNotNull()
                .returns(value, City::value);
    }

    @Test
    void givenValue_whenBadLonger_thenThrowException() {
        String value = "DoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDou" +
                "alaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDo" +
                "ualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDou" +
                "alaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDou" +
                "alaDoualaDoualaDoualaDoualaDoualaDoualaDoualaDouala";
        assertThatThrownBy(() -> new City(value))
                .isExactlyInstanceOf(BadFieldLongerException.class)
                .hasMessage("City length exceeds 100 characters");
    }
}