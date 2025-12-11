# Étape 1 : construire l'application avec Maven
FROM maven:3.9.2-eclipse-temurin-11 AS build
WORKDIR /app

# Copier le pom.xml et le code source
COPY pom.xml .
COPY src ./src

# Construire le JAR en ignorant les tests
RUN mvn clean package -DskipTests

# Étape 2 : créer l'image finale avec JDK
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copier le JAR généré depuis l'étape de build
COPY --from=build /app/target/FinanceApp-1.0-SNAPSHOT.jar app.jar

# Exposer le port sur lequel l'application écoute
EXPOSE 8080

# Lancer l'application
ENTRYPOINT ["java","-jar","app.jar"]
