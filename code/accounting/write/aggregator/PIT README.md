# PIT Mutation Testing (Planned)

This module defines the **mutation testing strategy** for this multi-module
Spring Boot project using **PIT (Pitest)**.

Mutation testing is currently **disabled** due to **Java 25 compatibility
limitations** in PIT.

This document explains:

- The intended configuration
- Why it is currently disabled
- How to enable it in the future with minimal effort

---

## 🧠 Why Mutation Testing?

Mutation testing measures **test quality**, not just coverage.

It answers:
> “If the code is wrong, do the tests fail?”

This complements JaCoCo coverage:

- JaCoCo → *How much code is executed*
- PIT → *How well code behavior is verified*

---

## ⚠️ Current Status

### ❌ Disabled (as of now)

Reason:

- The project runs on **Java 25**
- PIT **does not support Java 25 yet**
- PIT fails at runtime with `MINION_DIED`

This is a known limitation of PIT, not a project misconfiguration.

👉 **No Java downgrade or toolchain workaround will be applied**.

Mutation testing will be enabled once:

- PIT officially supports Java 25
- Or a compatible PIT release is published

---

## 🧱 Planned Project Structure

```bash

platform
├── pom.xml
├── customer-service
├── order-service
├── account-service
└── mutation-testing       # ← Aggregated PIT reports (planned)
└── pom.xml

```

---

## 🧬 Intended Mutation Testing Model

### Key design decisions

- PIT runs in a **dedicated aggregator module**
- Mutation testing is **not part of the default Maven lifecycle**
- PIT runs **manually or in scheduled CI**
- Aggregation is **cross-module**

---

## ⚙️ Intended Configuration

### Parent POM (root)

All PIT configuration is centralized in the **root POM** via `pluginManagement`.

```xml

<plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>${pitest.version}</version>

    <dependencies>
        <!-- Required for JUnit 5 -->
        <dependency>
            <groupId>org.pitest</groupId>
            <artifactId>pitest-junit5-plugin</artifactId>
            <version>1.2.1</version>
        </dependency>
    </dependencies>

    <configuration>

        <!-- Scope mutation to production code -->
        <targetClasses>
            <param>com.example.*</param>
        </targetClasses>

        <!-- Performance tuning -->
        <threads>4</threads>

        <!-- Stability -->
        <failWhenNoMutations>false</failWhenNoMutations>
        <timestampedReports>false</timestampedReports>

        <!-- Mutation strength -->
        <mutators>
            <mutator>STRONGER</mutator>
        </mutators>

        <!-- Exclusions -->
        <excludedClasses>
            <param>**.dto.*</param>
            <param>**.config.*</param>
            <param>**.configuration.*</param>
            <param>**.generated.*</param>
            <param>*Application</param>
        </excludedClasses>

    </configuration>
</plugin>
````

---

### Aggregator Module (`mutation-testing/pom.xml`)

```xml

<project>
    <artifactId>mutation-testing</artifactId>
    <packaging>pom</packaging>

    <!-- Modules to be mutated -->
    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>customer-service</artifactId>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>order-service</artifactId>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>account-service</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.pitest</groupId>
                <artifactId>pitest-maven</artifactId>

                <configuration>
                    <aggregate>true</aggregate>

                    <outputFormats>
                        <param>HTML</param>
                        <param>XML</param>
                    </outputFormats>

                    <reportsDirectory>
                        ${project.build.directory}/pit-reports
                    </reportsDirectory>
                </configuration>

            </plugin>
        </plugins>
    </build>
</project>
```

---

## ▶️ Intended Execution Command

Once Java 25 is supported by PIT:

```bash
mvn -Pmutation -pl ./mutation-testing -am pitest:mutationCoverage
```

Expected output:

```
mutation-testing/target/pit-reports/index.html
```

---

## 🚫 Why PIT Is Isolated from JaCoCo

* PIT launches its own JVMs (“minions”)
* JaCoCo injects a JVM agent
* Mixing both causes runtime crashes

Therefore:

* PIT runs in its **own Maven profile**
* JaCoCo is disabled during mutation runs

---

## 📊 Planned Thresholds

| Metric                 | Target          |
|------------------------|-----------------|
| Line coverage (JaCoCo) | ≥ 80%           |
| Mutation score (PIT)   | ≥ 65% (initial) |

Thresholds will be enforced **only at aggregate level**.

---

## 🧠 Best Practices (When Enabled)

* Run PIT **nightly or on-demand**
* Mutate **domain logic only**
* Exclude infrastructure & adapters
* Avoid full Spring context tests
* Prefer unit and slice tests

---

## 🔮 Re-enable Checklist

When PIT supports Java 25:

* [ ] Verify PIT release notes
* [ ] Enable `mutation` Maven profile
* [ ] Run aggregation locally
* [ ] Add mutation job to CI
* [ ] Monitor runtime and stability

---

## 📎 References

* [https://pitest.org](https://pitest.org)
* [https://pitest.org/quickstart/maven/](https://pitest.org/quickstart/maven/)
* [https://github.com/hcoles/pitest](https://github.com/hcoles/pitest)

