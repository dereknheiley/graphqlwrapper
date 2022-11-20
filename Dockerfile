FROM gradle:7.5.1-jdk17-alpine as build-env
WORKDIR /app
COPY . /app
RUN gradle clean bootJar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build-env /app/build/libs/takehome-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080
CMD ["java", "-jar", "takehome-0.0.1-SNAPSHOT.jar"]