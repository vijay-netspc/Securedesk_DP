# ---- Stage 1: build the jar with Maven + JDK 17 ----
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first so they're cached across rebuilds unless pom.xml changes
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

# ---- Stage 2: run the jar on a slim JRE-only image ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/securedesk-1.0.0.jar app.jar

# Render sets $PORT at runtime; application.properties already reads it via ${PORT:8080}
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
