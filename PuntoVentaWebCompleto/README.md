# PuntoVentaWebCompleto

Sistema de Punto de Venta Web desarrollado en Java que implementa exactamente la funcionalidad del proyecto C# RepoPro, utilizando la estructura y componentes del proyecto SistemaEmpresa.

## Características Principales

### Funcionalidades del Sistema Base (SistemaEmpresa)
- ✅ **Sistema de Autenticación JWT** - Login seguro con tokens
- ✅ **Dashboard Dinámico** - Panel principal con estadísticas
- ✅ **Menú Dinámico** - Navegación configurable desde base de datos
- ✅ **Carrusel de Imágenes** - Banner dinámico en dashboard
- ✅ **CRUDs Completos**:
  - Clientes
  - Empleados
  - Puestos
  - Productos
  - Marcas
  - Proveedores
  - Usuarios

### Funcionalidades Específicas de Punto de Venta (C# RepoPro)
- ✅ **Módulo de Compras** - Implementa exactamente la lógica del C# `Compra.crear()`
- ✅ **Módulo de Ventas** - Implementa exactamente la lógica del C# `Venta.crearVenta()`
- ✅ **Transacciones Master-Detail** - Compras y ventas con múltiples detalles
- ✅ **Manejo de Inventario** - Control de existencias de productos

## Tecnologías Utilizadas

- **Backend**: Java 17+, Jakarta EE 9+
- **Frontend**: JSP, Bootstrap 5, Font Awesome 6, JavaScript vanilla
- **Base de Datos**: MySQL 8.0
- **Servidor**: Apache Tomcat 10+
- **Autenticación**: JWT (sin librerías externas)
- **Arquitectura**: MVC (Model-View-Controller)

## Estructura del Proyecto

```
PuntoVentaWebCompleto/
├── database/
│   └── script_completo_puntoventa.sql    # Script completo de base de datos
├── src/java/com/puntoventa/
│   ├── config/
│   │   └── DatabaseConnection.java       # Configuración de BD
│   ├── dao/                             # Data Access Objects
│   │   ├── ClienteDAO.java
│   │   ├── EmpleadoDAO.java
│   │   ├── ProductoDAO.java
│   │   ├── CompraDAO.java              # DAO crítico - lógica C#
│   │   ├── VentaDAO.java               # DAO crítico - lógica C#
│   │   └── ...
│   ├── models/                         # Modelos de datos
│   │   ├── Compra.java                 # Basado en C# Compra.h
│   │   ├── Venta.java                  # Basado en C# Venta.h
│   │   └── ...
│   ├── servlets/                       # Controladores
│   │   ├── CompraServlet.java          # Servlet crítico
│   │   ├── VentaServlet.java           # Servlet crítico
│   │   └── ...
│   ├── utils/                          # Utilidades
│   │   ├── JWTUtil.java
│   │   └── SimpleJSONUtil.java
│   └── filters/
│       └── JWTAuthFilter.java          # Filtro de autenticación
├── web/
│   ├── WEB-INF/
│   │   ├── web.xml                     # Configuración de servlets
│   │   └── views/                      # Vistas JSP
│   └── index.html                      # Página de login
└── build.xml                          # Script de construcción Ant
```

## Instalación y Configuración

### Prerrequisitos

1. **Java Development Kit (JDK) 17 o superior**
2. **Apache Tomcat 10 o superior**
3. **MySQL 8.0 o superior**
4. **Apache Ant** (para construcción)

### Paso 1: Configuración de Base de Datos

1. Crear la base de datos:
```sql
CREATE DATABASE PuntoVentaWebCompleto;
```

2. Ejecutar el script completo:
```bash
mysql -u root -p PuntoVentaWebCompleto < database/script_completo_puntoventa.sql
```

### Paso 2: Configuración de Variables de Entorno

Configurar las siguientes variables de entorno (o modificar `DatabaseConnection.java`):

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=PuntoVentaWebCompleto
export DB_USER=root
export DB_PASSWORD=tu_password
```

### Paso 3: Construcción del Proyecto

```bash
# Compilar el proyecto
ant compile

# Crear el archivo WAR
ant war
```

### Paso 4: Despliegue en Tomcat

1. Copiar el archivo `PuntoVentaWebCompleto.war` a la carpeta `webapps` de Tomcat
2. Iniciar Tomcat
3. Acceder a: `http://localhost:8080/PuntoVentaWebCompleto`

