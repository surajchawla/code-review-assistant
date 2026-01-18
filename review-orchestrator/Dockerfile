# ============================
# 1. Build Stage
# ============================
FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy only the pom first (leverages Docker layer caching)
COPY pom.xml .

# Pre-fetch dependencies
RUN mvn -q dependency:go-offline

# Copy the rest of the source
COPY src ./src

# Build the application
RUN mvn -q clean package -DskipTests


# ============================
# 2. Runtime Stage
# ============================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8080

# JVM optimizations for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]