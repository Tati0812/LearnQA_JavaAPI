FROM maven:3.9.10-eclipse-temurin-21
WORKDIR /tests
COPY . .
CMD mvn clean test