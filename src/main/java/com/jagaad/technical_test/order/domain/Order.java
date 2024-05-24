package com.jagaad.technical_test.order.domain;

import com.jagaad.technical_test.order.common.OrderData;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.javamoney.moneta.Money;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_order")
public class Order {
    @EmbeddedId
    private OrderId id;
    @Embedded
    private OrderNumber orderNumber;
    @Embedded
    private Firstname firstname;
    @Embedded
    private Lastname lastname;
    @Embedded
    private PhoneNumber phone;
    @Embedded
    private DeliveryAddress deliveryAddress;
    @Column(name = "c_total")
    @Convert(converter = MoneyConverter.class)
    private Money total;
    @Setter(AccessLevel.NONE)
    @Enumerated(EnumType.STRING)
    @Column(name = "c_product")
    private Product product;
    @Embedded
    private OrderTime createdAt;
    @Builder
    public Order(Firstname firstname,
                 Lastname lastname,
                 PhoneNumber phone,
                 DeliveryAddress deliveryAddress,
                 Product product,
                 Integer orderNumber) {
        this.id = new OrderId();
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.product = product;
        this.orderNumber = new OrderNumber(orderNumber);
        this.createdAt = new OrderTime(LocalDateTime.now());
        this.total = product.price();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void update(OrderData data, OrderRepository orderRepository) throws UpdateTimeOverException {
        createdAt.ensureExistForLesserThanFiveMinutes();
        this.firstname = new Firstname(data.firstname());
        this.lastname = new Lastname(data.lastname());
        setProduct(Product.valueOf(data.product()));
        this.total = this.product.price();
        this.phone = new PhoneNumber(data.phoneCountryCode(), data.phoneNumber());
        this.deliveryAddress = new DeliveryAddress(
                new City(data.city()),
                new PostalCode(data.postalCode()),
                new Street(data.street())
        );

        orderRepository.save(this);
    }

    public void setProduct(Product product) {
        this.product = product;
        this.total = product.price();
    }
}
