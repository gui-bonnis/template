Great — let’s walk through a **concrete, copy-pasteable, step-by-step** setup for running **PIT (PITEST)** on a **Maven
multi-module** project and **producing an aggregated report**. I’ll show exactly **where** to put things (parent POM,
submodule POMs, pluginManagement, profiles), give you the exact commands to run, and cover the most common pitfalls.

I’ll assume this repo layout (change names to match your project):

```
/my-project
  ├── pom.xml                <-- parent aggregator (packaging = pom)
  ├── aggregator/            <-- optional dedicated aggregation module (packaging = pom)
  │     └── pom.xml
  ├── module-a/
  │     └── pom.xml
  ├── module-b/
  │     └── pom.xml
  └── module-c/
        └── pom.xml
```

---

## 1) Important rules / high-level notes

* PIT runs **per module** (it mutates classes and runs tests in the same module by default).
* To **aggregate** results you need:

    1. Each module to produce **mutations.xml** and **linecoverage.xml** in a predictable (non-timestamped) folder.
    2. A **dedicated aggregator module** (or the parent) to run the `report-aggregate` (PitAggregationMojo) that will
       read each submodule’s reports and create the combined report.
* Use a **profile** (e.g. `pit`) so PIT runs only when you ask for it — mutation testing is slow and you rarely want it
  on every build.
* Use a relatively recent PIT version (≥ `1.22.0` recommended as of recent releases). I’ll use `1.22.0` in examples —
  replace with the latest stable if you want.

---

## 2) Parent `pom.xml` — dependencyManagement & pluginManagement (copy/paste)

Put common versions and the PIT plugin configuration under `<pluginManagement>` so modules inherit it without
duplicating full configs.

```xml
<!-- parent pom.xml (root) -->
<project>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.example</groupId>
    <artifactId>my-project-parent</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <modules>
        <module>module-a</module>
        <module>module-b</module>
        <module>module-c</module>
        <module>aggregator</module> <!-- aggregator module for final aggregation -->
    </modules>

    <dependencyManagement>
        <dependencies>
            <!-- put common dependencies versions here (JUnit, AssertJ, etc.) -->
            <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter</artifactId>
                <version>5.9.3</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <!-- PIT plugin default configuration (inherits in modules) -->
                <plugin>
                    <groupId>org.pitest</groupId>
                    <artifactId>pitest-maven</artifactId>
                    <version>1.22.0</version>

                    <configuration>
                        <!-- put sane defaults here; modules can override -->
                        <targetClasses>
                            <!-- mutate classes in your project's package -->
                            <param>com.example.*</param>
                        </targetClasses>

                        <targetTests>
                            <param>com.example.*</param>
                        </targetTests>

                        <!-- avoid timestamped directories so aggregate can find reports -->
                        <timestampedReports>false</timestampedReports>

                        <!-- consistent reports folder per module -->
                        <reportsDirectory>${project.build.directory}/pit-reports</reportsDirectory>

                        <!-- reduce runtime on CI; tune for local runs -->
                        <threads>4</threads>

                        <!-- JVM args for tests if needed (increase heap) -->
                        <jvmArgs>
                            <jvmArg>-Xmx2G</jvmArg>
                        </jvmArgs>

                        <!-- mutators example -->
                        <mutators>
                            <mutator>DEFAULTS</mutator>
                        </mutators>

                        <!-- optionally skip if no tests/compiled classes -->
                        <skip>false</skip>
                    </configuration>
                </plugin>

                <!-- Aggregation plugin for parent/aggregator module -->
                <plugin>
                    <groupId>org.pitest</groupId>
                    <artifactId>pitest-aggregate-report</artifactId>
                    <version>1.22.0</version>
                    <!-- No executions here; we configure this in aggregator module explicitly -->
                </plugin>

            </plugins>
        </pluginManagement>

        <!-- you can add other build plugins (compiler, surefire) here -->
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
                <configuration>
                    <useModulePath>false</useModulePath> <!-- avoid Java 9+ module issues in many projects -->
                </configuration>
            </plugin>
        </plugins>
    </build>

    <!-- create a profile to enable pit runs (so you don't run PIT by default) -->
    <profiles>
        <profile>
            <id>pit</id>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.pitest</groupId>
                        <artifactId>pitest-maven</artifactId>
                        <!-- plugin version and config are inherited from pluginManagement -->
                        <!-- bind the pit goal to verify so it runs via mvn -Ppit verify -->
                        <executions>
                            <execution>
                                <id>pit-mutation</id>
                                <goals>
                                    <goal>mutationCoverage</goal>
                                </goals>
                                <phase>verify</phase>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
```

