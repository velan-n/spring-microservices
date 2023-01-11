package com.example.microservices.currencyexchangeservice.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

@RestController
public class CircuitBreakerController {
    Logger logger= LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
//    @Retry(name="sample-api", fallbackMethod = "hardCodedResponse")
//    @CircuitBreaker(name="default", fallbackMethod = "hardCodedResponse")
//    @RateLimiter(name="sample-api")
    @Bulkhead(name="sample-api")
    public String breakCircuit(){
//        logger.info("Api call Received");
//        ResponseEntity<String> forEntity = new RestTemplate().getForEntity("localhost:8080/dummy-service", String.class);
//        return forEntity.getBody();
        return "Sample-api";
    }

    public String hardCodedResponse(NullPointerException e){
        return "Default Response";
    }

    public String hardCodedResponse(Exception e){
        return "Default Response Exception";
    }
}
