# Usar una imagen base de Java
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el .jar al contenedor
COPY target/EstudioLibre-0.0.1-SNAPSHOT.jar app.jar

# Comando para ejecutar el .jar
ENTRYPOINT ["java", "-jar", "app.jar"]
