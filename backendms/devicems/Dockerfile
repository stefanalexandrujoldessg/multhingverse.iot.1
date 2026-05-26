# ── Stage 1: build ────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-11 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

COPY src ./src
RUN mvn package -DskipTests -B --no-transfer-progress

# ── Stage 2: runtime ───────────────────────────────────────────────────
FROM eclipse-temurin:11-jre

RUN groupadd -r app && useradd -r -g app app

WORKDIR /app
RUN mkdir -p /app/uploads && chown app:app /app/uploads

COPY --from=build /app/target/devicemicroservice.jar app.jar

USER app

EXPOSE 9092

ENTRYPOINT ["java", "-jar", "app.jar"]
