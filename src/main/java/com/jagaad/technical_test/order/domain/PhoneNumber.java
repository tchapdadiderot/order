package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.exception.PhoneNumberIllegalArgumentException;
import jakarta.persistence.Column;
import org.apache.commons.lang3.StringUtils;

public record PhoneNumber(
        @Column(name = "c_phone_country_code") String country,
        @Column(name = "c_phone_number") String number
) {
    public String full() {
        return STR."00\{country}\{number}";
    }

    public PhoneNumber {
        if (StringUtils.isEmpty(country)) {
            throw new PhoneNumberIllegalArgumentException("Country Code should be set");
        }
        if (!StringUtils.isNumeric(country)) {
            throw new PhoneNumberIllegalArgumentException("Country code should be a numeric");
        }
        if (StringUtils.isEmpty(number)) {
            throw new PhoneNumberIllegalArgumentException("number should be set");
        }
        if (!StringUtils.isNumeric(number)) {
            throw new PhoneNumberIllegalArgumentException("Number should be a numeric");
        }
    }
}
