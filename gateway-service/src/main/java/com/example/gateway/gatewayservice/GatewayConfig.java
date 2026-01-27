package com.example.gateway.gatewayservice;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("catalog-service", r -> r.path("/api/books/**")
                        .uri("lb://CATALOG-SERVICE"))
                .route("user-service", r -> r.path("/api/users/**")
                        .uri("lb://USER-SERVICE"))
                .route("order-service", r -> r.path("/api/orders/**")
                        .uri("lb://ORDER-SERVICE"))
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .uri("lb://INVENTORY-SERVICE"))
                .route("review-service", r -> r.path("/api/reviews/**")
                        .uri("lb://REVIEW-SERVICE"))
                .build();
    }
}
