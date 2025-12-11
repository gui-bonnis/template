package com.soul.fin.common.core.event;

import com.soul.fin.common.core.entity.Audit;

public interface DomainEvent extends Message {
    Audit audit();
}
