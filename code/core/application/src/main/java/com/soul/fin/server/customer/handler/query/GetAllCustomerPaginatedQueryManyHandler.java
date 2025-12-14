package com.soul.fin.server.customer.handler.query;


import com.soul.fin.common.bus.core.QueryManyHandler;
import com.soul.fin.server.customer.dto.query.CustomerQuery;
import com.soul.fin.server.customer.dto.query.GetAllCustomersPaginatedQuery;
import com.soul.fin.server.customer.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class GetAllCustomerPaginatedQueryManyHandler implements QueryManyHandler<GetAllCustomersPaginatedQuery, CustomerQuery> {

    private final CustomerRepository customerRepository;

    public GetAllCustomerPaginatedQueryManyHandler(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Flux<CustomerQuery> handle(GetAllCustomersPaginatedQuery query) {
        return customerRepository.findBy(query.page(), query.size())
                .map(customer -> new CustomerQuery(customer.getId().value(), customer.getName()));
    }

}