# =======================
# ETAPA 1: BUILD CON MAVEN
# =======================
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom primero para cachear dependencias
COPY pom.xml .

RUN mvn dependency:go-offline

# Copiar el código fuente
COPY src ./src

# Compilar sin tests
RUN mvn clean package -DskipTests

# ==========================
# ETAPA 2: RUNTIME JAVA 21
# ==========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Render usa 8080
EXPOSE 8080

# Ejecutar Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
