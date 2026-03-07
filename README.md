# Demo Backend Application

Esta es una aplicación de backend desarrollada con Spring Boot que gestiona franquicias, sucursales y productos. Proporciona una API REST para operaciones CRUD relacionadas con el modelo de negocio de franquicias.

## Descripción

La aplicación permite:
- Crear y gestionar franquicias
- Agregar sucursales a franquicias
- Gestionar productos en sucursales
- Actualizar nombres de franquicias y sucursales
- Obtener información sobre productos con mayor stock

## Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 4.0.3**
- **Spring Data JPA**
- **MySQL** (base de datos)
- **Maven** (gestión de dependencias)
- **Docker** (contenedorización)
- **Lombok** (reducción de código boilerplate)

## Prerrequisitos

Antes de ejecutar la aplicación, asegúrate de tener instalados los siguientes componentes:

- **Java 21** o superior
- **Maven 3.6+**
- **Docker** y **Docker Compose** (para ejecutar la base de datos localmente)
- **Git** (para clonar el repositorio)

## Configuración del Entorno Local

### 1. Clonar el Repositorio

```bash
git clone <url-del-repositorio>
cd demo
```

### 2. Configurar la Base de Datos

La aplicación está configurada para usar MySQL. Para ejecutar una instancia local de MySQL usando Docker:

```bash
docker run --name mysql-demo -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=prueba_db -e MYSQL_USER=myuser -e MYSQL_PASSWORD=mypassword -p 3306:3306 -d mysql:8.0
```

O usando Docker Compose.

### 3. Configurar las Propiedades de la Aplicación

Edita el archivo `src/main/resources/application.properties` para configurar la conexión a la base de datos local:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prueba_db
spring.datasource.username=myuser
spring.datasource.password=mypassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

Si deseas usar la configuración de AWS RDS (para despliegue en la nube), mantén la configuración original.

## Ejecutar la Aplicación Localmente

### Opción 1: Usando Maven

1. Compila y ejecuta la aplicación:

```bash
mvn clean install
mvn spring-boot:run
```

2. La aplicación estará disponible en `http://localhost:8080`

### Opción 2: Usando Docker

1. Construye la imagen Docker:

```bash
docker build -t demo .
```

2. Ejecuta el contenedor:

```bash
docker run -p 8080:8080 --link mysql-demo:mysql demo
```

Asegúrate de que la base de datos esté ejecutándose y accesible.

## Despliegue

### Despliegue Local con Docker Compose

Para un despliegue completo local, actualiza el `compose.yaml` para usar MySQL en lugar de PostgreSQL:

```yaml
services:
  mysql:
    image: 'mysql:8.0'
    environment:
      - 'MYSQL_DATABASE=prueba_db'
      - 'MYSQL_ROOT_PASSWORD=root'
      - 'MYSQL_USER=myuser'
      - 'MYSQL_PASSWORD=mypassword'
    ports:
      - '3306:3306'
  app:
    build: .
    ports:
      - '8080:8080'
    depends_on:
      - mysql
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/prueba_db
      - SPRING_DATASOURCE_USERNAME=myuser
      - SPRING_DATASOURCE_PASSWORD=mypassword
```

Luego ejecuta:

```bash
docker-compose up --build
```

### Despliegue en Servidor Remoto

1. Construye el JAR:

```bash
mvn clean package
```

2. Construye la imagen Docker:

```bash
docker build -t demo .
```

3. Sube la imagen a un registro de contenedores (ej. Docker Hub, AWS ECR).

4. En el servidor, ejecuta la aplicación usando Docker o despliega el JAR directamente.

Ejemplo de despliegue en un servidor Ubuntu (basado en el historial de comandos):

```bash
# Subir archivos al servidor
scp -i key-ssh.pem -r demo ubuntu@3.22.186.55:/home/ubuntu

# En el servidor
cd demo
docker build -t demo .
docker run -d -p 8080:8080 demo
```

## API Endpoints

La API está disponible bajo el prefijo `/api`. Endpoints principales:

- `POST /api/franquicias` - Crear franquicia
- `POST /api/franquicias/{id}/sucursales` - Agregar sucursal
- `POST /api/sucursales/{id}/productos` - Agregar producto
- `PUT /api/franquicias/{id}` - Actualizar nombre de franquicia
- `PUT /api/sucursales/{id}` - Actualizar nombre de sucursal
- `PUT /api/productos/{id}` - Actualizar nombre y stock de producto
- `GET /api/franquicias` - Obtener todas las franquicias
- `GET /api/franquicias/{id}/productos-mayor-stock` - Obtener productos con mayor stock por sucursal

Para documentación completa de la API, consulta el código fuente o usa herramientas como Swagger si está configurado.

## Pruebas

Ejecuta las pruebas con Maven:

```bash
mvn test
```

## Configuración Adicional

- **Puerto**: La aplicación corre en el puerto 8080 por defecto.
- **Base de Datos**: Configurada para MySQL con Hibernate DDL auto-update.
- **Logs**: Usa SLF4J con Logback para logging.

## Contribución

1. Crea una rama para tu feature
2. Realiza tus cambios
3. Ejecuta las pruebas
4. Envía un pull request

## Licencia

Este proyecto es de demostración y no tiene licencia específica.
