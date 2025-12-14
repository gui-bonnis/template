# Simple, standalone Dockerfile for production-grade container
FROM eclipse-temurin:25-jre-alpine

# Create non-root user for security
RUN addgroup -S appuser && adduser -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy the pre-built JAR (built by Maven separately)
COPY boot/target/*.jar app.jar

# Install curl for health checks (minimal footprint)
RUN apk add --no-cache curl

# Change ownership and switch to non-root user
RUN chown -R appuser:appuser /app
USER appuser

# Expose ports
EXPOSE 8080 8081

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8081/actuator/health || exit 1

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]