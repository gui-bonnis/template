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

```
