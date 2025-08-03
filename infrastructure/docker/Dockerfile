# Build stage
FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:24-jdk-alpine
COPY --from=build /app/target/equipassa-*.jar /app/equipassa.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/equipassa.jar"]