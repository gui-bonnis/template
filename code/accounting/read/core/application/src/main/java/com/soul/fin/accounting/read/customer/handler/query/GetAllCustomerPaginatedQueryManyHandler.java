package com.soul.fin.accounting.read.customer.handler.query;


import com.soul.fin.accounting.read.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.read.customer.dto.query.GetAllCustomersPaginatedQuery;
import com.soul.fin.accounting.read.customer.ports.output.repository.CustomerRepository;
import com.soul.fin.common.bus.core.query.QueryManyHandler;
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
                .map(customer -> new CustomerQuery(customer.getId().getValue(), customer.getName()));
    }

}