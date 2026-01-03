package com.soul.fin.accounting.write.vo;

public record Address(String type,
                      String street,
                      String city,
                      String state,
                      String zipCode,
                      String country) {
    public Address {
        if (type == null || street == null) throw new IllegalArgumentException("Required fields cannot be null");
    }
}