# Sabor Gourmet - Sistema de Autenticación

Sistema de gestión de reservas desarrollado con Spring Boot y PostgreSQL.

## 🚀 Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.7**
  - Spring Security
  - Spring Data JPA
  - Spring Web
- **PostgreSQL** (EDB Postgres)
- **Thymeleaf** (Motor de plantillas)
- **Maven** (Gestión de dependencias)
- **BCrypt** (Hash de contraseñas)

## 📋 Requisitos Previos

- JDK 21 o superior
- PostgreSQL instalado y corriendo
- Maven (incluido Maven Wrapper en el proyecto)

## ⚙️ Configuración de la Base de Datos

### 1. Crear la base de datos en PostgreSQL

Ejecuta los siguientes comandos en `psql` o `pgAdmin`:

```sql
-- Crear base de datos y usuario (ejemplo)
CREATE DATABASE saborGourmet;
CREATE USER saborGourmet WITH PASSWORD 'saborGourmet';
GRANT ALL PRIVILEGES ON DATABASE saborGourmet TO saborGourmet;
```

### 2. Configuración en `application.properties`

El archivo ya está configurado con:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/saborGourmet
spring.datasource.username=saborGourmet
spring.datasource.password=saborGourmet
spring.jpa.hibernate.ddl-auto=update
server.port=8081
```

**Nota:** Si usas el usuario `postgres` por defecto, actualiza las credenciales en `src/main/resources/application.properties`.

## 🔧 Instalación y Ejecución

### Opción 1: Usando Maven Wrapper (Recomendado)

```powershell
# Construir el proyecto
.\mvnw.cmd clean package

# Ejecutar la aplicación
.\mvnw.cmd spring-boot:run
```

### Opción 2: Usando Maven instalado globalmente

```powershell
mvn clean package
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8081**

> Nota: si otro servicio ocupa el puerto 8081, cambia `server.port` en `src/main/resources/application.properties`.

## 📱 Endpoints Principales

| Endpoint | Método | Descripción |
|----------|--------|-------------|
| `/` | GET | Página de inicio |
| `/login` | GET | Formulario de login |
| `/login` | POST | Procesar autenticación |
| `/registro` | GET | Formulario de registro |
| `/registro` | POST | Registrar nuevo usuario |
| `/logout` | POST | Cerrar sesión |

## 🔐 Seguridad

- **Autenticación**: Spring Security con usuarios almacenados en PostgreSQL
- **Hash de contraseñas**: BCrypt
- **Protección CSRF**: Habilitada
- **Roles**: Soporte para `USER` y `ADMIN`

## 👤 Uso del Sistema

### Registrar un nuevo usuario

1. Accede a: http://localhost:8081/registro
2. Completa el formulario:
   - Username
   - Password
   - Role (USER o ADMIN)
3. Click en "Registrar"

### Iniciar sesión

1. Accede a: http://localhost:8081/login
2. Ingresa tus credenciales
3. Click en "Log in"

### Cerrar sesión

Desde la página de inicio, click en "Cerrar sesión"

## 📂 Estructura del Proyecto

```
saborGourmet/
├── src/
│   ├── main/
│   │   ├── java/cl/ipss/saborGourmet/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── controllers/
│   │   │   │   ├── HomeController.java
│   │   │   │   ├── loginController.java
│   │   │   │   └── RegistroController.java
│   │   │   ├── models/
│   │   │   │   └── Usuarios.java
│   │   │   ├── repositories/
│   │   │   │   └── UsuariosRepository.java
│   │   │   ├── services/
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   └── UsuariosService.java
│   │   │   └── SaborGourmetApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── templates/
│   │           ├── home.html
│   │           ├── login.html
│   │           └── registro.html
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## 🗃️ Modelo de Datos

### Tabla `usuarios`

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | BIGSERIAL | ID único (clave primaria) |
| username | VARCHAR(255) | Nombre de usuario |
| password | VARCHAR(255) | Contraseña hasheada (BCrypt) |
| role | VARCHAR(50) | Rol del usuario (USER/ADMIN) |

La tabla se crea automáticamente gracias a `spring.jpa.hibernate.ddl-auto=update`.

## 🛠️ Solución de Problemas

### Error: "no existe la base de datos saborGourmet"
Asegúrate de crear la base de datos PostgreSQL antes de ejecutar la aplicación.

### Error: Puerto 8080 en uso
La aplicación está configurada para usar el puerto 8081. Si necesitas cambiarlo, edita `server.port` en `application.properties`.

> Nota sobre EDB / PgAdmin:
>- EDB Postgres (EnterpriseDB) incluye una interfaz web (PgAdmin) que por defecto corre en `http://localhost:8080`.
>- Ese servidor web no afecta el puerto en el que tu aplicación Spring Boot escucha (configurado en `server.port`).
>- Si necesitas detener PgAdmin/EDB temporalmente en Windows usa `services.msc` o los comandos PowerShell para detener el servicio.

### Error: Conexión a PostgreSQL rechazada
Verifica que PostgreSQL esté corriendo y que las credenciales en `application.properties` sean correctas.

## 📝 Características Implementadas

- ✅ Registro de usuarios con validación
- ✅ Login con Spring Security
- ✅ Persistencia en PostgreSQL
- ✅ Hash seguro de contraseñas (BCrypt)
- ✅ Gestión de sesiones
- ✅ Protección CSRF
- ✅ Sistema de roles (USER/ADMIN)
- ✅ Mensajes de feedback (registro exitoso, error de login, logout)


## 👨‍💻 Autores
- Loretto Herrera
- Sebastián Masferrer

Proyecto desarrollado para el curso de Desarrollo de Software Web II - IPSS.


## 📄 Licencia

Este proyecto es de uso académico. Consulta con los autores antes de usarlo con fines comerciales.
