FROM maven:3.8.5-openjdk-17-slim AS build

COPY src /app/src
COPY pom.xml /app

RUN mvn -B test -f /app/pom.xml clean package

FROM eclipse-temurin:17.0.5_8-jre-ubi9-minimal

COPY --from=build /app/target/MailgramBot-1.0-SNAPSHOT-jar-with-dependencies.jar /app/bot.jar
ENTRYPOINT ["java", "-jar", "/app/bot.jar"]
