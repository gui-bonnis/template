package com.soul.fin.service.controller;


import com.soul.fin.common.bus.SpringCommandBus;
import com.soul.fin.common.bus.SpringQueryBus;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
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
@RequestMapping("/orders")
public class CustomerController {

    private final SpringCommandBus commandBus;
    private final SpringQueryBus queryBus;
    private final CustomerRepository customerRepository;

    public CustomerController(SpringCommandBus commandBus, SpringQueryBus queryBus, CustomerRepository customerRepository) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/{customerId}")
    public Mono<CustomerQueryResponse> getCustomerById(@PathVariable UUID customerId) {

        return Mono.just(new GetCustomerRequest(customerId))
                .map(CustomerMapper::toQuery)
                .flatMap(queryBus::execute)
                .map(CustomerMapper::toQueryResponse);

    }

    @GetMapping()
    public Flux<CustomerQueryResponse> getAllCustomers() {
        return customerRepository.findAll()
                .map(CustomerMapper::toQueryResponse);

    }

    @PostMapping
    @PreAuthorize("hasAuthority('order.create')")
    //@PreAuthorize("hasAuthority('order.delete') and @tenantGuard.allowAccess(authentication, #tenantId)")
    public Mono<CustomerRegisteredResponse> registerCustomer(@RequestBody Mono<RegisterCustomerRequest> request) {

        return request
                .map(CustomerMapper::toCommand)
                .flatMap(commandBus::execute)
                .map(CustomerMapper::toResponse);

    }


}
