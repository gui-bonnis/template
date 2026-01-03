package com.soul.fin.accounting.read.data.customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "customer", schema = "accounting_states")
public class CustomerEntity implements Persistable<UUID> {

    @Id
    private UUID id;
    private long version;
    private String name;
    @Transient
    private boolean isNew;

    public CustomerEntity() {
    }

    private CustomerEntity(UUID id, long version, String name, boolean isNew) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.isNew = isNew;
    }

    public CustomerEntity(UUID id, long version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
    }

    public static CustomerEntity insert(CustomerEntity entity) {
        return new CustomerEntity(entity.id, entity.version, entity.name, true);
    }

    public static CustomerEntity update(CustomerEntity entity) {
        return new CustomerEntity(entity.id, entity.version, entity.name, false);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

}

