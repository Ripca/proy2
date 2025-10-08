# Sistema Empresa

Sistema web de gestión empresarial desarrollado en Java Web (JSP/Servlets) con autenticación JWT y base de datos MySQL.

## Características

- **Autenticación JWT**: Sistema de login seguro con tokens
- **Gestión de Clientes**: CRUD completo para clientes
- **Gestión de Empleados**: CRUD completo para empleados y puestos
- **Gestión de Productos**: CRUD completo para productos y marcas
- **Gestión de Proveedores**: CRUD completo para proveedores
- **Gestión de Ventas**: Sistema de ventas con detalle
- **Gestión de Compras**: Sistema de compras con detalle
- **Dashboard**: Panel principal con accesos rápidos
- **Menú Dinámico**: Navegación jerárquica según especificaciones
- **Responsive Design**: Interfaz adaptable con Bootstrap 5

## Tecnologías Utilizadas

- **Backend**: Java 17, JSP, Servlets
- **Frontend**: HTML5, CSS3, JavaScript, Bootstrap 5, Font Awesome
- **Base de Datos**: MySQL 8.0
- **Servidor**: Apache Tomcat 10
- **IDE**: NetBeans

## Requisitos del Sistema

- Java JDK 17 o superior
- Apache Tomcat 10+ (REQUERIDO para Jakarta EE)
- MySQL 8.0 o superior
- NetBeans IDE (recomendado)

**NOTA IMPORTANTE**: Este proyecto usa Jakarta EE, NO JavaEE. Requiere Tomcat 10+ o superior.

## Instalación

### 1. Configurar Base de Datos

1. Crear la base de datos ejecutando el script:
   ```sql
   -- Ejecutar el archivo: database/sistema_empresa.sql
   ```

2. Verificar que MySQL esté ejecutándose en:
   - **Host**: localhost
   - **Puerto**: 3306
   - **Usuario**: root
   - **Contraseña**: (vacía)

3. Si necesita cambiar la configuración de BD, editar:
   ```java
   // Archivo: src/java/com/sistemaempresa/config/DatabaseConnection.java
   private static final String URL = "jdbc:mysql://localhost:3306/sistema_empresa";
   private static final String USERNAME = "root";
   private static final String PASSWORD = "";
   ```

### 2. Configurar Librerías

Agregar las siguientes librerías al proyecto (WEB-INF/lib/):

1. **MySQL Connector/J**: `mysql-connector-java-8.0.33.jar`
2. **Jakarta JSTL**: `jakarta.servlet.jsp.jstl-3.0.1.jar`
3. **Jakarta JSTL API**: `jakarta.servlet.jsp.jstl-api-3.0.0.jar`

**IMPORTANTE**: Este proyecto usa Jakarta EE 9+ (no JavaEE), compatible con:
- Tomcat 10+
- Jakarta Servlet API 5.0+
- Jakarta JSP 3.0+

### 3. Desplegar en Tomcat

1. Abrir el proyecto en NetBeans
2. Hacer clic derecho en el proyecto → "Clean and Build"
3. Hacer clic derecho en el proyecto → "Deploy"
4. O copiar el archivo WAR generado a la carpeta `webapps` de Tomcat

## Uso del Sistema

### Acceso al Sistema

1. Abrir navegador web
2. Ir a: `http://localhost:8080/SistemaEmpresa`
3. Usar las credenciales por defecto:
   - **Usuario**: `admin`
   - **Contraseña**: `123456`

### Usuarios de Prueba

El sistema incluye los siguientes usuarios de prueba:

| Usuario   | Contraseña | Rol           |
|-----------|------------|---------------|
| admin     | 123456     | Administrador |
| vendedor1 | 123456     | Vendedor      |
| cajero1   | 123456     | Cajero        |

### Funcionalidades Principales

#### 1. Dashboard
- Panel principal con resumen de datos
- Accesos rápidos a funciones principales
- Menú de navegación jerárquico

#### 2. Gestión de Clientes
- **Listar**: Ver todos los clientes registrados
- **Crear**: Agregar nuevos clientes
- **Editar**: Modificar datos de clientes existentes
- **Eliminar**: Eliminar clientes (con confirmación)
- **Buscar**: Buscar por nombre, apellido o NIT

#### 3. Gestión de Empleados
- CRUD completo de empleados
- Asignación de puestos
- Gestión de información personal y laboral

#### 4. Gestión de Productos
- CRUD completo de productos
- Gestión de marcas
- Control de precios y existencias

#### 5. Gestión de Proveedores
- CRUD completo de proveedores
- Información de contacto y datos fiscales

#### 6. Sistema de Ventas
- Registro de ventas con detalle
- Selección de clientes y empleados
- Cálculo automático de totales

#### 7. Sistema de Compras
- Registro de compras con detalle
- Selección de proveedores
- Actualización automática de inventario

## Estructura del Proyecto

```
SistemaEmpresa/
├── src/java/com/sistemaempresa/
│   ├── config/
│   │   └── DatabaseConnection.java
│   ├── dao/
│   │   ├── ClienteDAO.java
│   │   ├── EmpleadoDAO.java
│   │   ├── ProductoDAO.java
│   │   └── ...
│   ├── filters/
│   │   └── JWTAuthFilter.java
│   ├── models/
│   │   ├── Cliente.java
│   │   ├── Empleado.java
│   │   └── ...
│   ├── servlets/
│   │   ├── LoginServlet.java
│   │   ├── DashboardServlet.java
│   │   └── ...
│   └── utils/
│       └── JWTUtil.java
├── web/
│   ├── WEB-INF/
│   │   ├── views/
│   │   │   ├── dashboard.jsp
│   │   │   ├── clientes/
│   │   │   └── ...
│   │   └── web.xml
│   └── index.html
└── database/
    └── sistema_empresa.sql
```

## Seguridad

- **Autenticación JWT**: Tokens seguros para sesiones
- **Filtros de Seguridad**: Validación en todas las rutas protegidas
- **Validación de Datos**: Validación tanto en frontend como backend
- **Prevención XSS**: Escape de datos en vistas JSP

## Desarrollo

### Agregar Nuevas Funcionalidades

1. **Crear Modelo**: Agregar clase en `models/`
2. **Crear DAO**: Implementar operaciones CRUD en `dao/`
3. **Crear Servlet**: Implementar controlador en `servlets/`
4. **Crear Vistas**: Agregar JSPs en `web/WEB-INF/views/`
5. **Configurar Web.xml**: Agregar mapping del servlet

### Estructura de Base de Datos

El sistema maneja las siguientes entidades principales:
- Clientes
- Empleados
- Puestos
- Productos
- Marcas
- Proveedores
- Ventas y Ventas_detalle
- Compras y Compras_detalle
- Usuarios

## Soporte

Para soporte técnico o reportar problemas:
1. Verificar logs de Tomcat en `logs/catalina.out`
2. Verificar conexión a base de datos
3. Revisar configuración de librerías

## Licencia

Este proyecto es de uso educativo y empresarial interno.
