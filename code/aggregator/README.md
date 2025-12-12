# JaCoCo Coverage Aggregation (Multi-Module Spring Boot)

This module is responsible for generating an **aggregated JaCoCo coverage report**
for all application modules in this Maven multi-module Spring Boot project.

It aggregates:

- Unit test coverage (Surefire)
- Integration test coverage (Failsafe)

into **a single report**.

---

## 🧱 Project Structure

```bash 

platform
├── pom.xml                # Root parent POM
├── customer-service
├── order-service
├── account-service
└── coverage                # ← THIS MODULE
└── pom.xml

```

---

## 🎯 Responsibilities of this Module

- Acts as a **JaCoCo aggregator**
- Depends on all modules that should contribute coverage
- Generates:
    - `jacoco-aggregate/index.html`
    - `jacoco.xml` (CI / Sonar compatible)

> ⚠️ This module **does NOT contain code or tests**

---

## 🔑 Key Concepts

### 1. Why aggregation needs a dedicated module

JaCoCo’s `report-aggregate` goal **only aggregates Maven dependencies**,
**not Maven reactor modules**.

Therefore:

- Listing modules in `<modules>` is **not enough**
- All modules must be listed as `<dependencies>` in this aggregator

---

## ⚙️ Configuration Overview

### 1️⃣ Root POM (`platform/pom.xml`)

The root POM defines **all JaCoCo, Surefire and Failsafe configuration**.

#### JaCoCo (parent POM)

```xml

<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
    <executions>

        <!-- Attach JaCoCo agent -->
        <execution>
            <id>prepare-agent</id>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
            <configuration>
                <propertyName>jacoco.argLine</propertyName>
            </configuration>
        </execution>

        <!-- Per-module report -->
        <execution>
            <id>report</id>
            <phase>verify</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>

        <!-- Aggregated report -->
        <execution>
            <id>report-aggregate</id>
            <phase>verify</phase>
            <goals>
                <goal>report-aggregate</goal>
            </goals>
        </execution>

    </executions>
</plugin>
````

> ℹ️ `jacoco.argLine` is **generated automatically** by `prepare-agent`.
> It must **never** be defined manually.

---

#### Surefire (Unit Tests)

```xml

