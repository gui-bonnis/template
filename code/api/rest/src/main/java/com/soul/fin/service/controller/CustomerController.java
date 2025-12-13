package com.soul.fin.service.controller;


import com.soul.fin.server.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.server.customer.dto.query.GetAllCustomersPaginatedQuery;
import com.soul.fin.server.customer.dto.query.GetAllCustomersQuery;
import com.soul.fin.server.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.service.dto.request.DeleteCustomerRequest;
import com.soul.fin.service.dto.request.GetCustomerRequest;
import com.soul.fin.service.dto.request.RegisterCustomerRequest;
import com.soul.fin.service.dto.request.UpdateCustomerRequest;
import com.soul.fin.service.dto.response.CustomerQueryResponse;
import com.soul.fin.service.dto.response.CustomerRegisteredResponse;
import com.soul.fin.service.mapper.CustomerMapper;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping
    @PreAuthorize("hasAuthority('order.create')")
    //@PreAuthorize("hasAuthority('order.delete') and @tenantGuard.allowAccess(authentication, #tenantId)")
    public Mono<CustomerRegisteredResponse> registerCustomer(@RequestBody Mono<RegisterCustomerRequest> request) {
        return request
                .map(CustomerMapper::toCommand)
                .as(service::registerCustomer)
                .map(CustomerMapper::toResponse);
    }

    @PutMapping("/{customerId}")
    public Mono<CustomerUpdatedResponse> updateCustomer(@PathVariable UUID customerId, @RequestBody Mono<UpdateCustomerRequest> request) {
        return request
                .map(r -> CustomerMapper.toCommand(customerId, r))
                .as(service::updateCustomer)
                .map(CustomerMapper::toResponse);
    }

    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable UUID customerId) {
        return Mono.just(new DeleteCustomerRequest(customerId))
                .map(CustomerMapper::toCommand)
                .as(service::deleteCustomer);
    }

}
