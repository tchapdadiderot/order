package com.jagaad.technical_test.order.e2e;

import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.WebTestClient;

@Component
public class RestClient {

    public WebTestClient webTestClient() {
        int port = Integer.parseInt(System.getProperty("port"));
        return WebTestClient.bindToServer().baseUrl(STR."http://localhost:\{port}").build();
    }

}
