package com.jagaad.technical_test.order.api;

import com.jagaad.technical_test.order.dto.OrderDTO;
import com.jagaad.technical_test.order.dto.PiloteQuantity;
import com.jagaad.technical_test.order.dto.PlaceAnOrderDTO;
import com.jagaad.technical_test.order.dto.UpdateAnOrderDTO;
import com.jagaad.technical_test.order.service.OrderService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
class OrderControllerTest {
    protected WebTestClient webTestClient;
    OrderController orderController;
    @MockBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderController =  new OrderController(orderService);
        webTestClient = WebTestClient.bindToController(new Object[]{orderController}).build();
    }

    @Test
    void givenOrderData_whenPlaceAnOrder_thenReturnValidOrder() {
        var invoiceNumber = "B 0000 000 001";

        when(orderService.placeAnOrder(new PlaceAnOrderDTO().quantity(PiloteQuantity.FIVE)))
                .thenReturn(new OrderDTO().orderNumber(invoiceNumber));

        var response = webTestClient
                .post()
                .uri("/public/order")
                .bodyValue(new PlaceAnOrderDTO().quantity(PiloteQuantity.FIVE))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderDTO.class)
                .returnResult().getResponseBody();
        assertThat(response)
                .isNotNull()
                .returns(invoiceNumber, OrderDTO::getOrderNumber)
        ;
    }

    @SneakyThrows
    @Test
    void givenNewOrderDataAndOrderNumber_whenUpdateAnExistingOrder_thenNoThrowException() {
        var invoiceNumber = "B 0000 000 001";
        UpdateAnOrderDTO updateAnOrderDTO = new UpdateAnOrderDTO().quantity(PiloteQuantity.FIVE);

        webTestClient
                .put()
                .uri(STR."/public/order/\{invoiceNumber}")
                .bodyValue(updateAnOrderDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();

        verify(orderService).updateOrder(invoiceNumber, updateAnOrderDTO);
    }

    @Test
    void givenSearchQueryArrive_thenForwardItToTheServiceLayer() {
        var orderNumber1 = "B 0000 000 001";
        var orderNumber2 = "B 0000 000 002";

        var searchText = "rod";
        when(orderService.searchOrdersByCustomerData(searchText)).thenReturn(
                List.of(
                        new OrderDTO().orderNumber(orderNumber1),
                        new OrderDTO().orderNumber(orderNumber2)
                )
        );

        var responseBody = webTestClient
                .get()
                .uri(STR."/order?searchText=\{searchText}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(OrderDTO.class)
                .returnResult().getResponseBody();

        assertThat(responseBody)
                .hasSize(2)
                .anySatisfy(orderDTO -> assertThat(orderDTO.getOrderNumber()).isEqualTo(orderNumber1))
                .anySatisfy(orderDTO -> assertThat(orderDTO.getOrderNumber()).isEqualTo(orderNumber2));
    }

}