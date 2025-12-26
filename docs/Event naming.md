Perfect — this is the **right next step**.
Event naming & versioning conventions are what turn an event-driven system from “working” into **operable, evolvable,
and auditable**.

I’ll define this as a **non-negotiable standard**, aligned with:

* DDD
* Kafka-first systems
* Long-lived financial platforms
* Regulatory/audit needs

You can later drop this **as-is** into `docs/EVENT_CONVENTIONS.md`.

---

# 2️⃣ Event Naming & Versioning Conventions

## 1️⃣ What a Domain Event Is (Authoritative Definition)

A **Domain Event** represents:

> **A fact that already happened inside a single bounded context**

It is:

* Immutable
* Historical
* Declarative
* Owned by exactly **one service**

It is **not**:

* A command
* A request
* An intention
* A workflow step

---

## 2️⃣ Event Naming Rules (Strict)

### **Rule E-N1 — Past Tense, Fact-Based**

Event names **MUST** be written in **past tense**.

✅ Correct:

* `CustomerRegistered`
* `CustomerProfileUpdated`
* `CustomerDeactivated`
* `PortfolioCreated`
* `OrderExecuted`

❌ Forbidden:

* `RegisterCustomer`
* `CreatePortfolio`
* `UpdateCustomer`
* `ExecuteOrder`

> If it sounds like something you *want* to do, it is not an event.

---

### **Rule E-N2 — Aggregate-Centric Naming**

Event name format:

```
<AggregateName><WhatHappened>
```

Examples:

* `CustomerRegistered`
* `CustomerEmailChanged`
* `OrderCancelled`
* `PositionClosed`

This guarantees:

* Clear ownership
* Easy mental mapping
* No “mystery” events

---

### **Rule E-N3 — No Technical or Workflow Terms**

Events must never include:

* `Saga`
* `Workflow`
* `Step`
* `Process`
* `Async`
* `Handler`

❌ `CustomerRegistrationCompleted`
❌ `OrderSagaStepFinished`

Events describe **business reality**, not orchestration.

---

### **Rule E-N4 — One Aggregate, One Event Stream**

An event:

* Belongs to **one aggregate**
* Describes **one aggregate change**

❌ `CustomerAndAccountLinked`
✅ `AccountLinkedToCustomer` *(owned by Account service)*

---

## 3️⃣ Event Namespace & Topic Naming (Kafka)

### **Rule E-T1 — Topic Name Structure**

```
<domain>.<aggregate>.<event>.v<major>
```

Example:

```
customer.customer.customer-registered.v1
portfolio.portfolio.portfolio-created.v1
orders.order.order-executed.v2
```

Characteristics:

* All lowercase
* Kebab-case for event name
* Explicit version suffix

---

### **Rule E-T2 — Version in Topic, Not Payload**

**Major versions must be visible at the topic level.**

Why:

* Safe parallel consumption
* Easy rollback
* Zero ambiguity

❌ Single topic with mixed versions
✅ One topic per major version

---

## 4️⃣ Event Versioning Rules (Critical)

### **Rule E-V1 — Events Are Immutable**

Once an event is published:

* ❌ It is never changed
* ❌ Fields are never removed
* ❌ Semantics are never altered

If meaning changes → **new version**

---

### **Rule E-V2 — Semantic Versioning for Events**

Use **MAJOR.MINOR** semantics:

| Change Type           | Version Impact |
|-----------------------|----------------|
| Add optional field    | MINOR          |
| Clarify documentation | MINOR          |
| Remove field          | MAJOR          |
| Change meaning        | MAJOR          |
| Rename field          | MAJOR          |

---

### **Rule E-V3 — Major Version = New Topic**

Breaking changes require:

* New topic
* New schema
* Parallel consumption period

Example:

```
customer.customer.customer-registered.v1
customer.customer.customer-registered.v2
```

---

### **Rule E-V4 — Minor Versions Are Backward-Compatible**

Minor versions:

* Do not break consumers
* Do not require new topic
* Are handled via schema evolution

---

## 5️⃣ Event Payload Structure (Canonical)

Every event **MUST** contain:

```json
{
  "eventId": "uuid",
  "eventType": "CustomerRegistered",
  "aggregateId": "uuid",
  "aggregateType": "Customer",
  "version": "1.0",
  "occurredAt": "2025-01-01T12:00:00Z",
  "data": {},
  "metadata": {}
}
```

---

### **Required Fields Explained**

| Field           | Purpose                 |
|-----------------|-------------------------|
| `eventId`       | Idempotency             |
| `eventType`     | Human & machine clarity |
| `aggregateId`   | Ownership               |
| `aggregateType` | Routing & filtering     |
| `version`       | Schema version          |
| `occurredAt`    | Audit & ordering        |
| `data`          | Business payload        |
| `metadata`      | Technical context       |

---

## 6️⃣ Metadata Rules

### **Rule E-M1 — Metadata Is Not Business Data**

Metadata may include:

* `correlationId`
* `causationId`
* `tenantId`
* `traceId`
* `producedBy`

Metadata must **never**:

* Drive business decisions
* Be required for domain correctness

---

## 7️⃣ Consumer Rules (Equally Important)

### **Rule E-C1 — Consumers Must Be Forward-Compatible**

Consumers:

* Must ignore unknown fields
* Must not depend on field ordering
* Must not assume completeness

---

### **Rule E-C2 — Events Are Not RPC**

Consumers must:

* Never expect a reply
* Never assume ordering across aggregates
* Never block waiting for another service

---

## 8️⃣ Anti-Patterns (Explicitly Forbidden)

❌ Emitting commands as events
❌ Using events for request/response
❌ Mutating state in consumers
❌ Cross-aggregate events
❌ “Generic” events (`EntityUpdated`)

---

## 9️⃣ Minimal Example (CustomerRegistered v1)

**Topic**

```
customer.customer.customer-registered.v1
```

**Payload**

```json
{
  "eventId": "a1b2c3",
  "eventType": "CustomerRegistered",
  "aggregateId": "cust-123",
  "aggregateType": "Customer",
  "version": "1.0",
  "occurredAt": "2025-01-01T12:00:00Z",
  "data": {
    "email": "user@email.com",
    "country": "PT"
  },
  "metadata": {
    "correlationId": "req-789",
    "producedBy": "customer-service"
  }
}
```

---

## 🔒 Non-Negotiable Summary (Put This in Bold in Docs)

> * Events are facts
> * Facts never change
> * Breaking change = new version + new topic
> * Consumers adapt, producers never rewrite history

---

## Next (When You’re Ready)

The **natural next steps** after this are:

3️⃣ Kafka contract templates (JSON Schema / Avro rules)
4️⃣ WebSocket projection & streaming rules
5️⃣ Saga choreography conventions
6️⃣ Accounting-grade event audit rules

Tell me which one you want next, and we’ll extend the template with the same level of rigor.
