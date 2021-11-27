#1. Build
FROM maven:3.8-openjdk-11 AS build
WORKDIR /app
COPY ./ /app
RUN mvn clean install

#2. Run jar
FROM openjdk:11-jre
WORKDIR /app
COPY --from=build /app/target/kniffel-0.0.1.jar /app/app.jar
CMD java -jar /app/app.jar
EXPOSE 8080