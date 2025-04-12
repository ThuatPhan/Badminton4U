# Stage 1: Build JAR file
FROM openjdk:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Run application
FROM openjdk:17-jdk-alpine
WORKDIR /app
RUN apk update && apk add --no-cache curl
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
