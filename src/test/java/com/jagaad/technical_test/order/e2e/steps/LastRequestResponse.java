package com.jagaad.technical_test.order.e2e.steps;

import org.springframework.http.HttpStatusCode;

public record LastRequestResponse(HttpStatusCode status, String responseBody) {
}
