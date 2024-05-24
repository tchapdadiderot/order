package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.PhoneNumberIllegalArgumentException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneNumberTest {

    @Test
    public void givenPhoneNumberData_whenValid_thenBuildSuccessfully() {
        assertThat(new PhoneNumber("49", "2164866").full())
                .isEqualTo("00492164866");
    }

    @Test
    public void givenPhoneNumberData_whenCountryCodeIsNull_thenThrowPhoneNumberIllegalArgumentException() {
        assertThatThrownBy(() -> new PhoneNumber(null, "2164866"))
                .isExactlyInstanceOf(PhoneNumberIllegalArgumentException.class)
                .hasMessage("Country Code should be set");
    }

    @Test
    public void givenPhoneNumberData_whenCountryCodeIsAnEmptyString_thenThrowPhoneNumberIllegalArgumentException() {
        assertThatThrownBy(() -> new PhoneNumber("", "2164866"))
                .isExactlyInstanceOf(PhoneNumberIllegalArgumentException.class)
                .hasMessage("Country Code should be set");
    }

    @Test
    public void givenPhoneNumberData_whenCountryCodeIsNotADigit_thenThrowPhoneNumberIllegalArgumentException() {
        assertThatThrownBy(() -> new PhoneNumber("a5", "2164866"))
                .isExactlyInstanceOf(PhoneNumberIllegalArgumentException.class)
                .hasMessage("Country code should be a numeric");
    }

    @Test
    public void givenPhoneNumberData_whenNumberIsNull_thenThrowPhoneNumberIllegalArgumentException() {
        assertThatThrownBy(() -> new PhoneNumber("49", null))
                .isExactlyInstanceOf(PhoneNumberIllegalArgumentException.class)
                .hasMessage("number should be set");
    }

    @Test
    public void givenPhoneNumberData_whenNumberIsAnEmptyString_thenThrowPhoneNumberIllegalArgumentException() {
        assertThatThrownBy(() -> new PhoneNumber("49", ""))
                .isExactlyInstanceOf(PhoneNumberIllegalArgumentException.class)
                .hasMessage("number should be set");
    }

    @Test
    public void givenPhoneNumberData_whenNumberIsNotADigit_thenThrowPhoneNumberIllegalArgumentException() {
        assertThatThrownBy(() -> new PhoneNumber("49", "216dao4866"))
                .isExactlyInstanceOf(PhoneNumberIllegalArgumentException.class)
                .hasMessage("Number should be a numeric");
    }
}