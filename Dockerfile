# Usa un'immagine con Maven e JDK 17
FROM maven:3.9.4-eclipse-temurin-17

# Imposta la directory di lavoro
WORKDIR /app

# Copia tutto il progetto nel container
COPY . .

# Compila il progetto (senza test) e rinomina il JAR in "app.jar"
RUN mvn clean package -DskipTests && \
    cp target/*.jar app.jar

# Avvia l'applicazione Spring Boot
CMD ["java", "-jar", "app.jar"]
