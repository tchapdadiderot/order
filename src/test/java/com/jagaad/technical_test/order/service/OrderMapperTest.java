package com.jagaad.technical_test.order.service;

import com.jagaad.technical_test.order.common.OrderData;
import com.jagaad.technical_test.order.domain.City;
import com.jagaad.technical_test.order.domain.DeliveryAddress;
import com.jagaad.technical_test.order.domain.Firstname;
import com.jagaad.technical_test.order.domain.Lastname;
import com.jagaad.technical_test.order.domain.Order;
import com.jagaad.technical_test.order.domain.OrderNumber;
import com.jagaad.technical_test.order.domain.PhoneNumber;
import com.jagaad.technical_test.order.domain.PostalCode;
import com.jagaad.technical_test.order.domain.Product;
import com.jagaad.technical_test.order.domain.Street;
import com.jagaad.technical_test.order.dto.AddressDTO;
import com.jagaad.technical_test.order.dto.MoneyDTO;
import com.jagaad.technical_test.order.dto.OrderDTO;
import com.jagaad.technical_test.order.dto.PiloteQuantity;
import com.jagaad.technical_test.order.dto.PlaceAnOrderDTO;
import com.jagaad.technical_test.order.dto.UpdateAnOrderDTO;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderMapperTest {

    OrderMapper objectUnderTest = new OrderMapperImpl();

    @Test
    void givenThePlaceAnOrderData_WhenRequired_ThenMapToOrderDataSuccessfully() {
        var firstname = "Rodrigue";
        var lastname = "Lagoue";
        var phoneCountryCode = "237";
        var phoneNumber = "653492410";
        var quantity = PiloteQuantity.FIVE;
        var city = "Douala";
        var street = "Carrefour Bassong";
        var postalCode = "9911";
        var result = objectUnderTest.placeAnOrderDataToOrderData(
                new PlaceAnOrderDTO()
                        .firstname(firstname)
                        .lastname(lastname)
                        .phoneCountryCode(phoneCountryCode)
                        .phoneNumber(phoneNumber)
                        .quantity(quantity)
                        .deliveryAddress(new AddressDTO().city(city).street(street).postalCode(postalCode))
        );
        assertThat(result)
                .isNotNull()
                .returns(firstname, OrderData::firstname)
                .returns(lastname, OrderData::lastname)
                .returns(phoneCountryCode, OrderData::phoneCountryCode)
                .returns(phoneNumber, OrderData::phoneNumber)
                .returns(quantity.name(), OrderData::product)
                .returns(city, OrderData::city)
                .returns(street, OrderData::street)
                .returns(postalCode, OrderData::postalCode)
        ;
    }

    @Test
    void givenAnOrder_WhenRequired_ThenMapToOrderDTOSuccessfully() {
        var firstname = "Rodrigue";
        var lastname = "Lagoue";
        var phoneCountryCode = "237";
        var phoneNumber = "653492410";
        var quantity = PiloteQuantity.TEN;
        var city = "Douala";
        var street = "Carrefour Bassong";
        var postalCode = "9911";

        var order = mock(Order.class);
        when(order.getTotal()).thenReturn(Money.of(13.3d, "EUR"));
        when(order.getFirstname()).thenReturn(new Firstname(firstname));
        when(order.getLastname()).thenReturn(new Lastname(lastname));
        when(order.getPhone()).thenReturn(new PhoneNumber("237", "653492410"));
        when(order.getProduct()).thenReturn(Product.TEN);
        when(order.getOrderNumber()).thenReturn(new OrderNumber(1));
        when(order.getDeliveryAddress())
                .thenReturn(new DeliveryAddress(new City(city), new PostalCode(postalCode), new Street(street)));

        var orderDTO = objectUnderTest.orderToDto(order);

        assertThat(orderDTO)
                .isNotNull()
                .returns(firstname, OrderDTO::getFirstname)
                .returns(lastname, OrderDTO::getLastname)
                .returns(STR."00\{phoneCountryCode}\{phoneNumber}", OrderDTO::getPhone)
                .returns(quantity, OrderDTO::getQuantity)
                .returns(new MoneyDTO().currency("EUR").value(BigDecimal.valueOf(13.3d)), OrderDTO::getTotal)
                .returns(
                        new AddressDTO().city(city).street(street).postalCode(postalCode),
                        OrderDTO::getDeliveryAddress
                )
        ;
    }

    @Test
    void givenTheUpdateAnOrderData_WhenRequired_ThenMapToOrderDataSuccessfully() {
        var firstname = "Rodrigue";
        var lastname = "Lagoue";
        var phoneCountryCode = "237";
        var phoneNumber = "653492410";
        var quantity = PiloteQuantity.FIVE;
        var city = "Douala";
        var street = "Carrefour Bassong";
        var postalCode = "9911";
        var result = objectUnderTest.UpdateAnOrderDtoToOrderData(
                new UpdateAnOrderDTO()
                        .firstname(firstname)
                        .lastname(lastname)
                        .phoneCountryCode(phoneCountryCode)
                        .phoneNumber(phoneNumber)
                        .quantity(quantity)
                        .deliveryAddress(new AddressDTO().city(city).street(street).postalCode(postalCode))
        );
        assertThat(result)
                .isNotNull()
                .returns(firstname, OrderData::firstname)
                .returns(lastname, OrderData::lastname)
                .returns(phoneCountryCode, OrderData::phoneCountryCode)
                .returns(phoneNumber, OrderData::phoneNumber)
                .returns(quantity.name(), OrderData::product)
                .returns(city, OrderData::city)
                .returns(street, OrderData::street)
                .returns(postalCode, OrderData::postalCode)
        ;
    }

}