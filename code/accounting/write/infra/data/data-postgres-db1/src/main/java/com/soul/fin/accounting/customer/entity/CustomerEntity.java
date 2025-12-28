package com.soul.fin.accounting.customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
@Table(name = "customer", schema = "customer")
public class CustomerEntity {

    @Id
    private UUID id;
    private long version;
    private String name;

    public CustomerEntity() {
    }

    public CustomerEntity(UUID id, long version, String name) {
        this.id = id;
        this.version = version;
        this.name = name;
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
}

