package com.jagaad.technical_test.order.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.util.Optional;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<MonetaryAmount, String> {
    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        return Optional.ofNullable(attribute)
                .map(value -> STR."\{value.getCurrency().getCurrencyCode()};\{value.getNumber().doubleValue()}")
                .orElse(null);
    }

    @Override
    public MonetaryAmount convertToEntityAttribute(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        String[] split = value.split(";");
        try {
            return Money.of(Double.parseDouble(split[1]), split[0]);
        } catch (Throwable e) {
            throw new IllegalArgumentException(STR."'\{value}' is an illegal argument for Money object creation", e);
        }
    }
}
