# Multi-stage build for enterprise-grade container
FROM maven:3-eclipse-temurin-25 AS builder

WORKDIR /app

# Copy parent pom.xml and module pom.xml
COPY ../pom.xml ..
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:25-jre-alpine AS runtime

WORKDIR /app

# Create non-root user
RUN addgroup -S appuser && adduser -S appuser -G appuser

# Copy JAR from builder
COPY --from=builder /app/target/*.jar app.jar

# Install curl for healthcheck
RUN apk add --no-cache curl

# Set permissions
RUN chown -R appuser:appuser /app
USER appuser

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

EXPOSE 8080 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]