# Microservicio de Usuarios (ms_user)

Microservicio para la gestión de usuarios y autenticación del sistema de Gestión de Gastos.

## 🚀 Características

- ✅ **Registro de usuarios** con email y contraseña
- ✅ **Login con JWT** (JSON Web Tokens)
- ✅ **Autenticación con Google OAuth2**
- ✅ **Arquitectura Hexagonal** (Ports & Adapters)
- ✅ **Seguridad con Spring Security**
- ✅ **Gestión de usuarios** (CRUD completo)
- ✅ **Roles y autorización** (USER, ADMIN)
- ✅ **Soft delete** para usuarios
- ✅ **Docker ready** con Dockerfile incluido

## 🏗️ Arquitectura

Sigue el mismo patrón hexagonal que los otros microservicios:

```
src/main/java/com/example/ms_user/
├── domain/                     # Capa de dominio
│   ├── model/                  # Entidades y enums
│   │   ├── User.java
│   │   ├── AuthProvider.java (LOCAL, GOOGLE)
│   │   ├── Role.java (USER, ADMIN)
│   │   └── dto/                # DTOs
│   └── ports/                  # Interfaces
│       ├── UserRepositoryPort.java
│       ├── AuthServicePort.java
│       └── UserServicePort.java
├── application/                # Capa de aplicación
│   └── usecases/               # Lógica de negocio
│       ├── AuthUseCase.java
│       └── UserUseCase.java
└── infrastructure/             # Capa de infraestructura
    ├── adapters/
    │   ├── input/rest/         # Controladores REST
    │   │   ├── AuthController.java
    │   │   ├── UserController.java
    │   │   └── OAuth2Controller.java
    │   └── output/persistence/ # Repositorios JPA
    │       ├── UserRepository.java
    │       └── UserRepositoryAdapter.java
    ├── security/               # Configuración de seguridad
    │   ├── JwtService.java
    │   ├── JwtAuthenticationFilter.java
    │   ├── CustomUserDetails.java
    │   └── CustomUserDetailsService.java
    └── configuration/          # Beans y configuración
        ├── SecurityConfiguration.java
        └── BeanConfiguration.java
```

## 📊 Modelo de Datos

### Tabla: `users`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| user_id | BIGSERIAL | ID único del usuario |
| email | VARCHAR(100) | Email (único) |
| password | VARCHAR(255) | Contraseña encriptada (BCrypt) |
| first_name | VARCHAR(50) | Nombre |
| last_name | VARCHAR(50) | Apellido |
| provider | VARCHAR(20) | Proveedor de autenticación (LOCAL/GOOGLE) |
| provider_id | VARCHAR(100) | ID del proveedor OAuth2 |
| role | VARCHAR(20) | Rol del usuario (USER/ADMIN) |
| active | BOOLEAN | Estado del usuario (soft delete) |
| email_verified | BOOLEAN | Email verificado |
| profile_picture_url | VARCHAR(500) | URL de foto de perfil |
| created_at | TIMESTAMP | Fecha de creación |
| updated_at | TIMESTAMP | Fecha de actualización |

## 🔐 Endpoints de Autenticación

### Registro de Usuario
```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "user": {
    "userId": 1,
    "email": "usuario@example.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "fullName": "Juan Pérez",
    "provider": "LOCAL",
    "role": "USER",
    "emailVerified": false,
    "createdAt": "2025-11-10T10:30:00",
    "updatedAt": "2025-11-10T10:30:00"
  }
}
```

### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}
```

### Validar Token
```http
POST /api/v1/auth/validate
Authorization: Bearer {token}
```

### Google OAuth2
```http
GET /oauth2/authorization/google
```
Redirige al flujo de autenticación de Google.

## 👤 Endpoints de Usuarios

### Obtener Usuario Actual
```http
GET /api/v1/users/me
Authorization: Bearer {token}
```

### Obtener Usuario por ID
```http
GET /api/v1/users/{userId}
Authorization: Bearer {token}
```
*Requiere: Ser el mismo usuario o tener rol ADMIN*

### Obtener Todos los Usuarios
```http
GET /api/v1/users
Authorization: Bearer {token}
```
*Requiere: Rol ADMIN*

### Actualizar Usuario
```http
PUT /api/v1/users/{userId}
Authorization: Bearer {token}
Content-Type: application/json

