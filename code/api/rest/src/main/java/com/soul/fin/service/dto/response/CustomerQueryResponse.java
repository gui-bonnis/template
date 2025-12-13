package com.soul.fin.service.dto.response;

import java.util.UUID;

public record CustomerQueryResponse(UUID customerId,
                                    String name) {
}
