package com.soul.fin.accounting.write.controller;


import com.soul.fin.accounting.write.customer.dto.command.CustomerUpdatedResponse;
import com.soul.fin.accounting.write.customer.ports.input.service.CustomerApplicationService;
import com.soul.fin.accounting.write.dto.request.DeleteCustomerRequest;
import com.soul.fin.accounting.write.dto.request.RegisterCustomerRequest;
import com.soul.fin.accounting.write.dto.request.UpdateCustomerRequest;
import com.soul.fin.accounting.write.dto.response.CustomerRegisteredResponse;
import com.soul.fin.accounting.write.mapper.CustomerMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerApplicationService service;

    public CustomerController(CustomerApplicationService service) {
        this.service = service;
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
