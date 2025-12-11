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
  	docker compose -f tools/obs-infrastructure/docker-compose.yaml up

.PHONY: doc_observability_infrastructure_down
doc_observability_infrastructure_down:
	cd code && \
	docker compose -f tools/obs-infrastructure/docker-compose.yaml down