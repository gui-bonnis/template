package com.fin.soul.accounting.read.projection.customer.repository;

import com.fin.soul.accounting.read.projection.customer.model.CustomerSummaryV1;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Repository
public class CustomerSummaryRepositoryV1 {
    private final DatabaseClient db;

    public CustomerSummaryRepositoryV1(DatabaseClient db) {
        this.db = db;
    }

    public Mono<Void> insert(CustomerSummaryV1 view) {
        return db.sql("""
                            INSERT INTO accounting_projections.customer_summary_v1
                            (account_id, name, version, updated_at)
                            VALUES (:id, :name, :version, :updatedAt)
                        """)
                .bind("id", view.id())
                .bind("name", view.name())
                .bind("version", view.version())
                .bind("updatedAt", view.updatedAt())
                .then();
    }

    public Mono<Void> increaseAvailable(
            UUID customerId,
            String name,
            long version,
            Instant at
    ) {
        return db.sql("""
                            UPDATE accounting_projections.customer_summary_v1
                            SET name = :name,
                                version = :version,
                                updated_at = :at
                            WHERE customer_id = :id
                        """)
                .bind("id", customerId)
                .bind("name", name)
                .bind("version", version)
                .bind("at", at)
                .then();
    }
}
