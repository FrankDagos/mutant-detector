# 🧬 Mutant Detector API

**Autor: Franco D'Agostino --- Legajo 47761**

![Java
17](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring
Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?style=flat-square)
![Coverage](https://img.shields.io/badge/Coverage-%3E85%25-brightgreen?style=flat-square)
![Deploy](https://img.shields.io/badge/Deploy-Render-blue?style=flat-square)

API REST desarrollada para detectar mutantes basándose en su secuencia
de ADN. Proyecto realizado para materia Desarrollo de Software de la UTN Facultad Regional Mendoza.

👉 **Repositorio en Github URL:**\
https://github.com/FrankDagos/mutant-detector

------------------------------------------------------------------------

## ☁️ Demo Online (Deploy)

La aplicación se encuentra desplegada en **Render** y lista para ser
probada:

👉 **Swagger UI (Documentación Interactiva):**\
https://mutant-detector-api-1q1n.onrender.com/swagger-ui.html

👉 **URL Base de la API:**\
`https://mutant-detector-api-1q1n.onrender.com`

> **Nota:** Al estar alojado en un servicio gratuito, la primera
> petición puede tardar entre 40 y 60 segundos en "despertar" el
> servidor.

------------------------------------------------------------------------

## 🚀 Tecnologías Utilizadas

-   **Java 17**
-   **Spring Boot 3.4.12**
-   **H2 Database**
-   **Spring Data JPA**
-   **Lombok**
-   **JUnit 5 & Mockito**
-   **OpenAPI (Swagger)**
-   **Docker**

------------------------------------------------------------------------

## 📡 Endpoints de la API

### 1. **Detectar Mutante --- `POST /mutant`**

Analiza una secuencia de ADN.

**Respuestas posibles:** - **200 OK** → Es mutante (más de 1 secuencia
encontrada) - **403 Forbidden** → Es humano - **400 Bad Request** → ADN
inválido

**Ejemplo de Body (Mutante):**

``` json
{
  "dna": [
    "ATGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCCCTA",
    "TCACTG"
  ]
}
```

**Ejemplo de Body (Humano):**

``` json
{
  "dna": [
    "GTGCGA",
    "CAGTGC",
    "TTATGT",
    "AGAAGG",
    "CCTCTA",
    "TCACTG"
  ]
}
```

------------------------------------------------------------------------

### 2. **Estadísticas --- `GET /stats`**

Devuelve conteos de verificaciones y el ratio.

**Ejemplo de respuesta:**

``` json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
```

------------------------------------------------------------------------

## 🛠️ Instrucciones para Ejecutar Localmente

### 1. Clonar el repositorio

``` bash
git clone https://github.com/FrankDagos/mutant-detector.git
cd mutant-detector
```

### 2. Compilar y ejecutar

``` bash
./gradlew bootRun
```

### 3. Acceder a la app local

-   **API Local:** http://localhost:8080\
-   **Swagger UI:** http://localhost:8080/swagger-ui.html\
-   **Consola H2:** http://localhost:8080/h2-console

------------------------------------------------------------------------

## 🧪 Testing y Cobertura

El proyecto cuenta con más del **85% de cobertura**.

Para ejecutar los tests y generar el reporte:

``` bash
./gradlew test jacocoTestReport
```

El reporte HTML estará disponible en:\
`build/reports/jacoco/test/html/index.html`

------------------------------------------------------------------------

## 🏗️ Detalles de Arquitectura

El proyecto utiliza una arquitectura en **6 capas**:

-   **Controller:** Manejo de HTTP.\
-   **DTO:** Validación de entrada.\
-   **Service:** Lógica de negocio.\
-   **Domain/Model:** Entidades y núcleo del detector.\
-   **Repository:** Persistencia de datos.\
-   **Config:** Swagger y manejos globales de excepciones.

### ✨ Optimizaciones Implementadas

-   **Algoritmo O(N):** Recorrido único de la matriz.\
-   **Early Termination:** Se detiene apenas se detectan más de una
    secuencia.\
-   **Caching con Hash (SHA-256):**\
    Evita recalcular ADN ya analizado, mejorando enormemente el tiempo
    de respuesta.

------------------------------------------------------------------------

## 🔄 Diagrama de Secuencia

El siguiente diagrama ilustra cómo se procesa cada solicitud de análisis de ADN para asegurar eficiencia y evitar cálculos duplicados:

```mermaid
sequenceDiagram
    participant C as Cliente (API Consumer)
    participant Ctrl as MutantController
    participant Svc as MutantService
    participant Det as MutantDetector
    participant Repo as DnaRecordRepository
    participant DB as H2 Database

    C->>Ctrl: POST /mutant {dna}
    Ctrl->>Svc: analyzeDna(dna)
    
    Note over Svc: 1. Calcula Hash SHA-256 del ADN
    
    Svc->>Repo: findByDnaHash(hash)
    Repo->>DB: SELECT * FROM dna_records WHERE hash = ?
    DB-->>Repo: (Result / Null)
    Repo-->>Svc: Optional<DnaRecord>

    alt ADN ya analizado (Cache Hit)
        Svc-->>Ctrl: Retorna resultado guardado (isMutant)
    else ADN nuevo
        Svc->>Det: isMutant(dna)
        Note over Det: Ejecuta Algoritmo O(N)
        Det-->>Svc: true / false
        
        Svc->>Repo: save(newRecord)
        Repo->>DB: INSERT INTO dna_records ...
        
        Svc-->>Ctrl: Retorna nuevo resultado
    end

    alt isMutant == true
        Ctrl-->>C: 200 OK
    else isMutant == false
        Ctrl-->>C: 403 Forbidden
    end
