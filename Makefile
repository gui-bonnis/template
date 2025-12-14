.PHONY: help
help:
	@echo "Available targets:"
	@echo "  build              - Build the project JAR (skip tests)"
	@echo "  build-with-tests   - Build the project JAR with tests"
	@echo "  docker-build       - Build the Docker image"
	@echo "  test-unit          - Run unit tests"
	@echo "  test-integration   - Run integration tests"
	@echo "  test-mutation      - Run mutation tests"
	@echo "  test-all           - Run all tests (unit + integration + mutation)"
	@echo "  run-local          - Run the app with local profile"
	@echo "  local-up           - Start local dev infrastructure (Docker Compose)"
	@echo "  local-down         - Stop local dev infrastructure"
	@echo "  quality-up         - Start code quality tools (SonarQube)"
	@echo "  quality-down       - Stop code quality tools"
	@echo "  perf-security-up   - Start load/perf/security tools (JMeter + ZAP)"
	@echo "  perf-security-down - Stop load/perf/security tools"
	@echo "  clean-all          - Stop all Docker services and clean builds"
	@echo "  install            - Install dependencies (legacy)"
	@echo "  installtest        - Install with tests (legacy)"
	@echo "  run                - Run app (legacy)"
	@echo "  doc_observability_infrastructure_up   - Start observability (legacy)"
	@echo "  doc_observability_infrastructure_down - Stop observability (legacy)"

# Build targets
.PHONY: build
build:
	cd code && \
	mvn clean package -pl boot -am -DskipTests

.PHONY: build-with-tests
build-with-tests:
	cd code && \
	mvn clean package -pl boot -am

.PHONY: docker-build
docker-build:
	cd code && \
	docker build -f Dockerfile -t customer-service:latest .

# Test targets
.PHONY: test-unit
test-unit:
	cd code && \
	mvn test -pl boot -am

.PHONY: test-integration
test-integration:
	cd code && \
	mvn verify -pl boot -am

.PHONY: test-mutation
test-mutation:
	cd code && \
	mvn -Ppit verify -pl boot -am

.PHONY: test-all
test-all: test-unit test-integration test-mutation

# Run targets
.PHONY: run-local
run-local:
	cd code/boot && \
	mvn spring-boot:run -Dspring.profiles.active=local

# Docker Compose targets
.PHONY: local-up
local-up:
	cd tools && \
	docker-compose -f docker-compose.local.yml up -d

.PHONY: local-down
local-down:
	cd tools && \
	docker-compose -f docker-compose.local.yml down

.PHONY: quality-up
quality-up:
	cd tools && \
	docker-compose -f docker-compose.quality.yml up -d

.PHONY: quality-down
quality-down:
	cd tools && \
	docker-compose -f docker-compose.quality.yml down

.PHONY: perf-security-up
perf-security-up:
	cd tools && \
	docker-compose -f docker-compose.perf-security.yml up

.PHONY: perf-security-down
perf-security-down:
	cd tools && \
	docker-compose -f docker-compose.perf-security.yml down

# Clean targets
.PHONY: clean-all
clean-all: local-down quality-down perf-security-down doc_observability_infrastructure_down
	cd code && \
	mvn clean

# Legacy targets (kept for compatibility)
.PHONY: install
install:
	cd code/boot && \
	mvn clean install -DskipTests

.PHONY: installtest
installtest:
	cd code/boot && \
	mvn clean install

.PHONY: run
run:
	cd code/boot && \
	mvn spring-boot:run

.PHONY: doc_observability_infrastructure_up
doc_observability_infrastructure_up:
	cd code && \
	docker-compose -f tools/obs-infrastructure/docker-compose.yaml up

.PHONY: doc_observability_infrastructure_down
doc_observability_infrastructure_down:
	cd code && \
	docker-compose -f tools/obs-infrastructure/docker-compose.yaml down