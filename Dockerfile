FROM gradle:7.6-jdk17 AS build

WORKDIR /home/gradle/project

COPY . .

RUN gradle build --no-daemon -x test

FROM openjdk:17-jdk-alpine

COPY --from=build /home/gradlge/project/build/libs/app.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]