package com.jagaad.technical_test.order.api;

import com.jagaad.technical_test.order.dto.OrderDTO;
import com.jagaad.technical_test.order.dto.PlaceAnOrderDTO;
import com.jagaad.technical_test.order.dto.UpdateAnOrderDTO;
import com.jagaad.technical_test.order.domain.UpdateTimeOverException;
import com.jagaad.technical_test.order.exception.ForbiddenException;
import com.jagaad.technical_test.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;

@RestController
public class OrderController implements OrderApi {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public ResponseEntity<OrderDTO> planAnOrder(PlaceAnOrderDTO placeAnOrderDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeAnOrder(placeAnOrderDTO));
    }

    @Override
    public ResponseEntity<Void> updateOrder(String orderNumber, UpdateAnOrderDTO updateAnOrderDTO) {
        try {
            orderService.updateOrder(orderNumber, updateAnOrderDTO);
        } catch (UpdateTimeOverException e) {
            throw new ForbiddenException(e.getMessage());
        }
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @Override
    public ResponseEntity<List<OrderDTO>> searchOrdersByCustomerData(String searchText) {
        return ResponseEntity.ok(orderService.searchOrdersByCustomerData(searchText));
    }
}
