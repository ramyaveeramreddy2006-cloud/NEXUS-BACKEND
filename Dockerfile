FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -B -ntp -Dmaven.wagon.http.retryHandler.count=5 -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN groupadd --system --gid 10001 nexus \
    && useradd --system --uid 10001 --gid nexus --no-create-home nexus
COPY --from=build --chown=nexus:nexus /workspace/target/nexus-platform-1.0.0.jar /app/app.jar
USER nexus
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
