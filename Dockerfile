FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY settings.xml .
COPY src ./src
RUN mvn -s settings.xml -B -ntp -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
RUN groupadd --system --gid 10001 nexus \
    && useradd --system --uid 10001 --gid nexus --no-create-home nexus
COPY --from=build --chown=nexus:nexus /workspace/target/nexus-platform-1.0.0.jar /app/app.jar
USER nexus
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Xss512k -XX:+ExitOnOutOfMemoryError"
EXPOSE 8081
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
