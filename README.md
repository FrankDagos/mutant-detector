# 🧬 Mutant Detector API

**Autor:** Franco D'Agostino\
**Legajo:** 47761\
**Comisión:** 3K9

![Java
17](https://img.shields.io/badge/Java-17-orange?style=flat-square)
![Spring
Boot](https://img.shields.io/badge/Spring%20Boot-3.4-green?style=flat-square)
![Coverage](https://img.shields.io/badge/Coverage-%3E85%25-brightgreen?style=flat-square)
![Deploy](https://img.shields.io/badge/Deploy-Render-blue?style=flat-square)

API REST desarrollada para detectar mutantes basándose en su secuencia
de ADN.\
Proyecto realizado para la materia **Desarrollo de Software** --- UTN
FRM.

👉 **Repositorio GitHub:** https://github.com/FrankDagos/mutant-detector

------------------------------------------------------------------------

## ☁️ Demo Online (Deploy)

La aplicación está desplegada en **Render**:

👉 **Swagger UI:**\
https://mutant-detector-api-1q1n.onrender.com/swagger-ui.html

👉 **URL Base de la API:**\
`https://mutant-detector-api-1q1n.onrender.com`

> **Nota:** Render Free entra en "sleep mode". La primera petición puede
> tardar 40--60 segundos.

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

### **1. Detectar Mutante**

**POST** `/mutant`

Analiza una secuencia de ADN.

**Respuestas:** - **200 OK** → Mutante\
- **403 Forbidden** → Humano\
- **400 Bad Request** → ADN inválido

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

------------------------------------------------------------------------

### **2. Estadísticas**

**GET** `/stats`

Devuelve conteos generales y ratio. Soporta filtro opcional por fecha.

**Parámetros opcionales:** - `startDate` → YYYY-MM-DD\
- `endDate` → YYYY-MM-DD

**Ejemplo de respuesta:**

``` json
{
  "count_mutant_dna": 40,
  "count_human_dna": 100,
  "ratio": 0.4
}
```

------------------------------------------------------------------------

### **3. Gestión de Registros**

**DELETE** `/mutant/{hash}`

Elimina un registro usando su hash SHA-256.

**Respuestas:** - **200 OK** → Eliminado\
- **404 Not Found** → No existe

------------------------------------------------------------------------

### **4. Monitoreo (Health Check)**

**GET** `/health`

**Ejemplo de respuesta:**

``` json
{
  "status": "UP",
  "service": "Mutant Detector API",
  "version": "1.0.0"
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

### 3. URLs locales

-   API: http://localhost:8080\
-   Swagger UI: http://localhost:8080/swagger-ui.html\
-   H2 Console: http://localhost:8080/h2-console

------------------------------------------------------------------------

## 🧪 Testing y Cobertura

El proyecto tiene más del **85%** de cobertura.

Ejecutar tests + reporte:

``` bash
./gradlew test jacocoTestReport
```

Reporte HTML:

    build/reports/jacoco/test/html/index.html

------------------------------------------------------------------------

## 🏗️ Arquitectura y Mejoras

El proyecto utiliza una arquitectura de **6 capas**: - Controller\
- DTO\
- Service\
- Domain\
- Repository\
- Config

### ✨ Optimizaciones (Nivel 1, 2 y 3)

-   **Algoritmo O(N)** → análisis con un único recorrido.\
-   **Early Termination** → detiene cuando encuentra más de una
    secuencia.\
-   **Caching Persistente (SHA-256)** → evita reprocesar ADN ya
    analizado.\
-   **Validaciones de seguridad** → tamaño máximo para prevenir DDoS.\
-   **Logging detallado (SLF4J)**.\
-   **Filtros avanzados en estadísticas**.\
-   **Eliminación manual de registros**.

------------------------------------------------------------------------

## 🔄 Diagrama de Secuencia

``` mermaid
sequenceDiagram
    participant C as Cliente (API Consumer)
    participant Ctrl as MutantController
    participant Svc as MutantService
    participant Det as MutantDetector
    participant Repo as DnaRecordRepository
    participant DB as H2 Database

    C->>Ctrl: POST /mutant {dna}
    Ctrl->>Svc: analyzeDna(dna)

    Note over Svc: Calcula Hash SHA-256 del ADN

    Svc->>Repo: findByDnaHash(hash)
    Repo->>DB: SELECT * FROM dna_records WHERE hash = ?
    DB-->>Repo: Resultado / Null
    Repo-->>Svc: Optional<DnaRecord>

    alt ADN ya analizado (Cache Hit)
        Svc-->>Ctrl: Retorna resultado guardado
    else ADN nuevo
        Svc->>Det: isMutant(dna)
        Note over Det: Ejecuta Algoritmo O(N)
        Det-->>Svc: true / false

        Svc->>Repo: save(newRecord)
        Repo->>DB: INSERT new record

        Svc-->>Ctrl: Retorna nuevo resultado
    end

    alt isMutant == true
        Ctrl-->>C: 200 OK
    else
        Ctrl-->>C: 403 Forbidden
    end
```
