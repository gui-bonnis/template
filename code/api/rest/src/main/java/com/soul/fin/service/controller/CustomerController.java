package com.soul.fin.service.controller;


import com.soul.fin.server.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.service.dto.CustomerQueryResponse;
import com.soul.fin.service.dto.CustomerRegisteredResponse;
import com.soul.fin.service.dto.GetCustomerRequest;
import com.soul.fin.service.dto.RegisterCustomerRequest;
import com.soul.fin.service.mapper.CustomerMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerApplicationService service;

    public CustomerController(CustomerApplicationService service) {
        this.service = service;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerQueryResponse> getCustomerById(@PathVariable UUID customerId) {

        return Mono.just(new GetCustomerRequest(customerId))
                .map(CustomerMapper::toQuery)
                .as(service::getCustomerById)
                .map(CustomerMapper::toQueryResponse);

    }

    @GetMapping()
    public Flux<CustomerQueryResponse> getAllCustomers() {

        return service.getAllCustomers().map(CustomerMapper::toQueryResponse);

    }

    @PostMapping
    @PreAuthorize("hasAuthority('order.create')")
    //@PreAuthorize("hasAuthority('order.delete') and @tenantGuard.allowAccess(authentication, #tenantId)")
    public Mono<CustomerRegisteredResponse> registerCustomer(@RequestBody Mono<RegisterCustomerRequest> request) {

        return request
                .map(CustomerMapper::toCommand)
                .as(service::registerCustomer)
                .map(CustomerMapper::toResponse);

    }


}
