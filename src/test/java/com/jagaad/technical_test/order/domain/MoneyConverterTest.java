package com.jagaad.technical_test.order.domain;

import org.junit.jupiter.api.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyConverterTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    public MoneyConverter objectToTest = new MoneyConverter();

    @Test
    public void convertToDatabaseColumnTest() {
        MonetaryAmount money = Monetary.getDefaultAmountFactory()
                .setCurrency(EUR.getCurrencyCode())
                .setNumber(20)
                .create();

        String databaseColumnValue = objectToTest.convertToDatabaseColumn(money);

        assertThat(databaseColumnValue).isEqualTo("EUR;20.0");
    }

    @Test
    public void convertToDatabaseColumnNullArgumentTest() {
        assertThat(objectToTest.convertToDatabaseColumn(null)).isNull();
    }

    @Test
    public void convertToEntityAttributeTest() {
        var entityAttribute = objectToTest.convertToEntityAttribute("EUR;20");

        assertThat(entityAttribute)
                .isNotNull()
                .returns(EUR.getCurrencyCode(), monetaryAmount -> monetaryAmount.getCurrency().getCurrencyCode())
                .returns(20, monetaryAmount -> monetaryAmount.getNumber().intValue())
                .returns(20d, monetaryAmount -> monetaryAmount.getNumber().doubleValue());
    }

    @Test
    public void convertToEntityAttributeNullTest() {
        assertNull(objectToTest.convertToEntityAttribute(null));
    }

    @Test
    public void convertToEntityAttributeEmptyTest() {
        assertNull(objectToTest.convertToEntityAttribute(""));
    }

    @Test
    public void convertToEntityAttributeMalformedTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> objectToTest.convertToEntityAttribute("EUR;;;4"));
        assertEquals(
                "'EUR;;;4' is an illegal argument for Money object creation",
                exception.getMessage()
        );
    }

}