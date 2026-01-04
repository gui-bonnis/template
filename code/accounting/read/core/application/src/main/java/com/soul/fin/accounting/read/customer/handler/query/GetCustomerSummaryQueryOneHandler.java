package com.soul.fin.accounting.read.customer.handler.query;


import com.soul.fin.accounting.read.customer.dto.query.CustomerQuery;
import com.soul.fin.accounting.read.customer.dto.query.GetCustomerSummaryQuery;
import com.soul.fin.accounting.read.customer.exception.CustomerApplicationExceptions;
import com.soul.fin.accounting.read.customer.ports.output.repository.CustomerSummaryRepository;
import com.soul.fin.common.bus.core.query.QueryOneHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GetCustomerSummaryQueryOneHandler implements QueryOneHandler<GetCustomerSummaryQuery, CustomerQuery> {

    private final CustomerSummaryRepository customerRepository;

    public GetCustomerSummaryQueryOneHandler(CustomerSummaryRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<CustomerQuery> handle(GetCustomerSummaryQuery query) {
        return customerRepository.findById(query.customerId())
                .switchIfEmpty(CustomerApplicationExceptions.customerNotFound(query.customerId()))
                .map(customer -> new CustomerQuery(customer.getId(), customer.getName()));
    }
}