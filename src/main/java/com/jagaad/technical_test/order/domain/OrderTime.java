package com.jagaad.technical_test.order.domain;

import jakarta.persistence.Column;

import java.time.Duration;
import java.time.LocalDateTime;

public record OrderTime(@Column(name="c_created_at") LocalDateTime value) implements SingleValueObject<LocalDateTime> {

    public void ensureExistForLesserThanFiveMinutes() throws UpdateTimeOverException {
        if (Duration.between(LocalDateTime.now(), value).abs().compareTo(Duration.ofMinutes(5)) > 0) {
            throw new UpdateTimeOverException("Order can not be changed after 5 minutes of creation");
        }
    }
}