**Where to put this:** root `pom.xml`.
**Why:** `pluginManagement` defines the default configuration; child modules will simply declare the plugin in
`<plugins>` and inherit.

---

## 3) Submodule `pom.xml` (module-a / module-b / module-c)

Each module just **declares** the PIT plugin (no need to repeat full configuration). Also ensure tests and sources are
declared normally.

```xml
<!-- module-a/pom.xml -->
<project>
    <parent>
        <groupId>com.example</groupId>
        <artifactId>my-project-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>module-a</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <!-- your module dependencies, including tests -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- simply reference plugin; config comes from parent pluginManagement -->
            <plugin>
                <groupId>org.pitest</groupId>
                <artifactId>pitest-maven</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**Where to put:** each module `pom.xml`.

**Important:** ensure the `targetClasses` pattern in parent is broad enough to include each module’s packages, or
override the `<targetClasses>` per module if necessary.

---

## 4) Aggregator module — collects and merges the reports

You should create a dedicated aggregator module (packaging `pom`) to run the `report-aggregate` goal — this module will
look across submodules for `mutations.xml` and `linecoverage.xml` (hence why timestampedReports must be disabled and a
predictable `reportsDirectory` used).

`aggregator/pom.xml`:

```xml
<!-- aggregator/pom.xml -->
<project>
    <parent>
        <groupId>com.example</groupId>
        <artifactId>my-project-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <artifactId>aggregator</artifactId>
    <packaging>pom</packaging>

    <build>
        <plugins>
            <plugin>
                <groupId>org.pitest</groupId>
                <artifactId>pitest-aggregate-report</artifactId>
                <!-- inherits version from parent pluginManagement -->
                <executions>
                    <execution>
                        <id>aggregate-pit-reports</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>report-aggregate</goal>
                        </goals>
                        <configuration>
                            <!-- where aggregated report will be placed -->
                            <outputDirectory>${project.build.directory}/pit-aggregate-report</outputDirectory>

                            <!-- If required, you can set other options, but usually defaults are ok -->
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

**Where to put:** `aggregator/pom.xml` module listed in parent `<modules>`.

---

## 5) Key config details (must-gets)

1. **No timestamped reports** — aggregator expects `mutations.xml` and `linecoverage.xml` in module report dirs. In
   parent pluginManagement we set:

   ```xml
   <timestampedReports>false</timestampedReports>
   <reportsDirectory>${project.build.directory}/pit-reports</reportsDirectory>
   ```

   This makes output deterministic (e.g. `module-a/target/pit-reports/mutations.xml`).

2. **Run tests compile first** — PIT needs compiled classes and tests. Bind it to `verify` (after `test-compile`/`test`)
   or run `mvn -Ppit -DskipTests=false verify`. The plugin will compile but having classes precompiled avoids surprises.

3. **Profile** — keep PIT under a profile (`pit`) so it doesn’t run on every `mvn package`.

4. **Aggregation only after all module runs** — invoke the build from the reactor root so every module runs PIT before
   aggregator reads reports.

---

## 6) Commands to run locally

From project root:

**Run PIT in all modules and then aggregate:**

```bash
mvn -Ppit -T 1C clean verify
```

What happens:

* `-Ppit` activates the profile that binds `pitest-maven:mutationCoverage` to `verify`.
* Each module (module-a, module-b, module-c) runs mutationCoverage and writes results to `target/pit-reports/`.
* The aggregator module’s `report-aggregate` runs during `verify` and produces combined report at
  `aggregator/target/pit-aggregate-report/` (configurable).

If you want to run PIT for a single module (fast dev iteration):

```bash
mvn -Ppit -pl module-a -am clean verify
```

(`-pl` selects the module; `-am` builds required dependencies)

---

## 7) Common PIT options you may need to tune

Example configuration keys (can be put in parent pluginManagement `<configuration>` or per module):

