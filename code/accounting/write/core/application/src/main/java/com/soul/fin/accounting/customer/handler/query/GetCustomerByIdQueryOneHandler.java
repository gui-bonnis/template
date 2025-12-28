package com.soul.fin.accounting.customer.handler.query;


import com.soul.fin.accounting.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.customer.dto.query.GetCustomerByIdQuery;
import com.soul.fin.accounting.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.common.bus.core.QueryOneHandler;
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
                .map(customer -> new CustomerQuery(customer.getId().getValue(), customer.getName()));
    }
}