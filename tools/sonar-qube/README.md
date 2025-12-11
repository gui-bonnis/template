Here is a **production-grade but lightweight `docker-compose.yml`** that spins up **SonarQube Community Edition**
locally with:

✅ PostgreSQL (required in production; faster & more stable than embedded DB)
✅ SonarQube 10.x
✅ Persisted volumes
✅ Ready for local analysis (`http://localhost:9000`)
✅ Works on macOS / Linux / Windows

You can copy-paste and run directly.

---

# 🚀 **docker-compose.yml (Recommended Setup: SonarQube + PostgreSQL)**

```yaml
version: "3.9"

services:
  sonarqube:
    image: sonarqube:10-community
    container_name: sonarqube
    depends_on:
      - db
    ports:
      - "9000:9000"
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
      SONAR_SEARCH_JAVAADDITIONALOPTS: -Dnode.store.allow_mmap=false
    ulimits:
      nofile:
        soft: 65536
        hard: 65536
    volumes:
      - sonar_data:/opt/sonarqube/data
      - sonar_extensions:/opt/sonarqube/extensions
      - sonar_logs:/opt/sonarqube/logs

  db:
    image: postgres:14
    container_name: sonarqube-db
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
      POSTGRES_DB: sonar
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U sonar" ]
      interval: 5s
      timeout: 5s
      retries: 10

volumes:
  sonar_data:
  sonar_extensions:
  sonar_logs:
  postgres_data:
```

---

# ▶️ How to run

```sh
docker compose up -d
```

Then open:

