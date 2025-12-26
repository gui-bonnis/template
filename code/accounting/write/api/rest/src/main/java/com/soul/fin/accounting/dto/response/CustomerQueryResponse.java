package com.soul.fin.accounting.dto.response;

import java.util.UUID;

public record CustomerQueryResponse(UUID customerId,
                                    String name) {
}
