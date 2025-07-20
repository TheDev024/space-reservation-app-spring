FROM gradle:8-jdk21 AS build

WORKDIR /app

COPY . /app

ENTRYPOINT ["./gradlew", "bootJar"]

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/build/libs/application.jar /app/

ENTRYPOINT ["java", "-jar", "application.jar"]