## Credenciales por Defecto

- **Usuario**: `admin`
- **Contraseña**: `123456`

## Funcionalidades Implementadas

### Módulo de Compras (Basado en C# Compra.h)

**Características:**
- Creación de compras con múltiples detalles
- Transacciones seguras (rollback en caso de error)
- Obtención de LAST_INSERT_ID() como en C#
- Listado con información de proveedores
- Edición y eliminación de compras

**Flujo de Creación (igual que C#):**
1. INSERT en tabla `Compras`
2. Obtener ID generado automáticamente
3. INSERT múltiples en `Compras_detalle`
4. COMMIT o ROLLBACK según resultado

### Módulo de Ventas (Basado en C# Venta.h)

**Características:**
- Creación de ventas con múltiples detalles
- Campo `cantidad` como VARCHAR(45) (igual que C#)
- Transacciones seguras
- Listado con información de clientes y empleados
- Edición y eliminación de ventas

**Flujo de Creación (igual que C#):**
1. INSERT en tabla `Ventas`
2. Obtener ID generado automáticamente
3. INSERT múltiples en `Ventas_detalle`
4. COMMIT o ROLLBACK según resultado

## Estructura de Base de Datos

### Tablas Principales (del C# RepoPro)
- `Compras` - Encabezado de compras
- `Compras_detalle` - Detalles de compras
- `Ventas` - Encabezado de ventas
- `Ventas_detalle` - Detalles de ventas
- `Productos` - Catálogo de productos
- `Clientes` - Información de clientes
- `Empleados` - Información de empleados
- `Proveedores` - Información de proveedores

### Tablas del Sistema (de SistemaEmpresa)
- `usuarios` - Usuarios del sistema
- `menus` - Menús dinámicos
- `carrusel_imagenes` - Imágenes del carrusel

## API Endpoints

### Compras
- `GET /CompraServlet?action=list` - Listar compras
- `GET /CompraServlet?action=new` - Formulario nueva compra
- `POST /CompraServlet?action=create` - Crear compra
- `GET /CompraServlet?action=view&id=X` - Ver compra
- `GET /CompraServlet?action=edit&id=X` - Editar compra
- `POST /CompraServlet?action=update` - Actualizar compra
- `POST /CompraServlet?action=delete&id=X` - Eliminar compra

### Ventas
- `GET /VentaServlet?action=list` - Listar ventas
- `GET /VentaServlet?action=new` - Formulario nueva venta
- `POST /VentaServlet?action=create` - Crear venta
- `GET /VentaServlet?action=view&id=X` - Ver venta
- `GET /VentaServlet?action=edit&id=X` - Editar venta
- `POST /VentaServlet?action=update` - Actualizar venta
- `POST /VentaServlet?action=delete&id=X` - Eliminar venta

## Diferencias Clave con el Proyecto C#

### Similitudes Mantenidas
- ✅ Misma estructura de tablas
- ✅ Misma lógica de transacciones
- ✅ Mismo flujo master-detail
- ✅ Campo `cantidad` como VARCHAR en ventas
- ✅ Uso de LAST_INSERT_ID()

### Adaptaciones a Java Web
- 🔄 PreparedStatements en lugar de concatenación de strings
- 🔄 try-with-resources para manejo de conexiones
- 🔄 Servlets en lugar de formularios de escritorio
- 🔄 JSON responses para AJAX
- 🔄 JSP para vistas web

## Desarrollo y Mantenimiento

### Agregar Nuevos Módulos
1. Crear modelo en `src/java/com/puntoventa/models/`
2. Crear DAO en `src/java/com/puntoventa/dao/`
3. Crear servlet en `src/java/com/puntoventa/servlets/`
4. Agregar mapping en `web.xml`
5. Crear vistas JSP en `web/WEB-INF/views/`

### Debugging
- Logs en consola de Tomcat
- Verificar conexión de BD con `DatabaseConnection.testConnection()`
- Revisar filtro JWT en `JWTAuthFilter.java`

## Soporte

Para problemas o consultas sobre la implementación, revisar:
1. Logs de Tomcat
2. Logs de MySQL
3. Consola del navegador (errores JavaScript)
4. Verificar configuración de variables de entorno

---

**Nota**: Este proyecto implementa exactamente la funcionalidad del sistema C# RepoPro pero adaptado para web, manteniendo la misma lógica de negocio y estructura de datos.
