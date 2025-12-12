package com.soul.fin.service.dto;

import java.util.UUID;

public record CustomerQueryResponse(UUID customerId,
                                    String name) {
}
