package com.soul.fin.accounting.customer.handler.query;


import com.soul.fin.accounting.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.customer.dto.query.GetAllCustomersQuery;
import com.soul.fin.accounting.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.common.bus.core.QueryManyHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetAllCustomerQueryManyHandler implements QueryManyHandler<GetAllCustomersQuery, CustomerQuery> {

    private final CustomerRepository customerRepository;

    public GetAllCustomerQueryManyHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Flux<CustomerQuery> handle(GetAllCustomersQuery query) {
        return customerRepository.findAll()
                .map(customer -> new CustomerQuery(customer.getId().getValue(), customer.getName()));
    }

}