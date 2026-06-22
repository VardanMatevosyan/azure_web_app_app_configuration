FROM eclipse-temurin:25-jdk-alpine AS builder
LABEL authors="Vardan"
WORKDIR /achieve
COPY . /achieve
RUN ./mvnw clean package


FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
COPY --from=builder /achieve/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Xms512m", "-Xmx1024m", "-jar", "app.jar"]
