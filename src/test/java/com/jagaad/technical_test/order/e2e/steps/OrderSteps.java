package com.jagaad.technical_test.order.e2e.steps;

import com.jagaad.technical_test.order.dto.AddressDTO;
import com.jagaad.technical_test.order.dto.MoneyDTO;
import com.jagaad.technical_test.order.dto.OrderDTO;
import com.jagaad.technical_test.order.dto.PiloteQuantity;
import com.jagaad.technical_test.order.dto.PlaceAnOrderDTO;
import com.jagaad.technical_test.order.dto.UpdateAnOrderDTO;
import com.jagaad.technical_test.order.e2e.RestClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

@SuppressWarnings("ALL")
public class OrderSteps implements En {
    @Autowired
    RestClient restClient;
    @Autowired
    JdbcTemplate jdbcTemplate;
    private List<OrderDTO> orders;
    private OrderDTO order;
    private LastRequestResponse lastRequestResponse;
    private LocalDateTime now;

    public OrderSteps() {
        And(
                "I place an order with the following details",
                (DataTable dataTable) -> {
                    var data = dataTable.asMaps().getFirst();
                    this.order = restClient.webTestClient()
                            .post()
                            .uri("/public/order")
                            .bodyValue(
                                    new PlaceAnOrderDTO()
                                            .firstname(data.get("firstname"))
                                            .lastname(data.get("lastname"))
                                            .phoneCountryCode(data.get("phoneCountryCode"))
                                            .phoneNumber(data.get("phoneNumber"))
                                            .quantity(PiloteQuantity.fromValue(data.get("quantity")))
                                            .deliveryAddress(
                                                    new AddressDTO()
                                                            .city(data.get("deliveryCity"))
                                                            .postalCode(data.get("deliveryPostalCode"))
                                                            .street(data.get("deliveryStreet"))
                                            )

                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectStatus().isCreated()
                            .expectBody(OrderDTO.class)
                            .returnResult().getResponseBody();
                }
        );
        And(
                "I should receive an invoice with a number respecting the regex {string}",
                (String invoiceRegex) -> Pattern.compile(invoiceRegex).asPredicate().test(order.getOrderNumber())
        );
        And(
                "I should see that the invoice was created lesser than {int} seconds ago",
                (Integer seconds) ->
                        assertThat(this.order.getCreatedAt())
                                .isCloseTo(LocalDateTime.now(), within(seconds, ChronoUnit.SECONDS))
        );
        And(
                "I should see that the invoice total is € {double}",
                (Double moneyAmount) -> assertThat(this.order.getTotal())
                        .isEqualTo(new MoneyDTO().currency("EUR").value(BigDecimal.valueOf(moneyAmount)))
        );
        And(
                "I should that the other information on the invoice are the following one",
                (DataTable dataTable) -> {
                    var orderData = dataTable.asMaps().getFirst();
                    assertThat(order)
                            .returns(orderData.get("firstname"), OrderDTO::getFirstname)
                            .returns(orderData.get("lastname"), OrderDTO::getLastname)
                            .returns(orderData.get("phone"), OrderDTO::getPhone)
                            .returns(PiloteQuantity.fromValue(orderData.get("quantity")), OrderDTO::getQuantity)
                            .satisfies(
                                    it -> assertThat(it.getDeliveryAddress())
                                            .returns(orderData.get("deliveryCity"), AddressDTO::getCity)
                                            .returns(orderData.get("deliveryPostalCode"), AddressDTO::getPostalCode)
                                            .returns(orderData.get("deliveryStreet"), AddressDTO::getStreet)
                            );
                }
        );
        And(
                "I should see a line in the database table representing an order with the following data",
                (DataTable dataTable) -> {
                    var orderData = dataTable.asMaps().getFirst();
                    var sqlQueryResult = jdbcTemplate.queryForList("SELECT * FROM t_order");
                    assertThat(sqlQueryResult).anySatisfy(candidate -> assertThat(candidate)
                            .containsEntry("c_firstname", orderData.get("firstname"))
                            .containsEntry("c_lastname", orderData.get("lastname"))
                            .containsEntry("c_phone_country_code", orderData.get("phoneCountryCode"))
                            .containsEntry("c_phone_number", orderData.get("phoneNumber"))
                            .containsEntry("c_product", orderData.get("quantity"))
                            .containsEntry("c_city", orderData.get("deliveryCity"))
                            .containsEntry("c_postal_code", orderData.get("deliveryPostalCode"))
                            .containsEntry("c_street", orderData.get("deliveryStreet"))
                            .satisfies(it -> assertThat(it.get("c_id")).isNotNull())
                            .satisfies(
                                    it -> assertThat(((Timestamp) it.get("c_created_at")).toLocalDateTime())
                                            .isNotNull()
                                            .isEqualToIgnoringNanos(order.getCreatedAt())
                            )
                            .satisfies(
                                    it -> assertThat(it.get("c_order_number"))
                                            .isNotNull()
                                            .isEqualTo(orderNumberToLong(order.getOrderNumber()))
                            )
                    );
                }
        );

        And(
                "I assume the order number {string} is not present in the database",
                (String orderNumber) -> assertThat(jdbcTemplate.queryForList(
                        "SELECT * FROM t_order WHERE c_order_number = ?",
                        orderNumberToLong(orderNumber)
                ))
                        .isEmpty()
        );

        And(
                "The last request failed with the following data",
                (DataTable dataTable) -> {
                    Map<String, String> data = dataTable.asMaps().getFirst();

                    assertThat(lastRequestResponse)
                            .returns(
                                    HttpStatus.valueOf(data.get("status").toUpperCase()),
                                    LastRequestResponse::status
                            )
                            .returns(data.get("body"), LastRequestResponse::responseBody);
                }
        );

        And(
                "I assume the order with the following data exists {int} seconds ago in the database",
                (Integer exectedAgeInSeconds, DataTable dataTable) -> {
                    Map<String, String> data = dataTable.asMaps().getFirst();
                    jdbcTemplate.update("""
                                    INSERT INTO t_order (
                                           c_id,c_order_number,c_created_at,c_firstname,c_lastname,c_phone_country_code,
                                           c_phone_number,c_product,c_total,c_city,c_postal_code,c_street
                                    ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                                                             """,
                            UUID.randomUUID().toString(),
                            orderNumberToLong(data.get("orderNumber")),
                            LocalDateTime.now().minusSeconds(exectedAgeInSeconds),
                            data.get("firstname"),
                            data.get("lastname"),
                            data.get("phoneCountryCode"),
                            data.get("phoneNumber"),
                            data.get("quantity"),
                            data.get("total"),
                            data.get("deliveryCity"),
                            data.get("deliveryPostalCode"),
                            data.get("deliveryStreet")
                    );

                }
        );

        And(
                "I update the order number {string} with the following details",
                (String orderNumber, DataTable dataTable) -> {
                    Map<String, String> data = dataTable.asMaps().getFirst();
                    restClient
                            .webTestClient()
                            .put()
                            .uri("/public/order/" + orderNumber)
                            .bodyValue(
                                    new UpdateAnOrderDTO()
                                            .firstname(data.get("firstname"))
                                            .lastname(data.get("lastname"))
                                            .phoneCountryCode(data.get("phoneCountryCode"))
                                            .phoneNumber(data.get("phoneNumber"))
                                            .quantity(PiloteQuantity.fromValue(data.get("quantity")))
                                            .deliveryAddress(
                                                    new AddressDTO()
                                                            .city(data.get("deliveryCity"))
                                                            .postalCode(data.get("deliveryPostalCode"))
                                                            .street(data.get("deliveryStreet"))
                                            )
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectStatus().isNoContent();
                }
        );

        And(
                "I try to update the order number {string} with the following details",
                (String orderNumber, DataTable dataTable) -> {
                    Map<String, String> data = dataTable.asMaps().getFirst();
                    EntityExchangeResult<String> result = restClient
                            .webTestClient()
                            .put()
                            .uri("/public/order/" + orderNumber)
                            .bodyValue(
                                    new UpdateAnOrderDTO()
                                            .firstname(data.get("firstname"))
                                            .lastname(data.get("lastname"))
                                            .phoneCountryCode(data.get("phoneCountryCode"))
                                            .phoneNumber(data.get("phoneNumber"))
                                            .quantity(PiloteQuantity.fromValue(data.get("quantity")))
                                            .deliveryAddress(
                                                    new AddressDTO()
                                                            .city(data.get("deliveryCity"))
                                                            .postalCode(data.get("deliveryPostalCode"))
                                                            .street(data.get("deliveryStreet"))
                                            )
                            )
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .expectBody(String.class)
                            .returnResult();

                    lastRequestResponse = new LastRequestResponse(
                            result.getStatus(),
                            result.getResponseBody()
                    );
                }
        );

        And(
                "I should see that the Order with the following data exist inside the database",
                (DataTable dataTable) -> {
                    var orderData = dataTable.asMaps().getFirst();
                    var sqlQueryResult = jdbcTemplate.queryForList("SELECT * FROM t_order");
                    assertThat(sqlQueryResult).anySatisfy(candidate -> assertThat(candidate)
                            .containsEntry("c_firstname", orderData.get("firstname"))
                            .containsEntry("c_lastname", orderData.get("lastname"))
                            .containsEntry("c_phone_country_code", orderData.get("phoneCountryCode"))
                            .containsEntry("c_phone_number", orderData.get("phoneNumber"))
                            .containsEntry("c_product", orderData.get("quantity"))
                            .containsEntry("c_city", orderData.get("deliveryCity"))
                            .containsEntry("c_postal_code", orderData.get("deliveryPostalCode"))
                            .containsEntry("c_street", orderData.get("deliveryStreet"))
                            .containsEntry("c_total", orderData.get("total"))
                            .satisfies(it -> assertThat(it.get("c_id")).isNotNull())
                            .satisfies(
                                    it -> assertThat(it.get("c_created_at"))
                                            .isNotNull()
                            )
                            .satisfies(
                                    it -> assertThat(it.get("c_order_number"))
                                            .isNotNull()
                                            .isEqualTo(orderNumberToLong(orderData.get("orderNumber")))
                            )
                    );
                }
        );


        And(
                "The database contains the following orders",
                (DataTable data) -> {
                    now = LocalDateTime.now().minusMonths(2);
                    data.asMaps().forEach(this::insertOrder);
                }
        );
        And(
                "I look for orders placed by customer whose name contains {string}",
                (String searchText) ->
                        this.orders =
                                restClient
                                        .webTestClient()
                                        .get()
                                        .uri(STR."/order?searchText=\{searchText}")
                                        .headers(httpHeaders -> httpHeaders.setBasicAuth("admin", "admin"))
                                        .accept(MediaType.APPLICATION_JSON)
                                        .exchange()
                                        .expectStatus().isOk()
                                        .expectBodyList(OrderDTO.class)
                                        .returnResult().getResponseBody()
        );
        And(
                "The hits' list should contain the following orders",
                (DataTable expectedData) -> {
                    var expectedOrders = expectedData
                            .asMaps()
                            .stream()
                            .map(data ->data.get("orderNumber"))
                            .toList();
                    assertThat(this.orders.stream().map(OrderDTO::getOrderNumber))
                            .containsAll(expectedOrders);
                }
        );
    }

    private void insertOrder(Map<String, String> orderData) {
        now = now.plusDays(1);
        jdbcTemplate.update("""
                        INSERT INTO t_order (
                             c_id, c_order_number, c_firstname, c_lastname, c_phone_country_code,
                             c_phone_number, c_city, c_postal_code, c_street, c_created_at,
                             c_total, c_product
                             ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                UUID.randomUUID().toString(),
                orderNumberToLong(orderData.get("orderNumber")),
                orderData.get("firstname"),
                orderData.get("lastname"),
                orderData.get("phoneCountryCode"),
                orderData.get("phoneNumber"),
                orderData.get("deliveryCity"),
                orderData.get("deliveryPostalCode"),
                orderData.get("deliveryStreet"),
                Timestamp.valueOf(now),
                "EUR;6.65",
                "FIVE"
        );
    }

    private Object orderNumberToLong() {
        return orderNumberToLong(order.getOrderNumber());
    }

    private Object orderNumberToLong(String orderNumberAsString) {
        return Long.parseLong(
                new StringBuffer(orderNumberAsString)
                        .deleteCharAt(0)
                        .toString()
                        .replaceAll(" ", "")
        );
    }

}
