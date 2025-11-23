# --- ETAPA 1: CONSTRUCCIÓN (BUILD) ---
# Usamos una imagen de Java 17 con Gradle para compilar
FROM gradle:8.5-jdk17 AS build

# Copiamos el código fuente al contenedor
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Damos permisos de ejecución al wrapper de gradle (por si acaso)
RUN chmod +x ./gradlew

# Compilamos y generamos el archivo JAR (sin correr tests para hacerlo rápido en la nube)
RUN ./gradlew bootJar --no-daemon

# --- ETAPA 2: EJECUCIÓN (RUNTIME) ---
# Usamos una imagen ligera de Java 17 solo para correr la app
FROM eclipse-temurin:17-jdk-alpine

# Exponemos el puerto 8080 (donde escucha Spring Boot)
EXPOSE 8080

# Copiamos solo el JAR compilado desde la etapa anterior
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]