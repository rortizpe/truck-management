# Truck Management API

## Descripción del Proyecto

La API de gestión de camiones es una aplicación basada en Spring WebFlux, diseñada para manejar el registro, carga, descarga y consulta de camiones en un entorno reactivo. Este proyecto utiliza tecnologías modernas como PostgreSQL, Docker y Kafka para garantizar una arquitectura escalable y eficiente.

## Tecnologías Utilizadas

- **Java 17** con **Spring Boot** y **Spring WebFlux** para programación reactiva.
- **PostgreSQL** y **R2DBC** para la conexión reactiva a la base de datos.
- **Liquibase** para el versionamiento de la base de datos.
- **Docker** y **Docker Compose** para la contenerización completa del entorno.
- **JUnit** y **Mockito** para pruebas unitarias e integración.
- **Swagger/OpenAPI 3** para la documentación de la API.

## Configuración y Ejecución en Docker

Asegúrate de tener Docker y Docker Compose instalados. Sigue estos pasos para ejecutar la aplicación en un entorno contenerizado:

1. **Clona el repositorio** y navega a la carpeta raíz del proyecto.
2. **Construye la aplicación** ejecutando:
   ```bash
   ./gradlew clean build
   ```
3. **Inicia los contenedores** usando Docker Compose:
   ```bash
   docker-compose up --build
   ```

Esto iniciará la aplicación en `http://localhost:8085`.

## Explicación del Modelo de Datos y Lógica de Negocio

El modelo de datos de esta API está diseñado para manejar la gestión de camiones y sus cargas. Incluye las siguientes entidades principales:

- **Truck**: Representa un camión, con detalles como la placa de licencia, el modelo, el límite de capacidad y la carga actual.
- **Load**: Representa una carga asignada a un camión específico, con atributos como volumen y descripción.

### Lógica de Negocio

1. **Registro de Camiones**: Permite registrar un nuevo camión con sus detalles.
2. **Carga de Camión**: Permite añadir una carga a un camión especificado.
3. **Descarga de Camión**: Descarga la carga de un camión y genera una notificación.
4. **Consulta de Historial de Cargas**: Permite consultar el historial de cargas de un camión.
5. **Eliminación de Camión**: Elimina un camión del sistema.

## Ejecución de Pruebas Unitarias e Integración

Para ejecutar las pruebas unitarias e integración, usa el siguiente comando:

```bash
./gradlew test
```

## Documentación de la API (Swagger)

La API está documentada usando Swagger/OpenAPI. Una vez que el servicio esté en ejecución, puedes acceder a la documentación de la API en `http://localhost:8085/swagger-ui.html`.

## Pruebas en Postman

### Registrar un Camión

**Request:**
```curl
curl -X POST "http://localhost:8085/api/trucks" -H "Content-Type: application/json" -d '{
  "licensePlate": "ABC123",
  "model": "Volvo FH",
  "capacityLimit": 10000,
  "currentLoad": 0,
  "status": "AVAILABLE"
}'
```

### Listar todos los Camiones

**Request:**
```curl
curl -X GET "http://localhost:8085/api/trucks"
```

### Obtener Detalles de un Camión

**Request:**
```curl
curl -X GET "http://localhost:8085/api/trucks/{id}"
```

### Añadir Carga al Camión

**Request:**
```curl
curl -X POST "http://localhost:8085/api/trucks/{id}/load" -H "Content-Type: application/json" -d '{
  "volume": 500,
  "description": "Carga de ejemplo"
}'
```

### Descargar el Camión

**Request:**
```curl
curl -X POST "http://localhost:8085/api/trucks/{id}/unload"
```

### Consultar Historial de Cargas

**Request:**
```curl
curl -X GET "http://localhost:8085/api/trucks/{id}/loads"
```

### Eliminar un Camión

**Request:**
```curl
curl -X DELETE "http://localhost:8085/api/trucks/{id}"
```