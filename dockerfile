# Usar imagen base de Java
FROM eclipse-temurin:21-jdk-alpine

# Carpeta donde se alojará la app
WORKDIR /app

# Copiar el JAR al contenedor
COPY target/herotraining-backend-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto en el que corre Spring Boot
EXPOSE 8083

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"] 
