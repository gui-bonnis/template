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
