package com.soul.fin.accounting.write.shared;


import com.soul.fin.common.core.event.DomainEvent;

import java.util.List;

public record Result<T>(T domain, List<DomainEvent> events) {
}
