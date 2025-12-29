# syntax=docker/dockerfile:1.7
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Maven settings: mirror + more resilient downloads + cache deps
COPY maven-settings.xml /root/.m2/settings.xml
COPY . .

# Retry (HTTP 5xx) + longer timeouts (useful on slow networks)
ENV MAVEN_OPTS="-Dmaven.wagon.http.retryHandler.count=10 -Dmaven.wagon.http.retryHandler.requestSentEnabled=true -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 -Dmaven.wagon.rto=300000"

RUN --mount=type=cache,target=/root/.m2     chmod +x ./mvnw && ./mvnw -B -DskipTests -Dmaven.repo.local=/root/.m2 --no-transfer-progress package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app/app.jar"]