```xml

<configuration>
    <targetClasses>
        <param>com.example.modulea.*</param>
    </targetClasses>

    <targetTests>
        <param>com.example.modulea.*</param>
    </targetTests>

    <mutators>
        <mutator>DEFAULTS</mutator>
    </mutators>

    <threads>4</threads>
    <jvmArgs>
        <jvmArg>-Xmx2G</jvmArg>
    </jvmArgs>

    <!-- produce reports in module/target/pit-reports -->
    <reportsDirectory>${project.build.directory}/pit-reports</reportsDirectory>

    <!-- important for aggregator -->
    <timestampedReports>false</timestampedReports>

    <!-- include or exclude integration tests if needed -->
    <excludedTestClasses>
        <param>**.*IT</param>
    </excludedTestClasses>
</configuration>
```

---

## 8) Troubleshooting (most frequent problems & fixes)

* **PIT can’t find tests or classes**

    * Ensure `target/classes` and `target/test-classes` are present (use `mvn test-compile` first).
    * Check `targetClasses` & `targetTests` patterns — they must match your packages (wildcards `com.mycompany.*`).
    * If using Java 9+, set `<useModulePath>false</useModulePath>` for Surefire or configure module path properly.

* **Aggregate fails: "expect linecoverage.xml / mutations.xml"**

    * Make sure `timestampedReports=false` and `reportsDirectory` is stable in every module (so files exist exactly at
      `module/target/pit-reports/mutations.xml` etc).
    * Run full reactor from root so submodules generate those files before aggregator runs.

* **Report files contain timestamps in their filenames**

    * That breaks aggregation. Set `timestampedReports` to `false`.

* **Memory / performance problems**

    * Increase `jvmArgs` or reduce mutants (`maxMutationsPerClass`) and increase threads appropriately.
    * For quick dev runs use smaller `mutators` or `mutationThreshold`.

* **JUnit 5 support**

    * PIT supports JUnit 5, but ensure JUnit 5 test engine is present and that surefire/junit platforms are compatible;
      usually using JUnit Jupiter 5.9+ and latest surefire works. If tests use custom runners/providers, double-check
      compatibility.

* **Plugin not executed / inherits not applied**

    * Modules must **declare the plugin** inside `<build><plugins>` to inherit pluginManagement executions/config. If
      you forget to add `<plugin><groupId>org.pitest</groupId><artifactId>pitest-maven</artifactId></plugin>` in the
      module, inheritance may not apply.

---

## 9) Minimal example (quick files to paste)

### parent `pom.xml` — minimal relevant parts

(see the full example in step 2 above — copy/paste that)

### module-a `pom.xml`

```xml

<project>
    <parent>... parent info ...</parent>
    <artifactId>module-a</artifactId>

    <build>
        <plugins>
            <plugin>
                <groupId>org.pitest</groupId>
                <artifactId>pitest-maven</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### aggregator `pom.xml` (copy/paste the aggregate example in step 4)

---

## 10) Extra: run PIT without Maven plugin (command-line troubleshooting)

If plugin behavior is confusing, you can run PIT via command line jar to validate classpath and patterns. See PIT
command-line quickstart:

```bash
java -cp "<pit-jar-and-deps>:<project-classpath>" \
  org.pitest.mutationtest.commandline.MutationCoverageReport \
  --reportDir target/pit-debug \
  --targetClasses com.example.* \
  --targetTests com.example.*
```

This is advanced but useful to debug classpath problems.

---

## 11) Final checklist before running

* [ ] Parent `pluginManagement` contains pitest config (reportsDirectory, timestampedReports=false).
* [ ] Each module declares `org.pitest:pitest-maven` in `<plugins>`.
* [ ] Aggregator module configured to run `pitest-aggregate-report` with `<goal>report-aggregate</goal>` bound to
  `verify`.
* [ ] Tests compile fine with `mvn -DskipTests=false test-compile`.
* [ ] Run `mvn -Ppit clean verify` **from project root**.

---

If you want I can:

* Generate a **ready-to-run minimal repo** (parent + 2 modules + aggregator) with exact POMs so you can `git clone` and
  run `mvn -Ppit verify` locally and see a working aggregated report.
* Or, if you paste your `pom.xml` (parent + one failing module), I’ll point out exactly what to change in your files to
  make PIT run and aggregate.

Which do you prefer?
