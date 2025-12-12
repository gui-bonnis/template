package com.soul.fin.service.controller;


import com.soul.fin.common.bus.SpringCommandBus;
import com.soul.fin.common.bus.SpringQueryBus;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.service.dto.CustomerQueryResponse;
import com.soul.fin.service.dto.CustomerRegisteredResponse;
import com.soul.fin.service.dto.GetCustomerRequest;
import com.soul.fin.service.dto.RegisterCustomerRequest;
import com.soul.fin.service.mapper.CustomerMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class CustomerController {

    private final CustomerApplicationService customerApplicationService;
    private final SpringCommandBus commandBus;
    private final SpringQueryBus queryBus;

    public CustomerController(CustomerApplicationService customerApplicationService, SpringCommandBus commandBus, SpringQueryBus queryBus) {
        this.customerApplicationService = customerApplicationService;
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @GetMapping("/ret/{customerId}")
    @PreAuthorize("hasAuthority('order.create')")
    public Mono<CustomerQueryResponse> ret(@PathVariable UUID customerId) {

        final var req = new GetCustomerRequest(customerId);
        return Mono.just(req)
                .map(CustomerMapper::toQuery)
                .flatMap(queryBus::execute)
                .map(c -> new CustomerQuery(c.customerId(), c.name()))
                .map(CustomerMapper::toQueryResponse);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('order.create')")
    public Mono<String> get() {
        final var req = new RegisterCustomerRequest("Guilherme");
        return Mono.just(req)
                .map(CustomerMapper::toCommand)
                .flatMap(commandBus::execute)
//                .doOnNext(res -> System.out.println("TYPE = " + res.getClass()))
                //.cast(com.soul.fin.server.customer.dto.command.CustomerRegisteredResponse.class)
                .map(customerRegisteredResponse ->
                        "Customer registered with id: " + customerRegisteredResponse.customerId().toString()
                );
    }


    @PostMapping
    //@PreAuthorize("hasAuthority('order.place')")
    //@PreAuthorize("hasAuthority('order.delete') and @tenantGuard.allowAccess(authentication, #tenantId)")
    public Mono<CustomerRegisteredResponse> placeOrder(@RequestBody Mono<RegisterCustomerRequest> request) {
        return request
                .map(CustomerMapper::toCommand)
                .as(customerApplicationService::registerCustomer)
                .map(CustomerMapper::toResponse);
    }


}