{
  "firstName": "Juan Carlos",
  "lastName": "Pérez García",
  "profilePictureUrl": "https://..."
}
```

### Eliminar Usuario (Soft Delete)
```http
DELETE /api/v1/users/{userId}
Authorization: Bearer {token}
```

### Activar/Desactivar Usuario
```http
PUT /api/v1/users/{userId}/activate
PUT /api/v1/users/{userId}/deactivate
Authorization: Bearer {token}
```
*Requiere: Rol ADMIN*

## ⚙️ Configuración

### Variables de Entorno

Crea un archivo `.env` basado en `.env.example`:

```bash
# Perfil de Spring Boot
SPRING_PROFILES_ACTIVE=dev

# Puerto del servidor
SERVER_PORT=8110

# Base de datos
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/gestion_gastos_dev
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# JWT
JWT_SECRET=mySecretKeyForJWTTokenGenerationAndValidation12345678901234567890
JWT_EXPIRATION=86400000

# Google OAuth2
GOOGLE_CLIENT_ID=tu-google-client-id
GOOGLE_CLIENT_SECRET=tu-google-client-secret
```

### Configurar Google OAuth2

1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Crea un nuevo proyecto o selecciona uno existente
3. Habilita la API de Google+
4. Crea credenciales OAuth 2.0
5. Configura las URI de redirección:
   - `http://localhost:8110/login/oauth2/code/google`
6. Copia el Client ID y Client Secret a las variables de entorno

## 🚀 Ejecución

### Desarrollo Local

```bash
cd ms_user

# Compilar
./mvnw clean compile

# Ejecutar
./mvnw spring-boot:run

# Con perfil específico
./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=local
```

### Docker

```bash
# Construir imagen
docker build -t ms_user:latest .

# Ejecutar contenedor
docker run -p 8110:8110 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/gestion_gastos_dev \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  -e JWT_SECRET=mySecretKey \
  ms_user:latest
```

## 🔧 Integración con Otros Microservicios

Los otros microservicios deben validar el JWT enviado por los usuarios. Para esto:

1. **Agregar dependencia de JJWT** en los otros servicios
2. **Crear un filtro JWT** similar al de este servicio
3. **Validar el token** en cada petición
4. **Extraer el userId** del token para asociar las operaciones

### Ejemplo de Validación en ms_expense

```java
// Extraer userId del token JWT
String token = request.getHeader("Authorization").substring(7);
Long userId = jwtService.extractUserId(token);

// Usar userId en las operaciones
Expense expense = Expense.builder()
    .userId(userId)  // ID del usuario autenticado
    .amount(request.getAmount())
    .expenseCategoryId(request.getCategoryId())
    .build();
```

## 🧪 Testing

### Health Check
```bash
curl http://localhost:8110/actuator/health
```

### Registro de Usuario
```bash
curl -X POST http://localhost:8110/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Login
```bash
curl -X POST http://localhost:8110/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## 📦 Dependencias Principales

- **Spring Boot 3.5.7**: Framework base
- **Spring Security**: Seguridad y autenticación
- **Spring OAuth2 Client**: Integración con Google
- **JJWT 0.12.3**: Generación y validación de JWT
- **PostgreSQL**: Base de datos
- **Lombok**: Reducción de boilerplate
- **Hibernate/JPA**: ORM

## 🔒 Seguridad

- **Contraseñas**: Encriptadas con BCrypt
- **Tokens JWT**: Firmados con HS512
- **Expiración**: 24 horas por defecto
- **CORS**: Habilitado (configurar según necesidad)
- **Endpoints públicos**: `/api/v1/auth/**`, `/oauth2/**`, `/actuator/**`
- **Endpoints protegidos**: Todo lo demás requiere JWT válido

## 📝 Notas Importantes

1. **JWT Secret**: Cambia el secret en producción por uno más seguro
2. **Google OAuth2**: Configura las credenciales correctas
3. **Base de Datos**: El schema se crea automáticamente con `spring.jpa.hibernate.ddl-auto=update`
4. **Puerto**: Por defecto usa 8110 (configurable)
5. **Soft Delete**: Los usuarios no se eliminan físicamente, solo se desactivan

## 🔗 Relacionado

- **ms_categories** (Puerto 8090): Gestión de categorías
- **ms_income** (Puerto 8100): Gestión de ingresos
- **ms_expense** (Puerto 8080): Gestión de gastos

## 📄 Licencia

Este proyecto es parte del sistema de Gestión de Gastos - Universidad Corhuila