👉 **[http://localhost:9000](http://localhost:9000)**

Login:

* **Username:** `admin`
* **Password:** `admin` (you will be asked to change it)

---

# 🧰 Optional: Lighter version (no Postgres)

Suitable for quick tests, not for any real persistence.

```yaml
version: "3.9"

services:
  sonarqube:
    image: sonarqube:10-community
    ports:
      - "9000:9000"
    environment:
      SONAR_ES_BOOTSTRAP_CHECKS_DISABLE: "true"
    volumes:
      - sonar_data:/opt/sonarqube/data
      - sonar_extensions:/opt/sonarqube/extensions

volumes:
  sonar_data:
  sonar_extensions:
```

**Warning:** The embedded H2-like DB is deprecated and loses data when SonarQube updates.

---

# 🧪 Test local analysis

From your project:

### Maven:

```sh
mvn clean verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=admin \
  -Dsonar.password=admin
```

### Gradle:

```sh
./gradlew sonarqube \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=admin \
  -Dsonar.password=admin
```

---

# 🔌 Optional Plugins

If you want **PIT mutation testing support**, install it inside SonarQube UI:

`Administration → Marketplace → PIT Mutation Analysis → Install`

Then restart SonarQube.

---

Yes — you can start SonarQube with Docker Compose **and** have it pre-load Quality Profiles and Quality Gates
automatically.
Below I give you a complete, ready-to-run solution:

* `docker-compose.yml` (SonarQube + Postgres + an init container)
* `init/seed-sonar.sh` — a script that waits for Sonar to be healthy and then:

    * restores one or more Quality Profile XML files (via `api/qualityprofiles/restore`)
    * creates a Quality Gate and adds conditions (via `api/qualitygates/*`)
    * (optionally) assigns a profile / gate to a project

I also include the exact `curl` commands you can customize. The approach uses SonarQube Web API calls described in the
Sonar docs and Web API reference. ([docs.sonarsource.com][1])

---

# 1) Files you’ll create

Project layout:

```
sonar-seed/
├─ docker-compose.yml
└─ init/
   ├─ seed-sonar.sh
   └─ profiles/
      └─ java-custom-profile.xml     # exported profile(s) you want to restore
```

---

# 2) `docker-compose.yml`

This is the same Postgres-backed Sonar setup from before, plus an `init` service that runs once (and exits) to seed
Sonar via the REST API.

```yaml
version: "3.9"

services:
  sonarqube:
    image: sonarqube:10-community
    container_name: sonarqube
    depends_on:
      - db
    ports:
      - "9000:9000"
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
      # avoid mmap issues on some hosts
      SONAR_SEARCH_JAVAADDITIONALOPTS: -Dnode.store.allow_mmap=false
    ulimits:
      nofile:
        soft: 65536
        hard: 65536
    volumes:
      - sonar_data:/opt/sonarqube/data
      - sonar_extensions:/opt/sonarqube/extensions
      - sonar_logs:/opt/sonarqube/logs

  db:
    image: postgres:14
    container_name: sonarqube-db
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
      POSTGRES_DB: sonar
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U sonar" ]
      interval: 5s
      timeout: 5s
      retries: 10

  sonarqube-init:
    image: curlimages/curl:latest
    container_name: sonarqube-init
    depends_on:
      - sonarqube
    # mount your init script and exported profiles into the container
    volumes:
      - ./init:/init
    entrypoint: [ "/bin/sh", "/init/seed-sonar.sh" ]
    environment:
      SONAR_URL: "http://sonarqube:9000"
      SONAR_USER: "admin"
      SONAR_PASS: "admin"     # change this for security (or use token)
    restart: "no"

volumes:
  sonar_data:
  sonar_extensions:
  sonar_logs:
  postgres_data:
```

Run with:

```bash
docker compose up -d
# The init container will run, seed Sonar, then exit.
```

---

# 3) `init/seed-sonar.sh` (example)

Save this file as `init/seed-sonar.sh` and make it executable (`chmod +x init/seed-sonar.sh`). Edit
names/paths/conditions to match your desired profile and gate.

```sh
#!/bin/sh
set -e

SONAR_URL="${SONAR_URL:-http://localhost:9000}"
SONAR_AUTH="${SONAR_USER}:${SONAR_PASS}"

# Wait for SonarQube to be ready (HTTP 200 on /api/server/health)
echo "Waiting for SonarQube at $SONAR_URL ..."
until curl -s -u "$SONAR_AUTH" "$SONAR_URL/api/system/health" | grep -q '"status":"GREEN"'; do
  echo "  not ready yet..."
  sleep 3
done
echo "SonarQube ready."

# === 1) Restore quality profiles (if you exported them to init/profiles/) ===
PROFILES_DIR="/init/profiles"
if [ -d "$PROFILES_DIR" ]; then
  for f in "$PROFILES_DIR"/*.xml; do
    [ -e "$f" ] || continue
    echo "Restoring quality profile from $f ..."
    # API: POST /api/qualityprofiles/restore (multipart form, field 'backup')
    curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualityprofiles/restore" \
      -F "backup=@${f}" \
      | jq -r '.'
  done
else
  echo "No profiles dir found at $PROFILES_DIR; skipping restore."
fi

# === 2) Create a custom Quality Gate ===
GATE_NAME="My Custom Gate"
echo "Creating quality gate: $GATE_NAME"
# create: POST /api/qualitygates/create?name=...
CREATE_RESPONSE=$(curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualitygates/create" --data-urlencode "name=${GATE_NAME}")
# Response: {"id":"<id>","name":"My Custom Gate"}
GATE_ID=$(echo "$CREATE_RESPONSE" | jq -r '.id')
echo "Quality gate id: $GATE_ID"

# === 3) Add conditions to the quality gate ===
# Example: fail if new coverage < 80%
echo "Adding conditions to quality gate..."
# API: POST /api/qualitygates/create_condition
curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualitygates/create_condition" \
  --data "gateId=${GATE_ID}" --data "metric=new_coverage" --data "op=LT" --data "error=80"

# fail if overall coverage < 70%
curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualitygates/create_condition" \
  --data "gateId=${GATE_ID}" --data "metric=coverage" --data "op=LT" --data "error=70"

# fail if number of new bugs > 0
curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualitygates/create_condition" \
  --data "gateId=${GATE_ID}" --data "metric=new_bugs" --data "op=GT" --data "error=0"

echo "Conditions added."

# === 4) Optionally set this gate as default ===
# curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualitygates/set_as_default" --data "gateId=${GATE_ID}"

# === 5) (Optional) Assign a profile to a project and set quality gate to a project ===
# Example: assign Java profile named "My Java Profile" to project key "my:project"
# Note: profile-assignment uses api/qualityprofiles/add_project
PROJECT_KEY="my:project"
PROFILE_LANG="java"
PROFILE_NAME="My Java Profile"

if [ ! -z "$PROJECT_KEY" ]; then
  echo "Associating quality gate $GATE_NAME to project $PROJECT_KEY ..."
  # API: POST /api/qualitygates/select?gateId=...&projectId=... (or projectKey)
  curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualitygates/select" --data "gateId=${GATE_ID}" --data "projectKey=${PROJECT_KEY}" || true

  echo "Associating quality profile '$PROFILE_NAME' (lang=$PROFILE_LANG) to project $PROJECT_KEY ..."
  # API: POST /api/qualityprofiles/add_project
  curl -s -u "$SONAR_AUTH" -X POST "$SONAR_URL/api/qualityprofiles/add_project" \
    --data "language=${PROFILE_LANG}" --data "profileName=${PROFILE_NAME}" --data "project=${PROJECT_KEY}" || true
fi

echo "Seeding complete."
```

**Notes on the script above**

* It uses SonarWeb API endpoints:

    * `/api/qualityprofiles/restore` to import profile XMLs. ([docs.sonarsource.com][1])
    * `/api/qualitygates/create` and `/api/qualitygates/create_condition` to create gate and
      conditions. ([docs.sonarsource.com][2])
    * `/api/qualitygates/select` to associate a gate to a project. ([Stack Overflow][3])
    * `/api/qualityprofiles/add_project` to assign a profile to a project (useful after you restore/import a
      profile). ([Stack Overflow][4])

* The `restore` endpoint expects the same XML format you get from the UI “Backup” action. You can export a profile from
  an existing Sonar instance and place it at `init/profiles/java-custom-profile.xml`. ([docs.sonarsource.com][1])

* You can also set your custom gate as default with `api/qualitygates/set_as_default` (uncomment line in the script).

---

# 4) How to get/export a Quality Profile XML (from your source Sonar)

1. On the Sonar UI: **Quality Profiles → select a profile → Back up** (this downloads an XML).
2. Put that XML into `init/profiles/` before you run `docker compose up`. Restoring via `/api/qualityprofiles/restore`
   will recreate that profile in the new instance. ([docs.sonarsource.com][1])

---

# 5) Permissions & authentication

* The API calls to create gates and restore profiles need an **admin-level account** (or a token with sufficient
  rights). For simplicity the sample uses `admin:admin`. In production use:

    * a generated user token (preferred) or
    * change the admin password and use secret management for the init step.

* If you prefer tokens, set `SONAR_TOKEN` in env and use `-H "Authorization: Bearer $SONAR_TOKEN"` with `curl`.

---

# 6) What the Web API can and cannot do (important)

* **Quality Profiles** can be exported (backup) and restored programmatically. Use that for consistent rule sets across
  instances. ([docs.sonarsource.com][1])
* **Quality Gates** cannot be restored from a single XML backup the way profiles can; you create them via the Web API (
  create + create_condition) or copy an existing gate. That is why the script creates the gate and adds conditions
  programmatically. ([Sonar Community][5])

---

# 7) Example run

1. Put your exported profile(s) under `init/profiles/` (one or more `.xml` files).
2. `docker compose up --build`

    * Sonar starts (Postgres initializes).
    * `sonarqube-init` will wait until Sonar is GREEN and perform the imports and API calls.
3. Visit `http://localhost:9000` → Quality Profiles / Quality Gates to verify.

---

# 8) Tips & Caveats

* If you have **hundreds of profiles/rules** or are doing complex migrations, consider using the **qualityprofiles
  backup/restore** in combination with a DB backup/restore for full fidelity — but beware DB restores are heavier and
  environment-specific. See Sonar docs & community threads. ([Sonar Community][5])
* If your profiles reference plugin-specific rules, ensure the plugin is installed (placed under `extensions/plugins` or
  added before restore) — otherwise some rules may not import.
* Run the init script only once (container set to restart: "no"). If you rerun, the script will attempt to recreate
  gates / profiles — build idempotency into your script (check if gate/profile exists before creating).
* Use a token instead of `admin:admin` in CI.

---

# References / sources

* SonarQube docs — Quality Profiles (backup/restore). ([docs.sonarsource.com][1])
* SonarQube Web API — `api/qualityprofiles`, `api/qualitygates`. ([next.sonarqube.com][6])
* Community guidance on automated profile & gate provisioning and `api/qualitygates/select`. ([Sonar Community][5])

---

If you want, I can:

* adapt the script to be **idempotent** (check if gate/profile exists before creating/restoring),
* add example **quality gate conditions** matching your company rules (security/reliability/coverage thresholds), or
* produce a version that **uses a Sonar token** and securely reads it from an env file or Docker secret.

Which of those do you want next?

[1]: https://docs.sonarsource.com/sonarqube-server/10.6/instance-administration/quality-profiles?utm_source=chatgpt.com "Quality profiles | SonarQube Server 10.6"

[2]: https://docs.sonarsource.com/sonarqube-server/10.8/instance-administration/analysis-functions/quality-gates?utm_source=chatgpt.com "Quality gates | SonarQube Server 10.8"

[3]: https://stackoverflow.com/questions/51295203/how-to-forcibly-set-a-quality-gate-on-first-run-of-a-sonarqube-project?utm_source=chatgpt.com "How to forcibly set a quality gate on first run of a sonarqube ..."

[4]: https://stackoverflow.com/questions/44530552/automatically-associate-new-sonar-project-with-custom-quality-profile-and-qualit?utm_source=chatgpt.com "Automatically associate new Sonar project with custom ..."

[5]: https://community.sonarsource.com/t/how-to-move-quality-profiles-quality-gates-and-rules-from-one-sonarqube-to-another/44038?utm_source=chatgpt.com "How to move Quality Profiles, Quality Gates and Rules from ..."

[6]: https://next.sonarqube.com/sonarqube/web_api/api/qualityprofiles?utm_source=chatgpt.com "api/qualityprofiles - Web API - SonarQube Server"

