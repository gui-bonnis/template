package com.soul.fin.accounting.read.data.customer.mapper;

import com.soul.fin.accounting.read.customer.entity.CustomerSummary;
import com.soul.fin.accounting.read.data.customer.entity.CustomerSummaryEntity;
import org.springframework.stereotype.Component;

@Component
public class CustomerSummaryMapper {

    public CustomerSummary toCustomerSummary(CustomerSummaryEntity entity) {
        return new CustomerSummary(entity.getId(),
                entity.getName(),
                entity.getVersion());
    }

    public CustomerSummaryEntity toCustomerSummaryEntity(CustomerSummary customer) {
        return new CustomerSummaryEntity(customer.getId(), customer.getVersion(), customer.getName());
    }


}
