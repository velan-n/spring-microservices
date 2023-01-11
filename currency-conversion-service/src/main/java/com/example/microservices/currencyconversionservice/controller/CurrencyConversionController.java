package com.example.microservices.currencyconversionservice.controller;

import com.example.microservices.currencyconversionservice.model.CurrencyConversion;
import com.example.microservices.currencyconversionservice.proxy.CurrencyExchangeProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    @Autowired
    CurrencyExchangeProxy currencyExchangeProxy;

    @GetMapping("currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){
//        HashMap<String,String> uriResource=new HashMap<>();
//        uriResource.put("from",from);
//        uriResource.put("to",to);
        ResponseEntity<CurrencyConversion> entity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, from,to);
        CurrencyConversion currencyConversion = entity.getBody();
        BigDecimal totalCalculatedAmount=currencyConversion.getConversionMultiple().multiply(quantity);
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(totalCalculatedAmount);
        currencyConversion.setEnvironment(currencyConversion.getEnvironment()+" rest template");
        return currencyConversion;
    }

    @GetMapping("currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){
        CurrencyConversion currencyConversion = currencyExchangeProxy.retrieveExchangeValue(from,to);
        BigDecimal totalCalculatedAmount=currencyConversion.getConversionMultiple().multiply(quantity);
        currencyConversion.setQuantity(quantity);
        currencyConversion.setTotalCalculatedAmount(totalCalculatedAmount);
        currencyConversion.setEnvironment(currencyConversion.getEnvironment()+" feign");
        return currencyConversion;
    }
}
