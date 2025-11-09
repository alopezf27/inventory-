# inventory-
🧱 README - Backend (Spring Boot + PostgreSQL)
# 🧩 Inventory Backend

Backend del sistema **Inventory App**, desarrollado en **Spring Boot 3 + Java 21 + PostgreSQL + Docker Compose**.  
Administra productos, proveedores, movimientos de entrada/salida, existencias por bodega y control de inventario.

---

## 🚀 Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3.3**
  - Spring Data JPA
  - Spring Web
  - Flyway
- **PostgreSQL 16**
- **Docker & Docker Compose**
- **Maven 3.9**
- **HikariCP** para conexión de base de datos
- **Lombok**

---

## 🗂️ Estructura del proyecto



backend/
├── src/
│ ├── main/
│ │ ├── java/com/acme/inventory/
│ │ │ ├── model/ # Entidades (Company, Product, Movement, etc.)
│ │ │ ├── repository/ # Repositorios JPA
│ │ │ ├── service/ # Lógica de negocio
│ │ │ ├── controller/ # Controladores REST
│ │ │ └── dto/ # Clases DTO para API
│ │ └── resources/
│ │ ├── db/migration/ # Scripts Flyway
│ │ ├── application.yml # Configuración
│ │ └── logback.xml # Logging
├── pom.xml
├── Dockerfile
├── docker-compose.yml
└── README.md


---

## ⚙️ Configuración de entorno

Variables principales del entorno:

```bash
DB_HOST=db
DB_PORT=5432
DB_NAME=inventory
DB_USER=postgres
DB_PASS=postgres
SPRING_PROFILES_ACTIVE=prod


Archivo application.yml:

spring:
  datasource:
    url: jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
    username: ${DB_USER}
    password: ${DB_PASS}
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration
server:
  port: 8080

🐳 Ejecución con Docker Compose
# Apagar contenedores existentes (si los hay)
docker compose down -v

# Construir y levantar la app
docker compose up --build


Esto levantará:

inventory-db: PostgreSQL 16

inventory-backend: Spring Boot app (puerto 8080)

🧠 Endpoints principales
Método	Endpoint	Descripción
GET	/api/products	Listar productos
GET	/api/products/{id}	Obtener producto
POST	/api/products	Crear producto
GET	/api/inventory	Ver existencias
GET	/api/movements	Historial de movimientos
POST	/api/movements	Registrar entrada/salida/transferencia
🧾 Scripts SQL de ejemplo

Para inicializar datos de prueba (proveedores, productos, movimientos), revisa el archivo:

/src/main/resources/db/migration/V2__sample_data.sql

🧰 Comandos útiles
# Compilar
mvn clean package -DskipTests

# Ejecutar localmente
mvn spring-boot:run

# Ver logs
docker logs -f inventory-backend
