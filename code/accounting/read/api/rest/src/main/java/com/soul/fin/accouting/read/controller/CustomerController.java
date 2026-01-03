package com.soul.fin.accouting.read.controller;


import com.soul.fin.accounting.read.customer.dto.query.GetAllCustomersPaginatedQuery;
import com.soul.fin.accounting.read.customer.dto.query.GetAllCustomersQuery;
import com.soul.fin.accounting.read.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.accouting.read.dto.request.GetCustomerRequest;
import com.soul.fin.accouting.read.dto.response.CustomerQueryResponse;
import com.soul.fin.accouting.read.mapper.CustomerMapper;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
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
        return Flux.just(new GetAllCustomersQuery())
                .as(service::getAllCustomers)
                .map(CustomerMapper::toQueryResponse);
    }

    @GetMapping("/paginated")
    public Mono<List<CustomerQueryResponse>> getAllCustomers(@RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        return Flux.just(new GetAllCustomersPaginatedQuery(page, size))
                .as(service::getAllCustomersPaginated)
                .map(CustomerMapper::toQueryResponse)
                .collectList();
    }

}
