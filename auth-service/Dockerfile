FROM maven:3.9.9-eclipse-temurin-21 AS builder
LABEL authors="yelazami"

WORKDIR /app

COPY . .

RUN mvn -f pom.xml -pl auth-service dependency:go-offline && \
    mvn -f pom.xml -pl auth-service clean package -DskipTests


# Étape 2 : Exécuter avec OpenJDK
FROM openjdk:21-jdk AS runner

WORKDIR /app

# Copier le JAR généré depuis l'étape précédente
COPY --from=builder /app/auth-service/target/auth-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4005

ENTRYPOINT ["java", "-jar", "app.jar"]
