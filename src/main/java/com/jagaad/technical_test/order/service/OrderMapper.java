package com.jagaad.technical_test.order.service;

import com.jagaad.technical_test.order.common.OrderData;
import com.jagaad.technical_test.order.domain.Order;
import com.jagaad.technical_test.order.domain.OrderNumber;
import com.jagaad.technical_test.order.domain.OrderTime;
import com.jagaad.technical_test.order.domain.PhoneNumber;
import com.jagaad.technical_test.order.domain.Product;
import com.jagaad.technical_test.order.domain.SingleValueObject;
import com.jagaad.technical_test.order.dto.MoneyDTO;
import com.jagaad.technical_test.order.dto.OrderDTO;
import com.jagaad.technical_test.order.dto.PiloteQuantity;
import com.jagaad.technical_test.order.dto.PlaceAnOrderDTO;
import com.jagaad.technical_test.order.dto.UpdateAnOrderDTO;
import org.javamoney.moneta.Money;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Mapper(uses = {}, componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstname")
    @Mapping(target = "lastname")
    @Mapping(target = "phoneCountryCode")
    @Mapping(target = "phoneNumber")
    @Mapping(target = "street", source = "deliveryAddress.street")
    @Mapping(target = "city", source = "deliveryAddress.city")
    @Mapping(target = "postalCode", source = "deliveryAddress.postalCode")
    @Mapping(target = "product", source = "quantity")
    OrderData placeAnOrderDataToOrderData(PlaceAnOrderDTO input);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "orderNumber")
    @Mapping(target = "firstname")
    @Mapping(target = "lastname")
    @Mapping(target = "phone")
    @Mapping(target = "total")
    @Mapping(target = "quantity", source = "product")
    @Mapping(target = "deliveryAddress")
    @Mapping(target = "createdAt")
    OrderDTO orderToDto(Order input);

    default LocalDateTime createdAt(OrderTime input) {
        return Optional.ofNullable(input).map(OrderTime::value).orElse(null);
    }

    default PiloteQuantity productToQuantity(Product input) {
        return Optional.ofNullable(input).map(Enum::name).map(PiloteQuantity::fromValue).orElse(null);
    }

    default MoneyDTO moneyToMoneyDTO(Money input) {
        return Optional.ofNullable(input)
                .map(
                        it -> new MoneyDTO()
                                .currency(it.getCurrency().getCurrencyCode())
                                .value(it.getNumber().numberValue(BigDecimal.class))
                )
                .orElse(null);

    }

    default String phone(PhoneNumber input) {
        return Optional.ofNullable(input).map(PhoneNumber::full).orElse(null);
    }

    default String orderNumber(OrderNumber input) {
        return Optional.ofNullable(input).map(OrderNumber::businessValue).orElse(null);
    }

    default String singleValueObjectToString(SingleValueObject<String> input) {
        return Optional.ofNullable(input).map(SingleValueObject::value).orElse(null);
    }

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "firstname")
    @Mapping(target = "lastname")
    @Mapping(target = "phoneCountryCode")
    @Mapping(target = "phoneNumber")
    @Mapping(target = "street", source = "deliveryAddress.street")
    @Mapping(target = "city", source = "deliveryAddress.city")
    @Mapping(target = "postalCode", source = "deliveryAddress.postalCode")
    @Mapping(target = "product", source = "quantity")
    OrderData UpdateAnOrderDtoToOrderData(UpdateAnOrderDTO value);
}
