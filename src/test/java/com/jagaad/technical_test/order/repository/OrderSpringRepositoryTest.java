package com.jagaad.technical_test.order.repository;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("data")
@ActiveProfiles({"data_test"})
@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
class OrderSpringRepositoryTest {
    @Autowired
    private OrderSpringRepository objectUnderTest;
    private String firstname;
    private String lastname;
    private String phoneCountryCode;
    private String phoneNumber;
    private Product product;
    private String street;
    private String postalCode;
    private String city;

    @BeforeEach
    void setUp(){
        firstname = "Rodrigue";
        lastname = "Lagoue";
        phoneCountryCode = "237";
        phoneNumber = "653492410";
        product = Product.FIVE;
        street = "Basson";
        postalCode = "9911";
        city = "Douala";
    }

    @Test
    public void givenNextOrderNumberIsRequested_thenReturnNextOrderNumber() {
        var orderNumber = objectUnderTest.nextFreeOrderNumberForH2();
        assertThat(orderNumber).isGreaterThanOrEqualTo(1);
        assertThat(objectUnderTest.nextFreeOrderNumberForH2()).isEqualTo(orderNumber + 1);
    }

    @Test
    public void givenOrderNumber_thenReturnOrderCorresponding() {
        int newOrderNumber = 8;
        var savedOrder = objectUnderTest.save(
                Order.builder()
                        .orderNumber(newOrderNumber)
                        .firstname(new Firstname(firstname))
                        .lastname(new Lastname(lastname))
                        .phone(new PhoneNumber(phoneCountryCode, phoneNumber))
                        .product(product)
                        .deliveryAddress(new DeliveryAddress(
                                new City(city),
                                new PostalCode(postalCode),
                                new Street(street)
                        ))
                        .build()
        );

        var order = objectUnderTest.findByOrderNumber(new OrderNumber(newOrderNumber)).orElseThrow();

        assertThat(order).isEqualTo(savedOrder);

    }

}