package com.soul.fin.service.controller;

import com.soul.fin.service.dto.CreateOrderRequest;
import com.soul.fin.service.dto.CreateOrderResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.EntityResponse;

@RestController()
@RequestMapping("/hello")
public class HelloController {

    @GetMapping()
    public String ret() {
        return "qq coisa";
    }

    // mono
    // flux
    // GET, PUT, PATCH, POST, DELETE
    // call command bus to application modulo

    public EntityResponse<CreateOrderResponse> placeOrder(CreateOrderRequest request) {
        // all return types 2xx, 3xx, 4xx, 5xx
        // each error has its own exception handler at ApplicationErrorHandler
        // also the security module can throw exceptions that are handled there
        return null;
    }

}
