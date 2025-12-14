package com.soul.fin.server.customer.handler.query;


import com.soul.fin.common.bus.core.QueryOneHandler;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.server.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetCustomerByIdQueryOneHandler implements QueryOneHandler<GetCustomerByIdQuery, CustomerQuery> {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdQueryOneHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<CustomerQuery> handle(GetCustomerByIdQuery query) {
        return customerRepository.findById(query.customerId())
                .switchIfEmpty(CustomerApplicationExceptions.customerNotFound(query.customerId()))
                .map(customer -> new CustomerQuery(customer.getId().value(), customer.getName()));
    }
}