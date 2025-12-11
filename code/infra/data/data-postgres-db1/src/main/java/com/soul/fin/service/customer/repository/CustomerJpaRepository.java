package com.soul.fin.service.customer.repository;

import com.soul.fin.service.customer.entity.CustomerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends ReactiveCrudRepository<CustomerEntity, UUID> {
}
