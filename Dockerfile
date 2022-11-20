FROM openjdk:17-oracle

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY ./build/libs/takehome-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080
CMD ["java", "-jar", "takehome-0.0.1-SNAPSHOT.jar"]