FROM maven:3.9.9-eclipse-temurin-21 AS builder
LABEL authors="yelazami"

WORKDIR /app

COPY . .

# Étape 1 : Pré-téléchargement des dépendances Maven du module "analytics-service"
# Cela permet de préparer le cache Maven en avance, ce qui accélère les builds futurs
# et évite de télécharger les dépendances à chaque changement mineur.

RUN mvn -f pom.xml -pl analytics-service dependency:go-offline

# Étape 2 : Nettoyage et compilation du module "analytics-service" sans exécuter les tests
# Cela génère un fichier JAR (ou WAR) exécutable pour le déploiement, plus rapidement.
RUN mvn -f pom.xml -pl analytics-service clean package -DskipTests


# Étape 2 : Exécuter avec OpenJDK
FROM openjdk:21-jdk AS runner

WORKDIR /app

# Copier le JAR généré depuis l'étape précédente
COPY --from=builder /app/analytics-service/target/analytics-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4002

ENTRYPOINT ["java", "-jar", "app.jar"]
