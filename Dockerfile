# Usa un'immagine con Maven e JDK 17
FROM maven:3.9.4-eclipse-temurin-17

# Imposta la directory di lavoro
WORKDIR /app

# Copia il progetto
COPY . .

# Compila il progetto (senza test)
RUN mvn clean package -DskipTests

# Avvia l'applicazione Spring Boot
CMD ["java", "-jar", "target/*.jar"]
