package com.soul.fin.accounting.projection.data.tx;

import com.soul.fin.common.projection.engine.TransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
class R2dbcTxManager implements TransactionManager {

    private final TransactionalOperator tx;

    R2dbcTxManager(TransactionalOperator tx) {
        this.tx = tx;
    }

    @Override
    public <T> Mono<T> inTransaction(Mono<T> work) {
        return tx.transactional(work);
    }
}