<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.5.2</version>
    <configuration>
        <argLine>${jacoco.argLine}</argLine>
        <includes>
            <include>**/*Test.java</include>
        </includes>
    </configuration>
</plugin>
```

---

#### Failsafe (Integration Tests)

```xml

<plugin>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>3.5.2</version>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <argLine>${jacoco.argLine}</argLine>
        <includes>
            <include>**/*IT.java</include>
            <include>**/*ITCase.java</include>
        </includes>
    </configuration>
</plugin>
```

---

### 2️⃣ Aggregator Module (`coverage/pom.xml`)

This module **declares dependencies on all modules to be aggregated**.

```xml

<project>
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.example</groupId>
        <artifactId>platform</artifactId>
        <version>1.0.0</version>
    </parent>

    <artifactId>coverage</artifactId>
    <packaging>pom</packaging>

    <!-- 👇 REQUIRED for JaCoCo aggregation -->
    <dependencies>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>customer-service</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>order-service</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.example</groupId>
            <artifactId>account-service</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## ▶️ How to Run

### Run all tests and generate coverage

```bash
mvn verify
```

### Run only the aggregation module (CI-friendly)

```bash
mvn -pl coverage -am verify
```

---

## 📊 Output

### Per-module reports

```
<module>/target/site/jacoco/index.html
```

### Aggregated report

```
coverage/target/site/jacoco-aggregate/index.html
```

---

## 🚫 Common Pitfalls

| Problem                | Cause                                      |
|------------------------|--------------------------------------------|
| Aggregate report empty | Modules not declared as dependencies       |
| Coverage = 0%          | `argLine` overridden                       |
| ITs not included       | Using Surefire instead of Failsafe         |
| Missing coverage in CI | Running `mvn test` instead of `mvn verify` |

---

## ✅ Best Practices

* Keep JaCoCo configuration **only in the parent POM**
* Use a **dedicated aggregation module**
* Enforce coverage **only on the aggregated report**
* Exclude DTOs/config/generated code at the aggregate level
* Always use `mvn verify` in CI

---

## 🔮 Next Improvements (Optional)

* Coverage thresholds (`jacoco:check`)
* SonarQube XML export
* Package/class exclusions
* Testcontainers support
* Parallel build tuning

---

## 📎 References

* [https://www.jacoco.org/jacoco/trunk/doc/maven.html](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
* [https://maven.apache.org/surefire/](https://maven.apache.org/surefire/)
* [https://maven.apache.org/surefire/maven-failsafe-plugin/](https://maven.apache.org/surefire/maven-failsafe-plugin/)

## ✅ Coverage Rules (Fail Build on Aggregate Only)

Coverage enforcement **must be applied only to the aggregated report**, never at
individual module level.

Why:

- Prevents one small module from blocking the build
- Encourages system-level quality instead of local gaming
- Matches how coverage is consumed by CI and SonarQube

---

### Where coverage rules live

Coverage rules are defined **once**, in the **parent/root POM**, and executed
**only by the aggregator module**.

They are **not** enabled in application modules.

---

### JaCoCo `check` configuration (aggregate-only)

Add the following execution **to the parent POM**, under `jacoco-maven-plugin`:

```xml

<execution>
    <id>check-aggregate</id>
    <phase>verify</phase>
    <goals>
        <goal>check</goal>
    </goals>
    <configuration>

        <!-- Only evaluate the aggregated report -->
        <rules>
            <rule>
                <element>BUNDLE</element>
                <limits>
                    <limit>
                        <counter>INSTRUCTION</counter>
                        <value>COVEREDRATIO</value>
                        <minimum>0.80</minimum>
                    </limit>
                </limits>
            </rule>
        </rules>

    </configuration>
</execution>

```

📌 Notes:

* `BUNDLE` means **entire aggregated codebase**
* `INSTRUCTION` is the most reliable metric
* Adjust threshold gradually (e.g. 70% → 80%)

---

### Ensure the check runs only for the aggregator

The `jacoco:check` goal will automatically use the **aggregated execution data**
when executed in the aggregator module because:

* That module has dependencies on all others
* `report-aggregate` runs in the same phase

No extra wiring is required.

---

## 🚫 Coverage Exclusions (DTOs, Config, Generated Code)

Not all code should count towards coverage.

Typical exclusions:

* DTOs / records
* Configuration classes
* Spring Boot bootstrap code
* Generated sources
* API contracts

---

### Recommended exclusion patterns

Use **package-based exclusions** whenever possible.

#### Common exclusions

```text
**/dto/**
**/config/**
**/configuration/**
**/generated/**
**/model/**
**/entity/**
**/*Application.class
**/*Config.class
```

---

### Where exclusions are configured

Exclusions are configured **once**, in the **parent POM**, and applied consistently
to:

* Per-module reports
* Aggregated report
* Coverage checks

---

### JaCoCo exclusion configuration

Add this inside the `jacoco-maven-plugin` configuration:

```xml

<configuration>
    <excludes>
        <exclude>**/dto/**</exclude>
        <exclude>**/config/**</exclude>
        <exclude>**/configuration/**</exclude>
        <exclude>**/generated/**</exclude>
        <exclude>**/model/**</exclude>
        <exclude>**/entity/**</exclude>
        <exclude>**/*Application.class</exclude>
        <exclude>**/*Config.class</exclude>
    </excludes>
</configuration>
```

📌 This configuration applies to:

* `report`
* `report-aggregate`
* `check`

---

## ⚠️ Important Exclusion Rules

* Prefer **package-level exclusions** over class-level
* Never exclude:

    * Domain logic
    * Use cases
    * Application services
* If in doubt → **do not exclude**

Coverage should measure **behavior**, not boilerplate.

---

## 🔍 Verifying exclusions

After running:

```bash
mvn verify
```

Open:

```text
coverage/target/site/jacoco-aggregate/index.html
```

Confirm:

* Excluded packages do **not appear**
* Coverage percentage matches expectations
* Build fails if threshold is not met

---

## 🧠 Recommended Strategy

| Phase              | Threshold |
|--------------------|-----------|
| Early project      | 60–70%    |
| Active development | 75–80%    |
| Mature system      | 80–85%    |

Never start with aggressive thresholds — let coverage grow organically.

---

## ✅ Summary

* Coverage is enforced **once**, at aggregate level
* Aggregation is dependency-based, not module-based
* Exclusions are centralized and intentional
* CI uses `mvn verify` as the single entry point

This setup is **CI-safe, scalable, and future-proof**.

