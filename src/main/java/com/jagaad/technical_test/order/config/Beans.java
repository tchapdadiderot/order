package com.jagaad.technical_test.order.config;

import com.jagaad.technical_test.order.domain.OrderFactory;
import com.jagaad.technical_test.order.domain.OrderFactoryImpl;
import com.jagaad.technical_test.order.domain.OrderRepository;
import com.jagaad.technical_test.order.repository.OrderSpringRepository;
import com.jagaad.technical_test.order.service.OrderRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Beans {

    @Bean
    public OrderRepository orderRepository(OrderSpringRepository orderSpringRepository) {
        return new OrderRepositoryImpl(orderSpringRepository);
    }

    @Bean
    public OrderFactory orderFactory() {
        return new OrderFactoryImpl();
    }
}
