package com.soul.fin.accouting.read.dto.response;

import java.util.UUID;

public record CustomerQueryResponse(UUID customerId,
                                    String name) {
}
