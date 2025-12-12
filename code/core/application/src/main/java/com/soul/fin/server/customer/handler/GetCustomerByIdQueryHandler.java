package com.soul.fin.server.customer.handler;


import com.soul.fin.common.bus.core.QueryHandler;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetCustomerByIdQueryHandler implements QueryHandler<GetCustomerByIdQuery, CustomerQuery> {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdQueryHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<CustomerQuery> handle(GetCustomerByIdQuery query) {
        return customerRepository.findById(query.customerId())
                .map(customer -> new CustomerQuery(customer.getId().value(), customer.getName()));
    }
}