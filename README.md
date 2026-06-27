# TrazaAlimentos Backend

Sistema de Trazabilidad de Alimentos Orgánicos con Blockchain Simulado

## Descripción

TrazaAlimentos es una aplicación backend desarrollada con Spring Boot que permite registrar y seguir la trazabilidad de alimentos orgánicos desde su producción hasta el consumidor final. Utiliza códigos QR y simula una cadena de bloques con SHA-256 para garantizar la integridad de los datos.

## Características

- ✅ Autenticación JWT
- ✅ Gestión de usuarios con 4 roles (Productor, Distribuidor, Comerciante, Admin)
- ✅ Creación y seguimiento de productos y lotes
- ✅ Blockchain simulado con SHA-256
- ✅ Códigos QR para acceso público
- ✅ API REST completa
- ✅ Base de datos MySQL

## Requisitos

- Java 21 o superior
- Maven 3.8+
- MySQL 8.0+
- Node.js 16+ (para frontend, si aplica)

## Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/fabianavilca/TrazaAlimentos.git
cd TrazaAlimentos
```

### 2. Crear la base de datos

```bash
mysql -u root -p < src/main/resources/schema.sql
```

### 3. Configurar la aplicación

Edita `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/trazaalimentos
spring.datasource.username=root
spring.datasource.password=tu_contraseña
jwt.secret=tu_clave_secreta_minimo_32_caracteres
```

### 4. Compilar y ejecutar

```bash
mvn clean install
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080/api`

## Endpoints principales

### Autenticación

- `POST /api/auth/registro` - Registrar nuevo usuario
- `POST /api/auth/login` - Iniciar sesión

### Productos

- `POST /api/productos` - Crear producto
- `GET /api/productos` - Obtener todos los productos
- `GET /api/productos/{id}` - Obtener producto por ID

### Lotes

- `POST /api/lotes` - Crear lote
- `GET /api/lotes` - Obtener todos los lotes
- `GET /api/lotes/{id}` - Obtener lote por ID
- `GET /api/lotes/{codigoLote}` - Obtener por código
- `GET /api/lotes/{loteId}/cadena` - Ver cadena de bloques
- `GET /api/lotes/{loteId}/verificar` - Verificar integridad

### Distribuidor

- `POST /api/distribuidor/{distribuidorId}/recibir/{loteId}` - Recibir lote

### Comerciante

- `POST /api/comerciante/{comercianteId}/recibir/{loteId}` - Recibir lote

### Consumidor

- `GET /api/consumidor/lote/{codigoLote}` - Consultar trazabilidad completa

## Estructura del Proyecto

```
src/main/java/com/trazaalimentos/
├── config/          # Configuraciones (Security, JWT, CORS)
├── controller/      # Controladores REST
├── entity/          # Entidades JPA
├── repository/      # Interfaces JPA
├── service/         # Lógica de negocio
├── dto/             # Data Transfer Objects
└── util/            # Utilidades (JWT, SHA256, QR)
```

## Tecnologías Utilizadas

- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- MySQL 8.0
- JWT (jjwt)
- ZXing (Generación QR)
- Lombok
- Maven

## Contribuidores

- Fabiana Vilca

## Licencia

MIT
