package com.soul.fin.service.customer.vo;

import java.time.LocalDate;

public record PersonalInfo(String name,
                           String email,
                           String phone,
                           LocalDate dateOfBirth,
                           String encryptedSsn) {
    public PersonalInfo {
        if (name == null || email == null) throw new IllegalArgumentException("Required fields cannot be null");
    }
}