Below is the **canonical `CommandResult` hierarchy**, widely used in CQRS + DDD architectures (including event-sourced
systems, reactive systems, and saga-based workflows).

This hierarchy is **framework-agnostic**, fits perfectly with **reactive programming**, and matches **DDD semantics**.

---

# ⭐ The Canonical CommandResult Hierarchy

## **1. CommandResult (sealed parent type)**

A command can result in one of the following canonical states:

1. **Success** — the command was processed, invariants respected, events recorded.
2. **Rejected** — the command is *invalid* due to business rules (aggregate invariants).
3. **Accepted** — the command is valid and *accepted for async workflow* (e.g., saga).
4. **Failed** — an *unexpected error* occurred (technical not business).

These categories map exactly to the semantics CQRS needs.

---

# ⭐ UML Diagram

```
CommandResult
 ├── CommandSuccess
 ├── CommandRejected
 ├── CommandAccepted
 └── CommandFailure
```

---

# ⭐ Java Design (sealed interface + records)

### **CommandResult.java**

```java
public sealed interface CommandResult
        permits CommandSuccess, CommandRejected, CommandAccepted, CommandFailure {

    boolean isSuccess();
}
```

---

# ⭐ 2. CommandSuccess

Used when:

* The command was executed
* Aggregate invariants were respected
* Events were stored
* A new state exists

Return the **aggregate identifier** (or any domain-specific ID).

```java
public record CommandSuccess(
        String aggregateId,
        Object result // optional: DTO, version, metadata
) implements CommandResult {

    @Override
    public boolean isSuccess() {
        return true;
    }
}
```

Examples:

* Stock order placed
* Balance reserved
* Portfolio created

---

# ⭐ 3. CommandRejected

This is **not an error**—it's **domain validation**.

Used when business rules are violated:

* insufficient balance
* duplicate transaction
* invalid transition
* order already settled
* cannot cancel an executed order

```java
public record CommandRejected(
        String message,
        String errorCode // optional: domain error code
) implements CommandResult {

    @Override
    public boolean isSuccess() {
        return false;
    }
}
```

Important:
This should **not** trigger retries.
It is a normal, expected domain scenario.

---

# ⭐ 4. CommandAccepted

Used when the command **starts a long-running workflow**, typically:

* Saga
* Process Manager
* Asynchronous multi-step flow
* External system coordination (KYC, payment, settlement)

Meaning:

> The command is valid and queued, but the final business result will arrive later via events.

```java
public record CommandAccepted(
        String commandId,
        Instant acceptedAt
) implements CommandResult {

    @Override
    public boolean isSuccess() {
        return true;
    }
}
```

This is the correct response for REST: **202 Accepted**.

---

# ⭐ 5. CommandFailure

Unexpected, non-domain failures:

* database unavailable
* timeout calling external service
* concurrency failure
* version mismatch not handled gracefully
* infrastructure errors

This indicates **exceptional errors**, not business decisions.

```java
public record CommandFailure(
        String message,
        Throwable exception
) implements CommandResult {

    @Override
    public boolean isSuccess() {
        return false;
    }
}
```

These errors should be logged structurally, retried, or escalated.

---

# ⭐ Summary Table (Canonical Semantics)

| Result Type         | When Used                  | Meaning                              | HTTP Status (optional) |
|---------------------|----------------------------|--------------------------------------|------------------------|
| **CommandSuccess**  | Aggregate applied command  | Strong consistency ok, events stored | 200 / 201              |
| **CommandRejected** | Domain invariant violation | Command not allowed                  | 400                    |
| **CommandAccepted** | Saga or async workflow     | Valid, queued for future completion  | 202                    |
| **CommandFailure**  | Unexpected system error    | Technical failure                    | 500                    |

---

# ⭐ How This Fits in Reactive Programming

A command handler returns:

```java
Mono<CommandResult>
```

Example:

```java
public Mono<CommandResult> handle(CreateOrder command) {
    return loadAggregate(command.orderId())
            .flatMap(aggregate ->
                    aggregate.createOrder(command)
                            .flatMap(events -> eventStore.append(command.orderId(), events))
                            .thenReturn(new CommandSuccess(command.orderId(), null))
            )
            .onErrorResume(DomainException.class, ex ->
                    Mono.just(new CommandRejected(ex.getMessage(), ex.errorCode()))
            )
            .onErrorResume(ex ->
                    Mono.just(new CommandFailure("Unexpected error", ex))
            );
}
```

The bus doesn’t care whether the workflow is transactional or saga;
it returns the appropriate subclass of `CommandResult`.

---

# ⭐ Should Commands ALWAYS Return CommandResult?

Yes.

This creates a **uniform API contract** across:

* REST
* RSocket
* Kafka request-response
* Testing
* Monitoring
* Sagas
* Event-sourced write model

It also allows:

* structural logging
* tracing (commandId)
* correlation across distributed flows

---

# ⭐ If You Want Next

I can deliver:

### 🔥 A full production-ready **Reactive Command Bus** using this hierarchy

### 🔥 How to map CommandResult → REST (`@RestControllerAdvice`)

### 🔥 How to integrate this with Sagas + Kafka

### 🔥 How the bus fits in your **multi-module hexagonal structure**

Which one would you like next?
