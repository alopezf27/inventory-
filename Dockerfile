# Etapa 1: Construcción de la app
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
# reempaca el jar para que sea ejecutable
RUN mvn -DskipTests package spring-boot:repackage

# Etapa 2: Runtime
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/inventory
ENV SPRING_DATASOURCE_USERNAME=app
ENV SPRING_DATASOURCE_PASSWORD=app
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]