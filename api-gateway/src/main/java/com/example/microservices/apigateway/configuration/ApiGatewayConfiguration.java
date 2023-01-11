package com.example.microservices.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    //Customize the routing uri
    //localhost:8765/**/**
    @Bean
    public RouteLocator customRouter(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p->p.path("/get")
                        .filters(f->f
                                .addRequestHeader("Accept-Language","ta")
                                .addRequestParameter("MyParam","MyValue"))
                        .uri("http://httpbin.org:80"))
                .route(p->p.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                                // currency-exchange = name registered with eureka
                .route(p->p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route(p->p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                                //currency-conversion = name registered with eureka
                .route(p->p.path("/currency-conversion-new/**")
                        .filters(f->f.rewritePath(
                                "/currency-conversion-new/(?<segment>.*)", //(?<segment>.*)=optional
                                "/currency-conversion-feign/${segment}"         //${segment}=optional
                                ))
                        .uri("lb://currency-conversion"))

                .build();
    }
}
