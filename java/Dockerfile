#1. Build
FROM maven:3.8.6-eclipse-temurin-17-alpine AS build-step
WORKDIR /app
COPY ./ /app
RUN mvn clean package

#2. Run jar
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=build-step /app/target/*.jar /app/kniffel.jar
CMD java -jar /app/kniffel.jar
EXPOSE 8080