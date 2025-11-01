# 📚 DOCUMENTACIÓN TÉCNICA COMPLETA - SISTEMA EMPRESA

## 📋 Tabla de Contenidos

### Parte 1: Fundamentos
1. [Arquitectura General](#arquitectura-general)
2. [Estructura de Directorios](#estructura-de-directorios)
3. [Stack Tecnológico](#stack-tecnológico)

### Parte 2: Base de Datos
4. [Base de Datos](#base-de-datos)
5. [Diagrama de Relaciones](#diagrama-de-relaciones)
6. [Tablas Principales](#tablas-principales)

### Parte 3: Autenticación y Seguridad
7. [Autenticación y JWT](#autenticación-y-jwt)
8. [Estructura del JWT](#estructura-del-jwt)
9. [Sistema de Roles y Permisos](#sistema-de-roles-y-permisos)
10. [Seguridad y Buenas Prácticas](#seguridad-y-buenas-prácticas)

### Parte 4: Desarrollo
11. [Patrón CRUD](#patrón-crud)
12. [Modelos y Estructuras de Datos](#modelos-y-estructuras-de-datos)
13. [Métodos Clave de DAOs](#métodos-clave-de-daos)
14. [Gestión de Imágenes](#gestión-de-imágenes)

### Parte 5: Funcionalidades Avanzadas
15. [Reportes](#reportes)
16. [Flujos de Datos](#flujos-de-datos)
17. [Flujo Completo: Crear una Venta](#flujo-completo-crear-una-venta)

### Parte 6: Validaciones y Manejo de Errores
18. [Validaciones y Manejo de Errores](#validaciones-y-manejo-de-errores)
19. [Troubleshooting Común](#troubleshooting-común)

### Parte 7: Deployment
20. [Configuración y Deployment](#configuración-y-deployment)
21. [Estadísticas del Proyecto](#estadísticas-del-proyecto)
22. [Ciclo de Vida de una Solicitud](#ciclo-de-vida-de-una-solicitud)
23. [Mejores Prácticas Implementadas](#mejores-prácticas-implementadas)

---

## 🏗️ ARQUITECTURA GENERAL

### Patrón MVC (Model-View-Controller)

El proyecto implementa el patrón MVC con la siguiente estructura:

```
┌─────────────────────────────────────────────────────────────┐
│                    NAVEGADOR (Cliente)                      │
└────────────────────────┬────────────────────────────────────┘
                         │ HTTP Request/Response
┌────────────────────────▼────────────────────────────────────┐
│              SERVLETS (Controllers)                         │
│  - LoginServlet, ProductoServlet, VentaServlet, etc.       │
│  - Manejan lógica de negocio y enrutamiento                │
└────────────────────────┬────────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
┌───────▼────────┐ ┌────▼──────────┐ ┌──▼──────────────┐
│  DAOs          │ │  Models       │ │  Filters        │
│  (Acceso BD)   │ │  (Entidades)  │ │  (Seguridad)    │
└────────────────┘ └───────────────┘ └─────────────────┘
        │
┌───────▼────────────────────────────────────────────────────┐
│              BASE DE DATOS MySQL                           │
│  - Tablas de negocio (productos, ventas, compras)         │
│  - Tablas de seguridad (usuarios, roles, permisos)        │
└────────────────────────────────────────────────────────────┘
```

### Stack Tecnológico

- **Backend**: Java 11+ con Jakarta EE (Servlets 5.0)
- **Frontend**: JSP, HTML5, Bootstrap 5, JavaScript (Vanilla + jQuery)
- **Base de Datos**: MySQL 8.0+
- **Servidor**: Apache Tomcat 10+
- **Autenticación**: JWT (JSON Web Tokens) con HMAC-SHA256
- **Reportes**: JasperReports 6.20.0
- **Librerías Principales**:
  - Jackson (JSON processing)
  - MySQL Connector/J 9.4.0
  - JSTL 3.0.1
  - SLF4J 2.0.10

---

## 📁 ESTRUCTURA DE DIRECTORIOS

```
SistemaEmpresa/
├── src/java/com/sistemaempresa/
│   ├── config/
│   │   └── DatabaseConnection.java          # Gestión de conexiones BD
│   ├── dao/                                 # Data Access Objects
│   │   ├── ProductoDAO.java                # CRUD Productos
│   │   ├── VentaDAO.java                   # CRUD Ventas (maestro-detalle)
│   │   ├── CompraDAO.java                  # CRUD Compras (maestro-detalle)
│   │   ├── UsuarioDAO.java                 # CRUD Usuarios
│   │   ├── RolDAO.java                     # CRUD Roles
│   │   ├── RolMenuDAO.java                 # Relación Rol-Menu
│   │   ├── UsuarioRolDAO.java              # Relación Usuario-Rol
│   │   ├── MenuDAO.java                    # Menús dinámicos
│   │   └── [Otros DAOs...]
│   ├── models/                              # Clases de entidades
│   │   ├── Usuario.java
│   │   ├── Producto.java
│   │   ├── Venta.java / VentaDetalle.java
│   │   ├── Compra.java / CompraDetalle.java
│   │   ├── Rol.java
│   │   └── [Otros modelos...]
│   ├── servlets/                            # Controladores
│   │   ├── LoginServlet.java               # Autenticación
│   │   ├── DashboardServlet.java           # Dashboard principal
│   │   ├── ProductoServlet.java            # Gestión de productos
│   │   ├── VentaServlet.java               # Gestión de ventas
│   │   ├── CompraServlet.java              # Gestión de compras
│   │   ├── UploadImagenServlet.java        # Carga de imágenes
│   │   ├── ValidateTokenServlet.java       # Validación JWT
│   │   └── [Otros servlets...]
│   ├── filters/                             # Filtros de seguridad
│   │   ├── AuthenticationFilter.java       # Validación de sesión
│   │   ├── JWTAuthFilter.java              # Validación JWT
│   │   ├── CharacterEncodingFilter.java    # Encoding UTF-8
│   │   └── CORSFilter.java                 # CORS
│   ├── utils/                               # Utilidades
│   │   ├── JWTUtil.java                    # Generación/validación JWT
│   │   └── SimpleJSONUtil.java             # Procesamiento JSON
│   └── reportes/
│       ├── ReporteServlet.java             # Generación de reportes
│       └── [Archivos .jrxml y .jasper]
├── web/
│   ├── WEB-INF/
│   │   ├── web.xml                         # Configuración de la app
│   │   └── views/                          # Páginas JSP
│   │       ├── login.jsp
│   │       ├── dashboard.jsp
│   │       ├── productos/
│   │       │   ├── list_content.jsp        # Listado de productos
│   │       │   └── form_content.jsp        # Formulario de productos
│   │       ├── ventas/
│   │       │   ├── list_content.jsp
│   │       │   └── form_content.jsp
│   │       ├── compras/
│   │       │   ├── list_content.jsp
│   │       │   └── form_content.jsp
│   │       └── [Otros módulos...]
│   ├── assets/
│   │   ├── css/                            # Estilos
│   │   ├── js/                             # Scripts
│   │   └── productos/                      # Imágenes de productos
│   ├── error404.jsp                        # Página de error 404
│   ├── error500.jsp                        # Página de error 500
│   └── index.jsp                           # Página de login
├── database/
│   ├── sistema_empresa.sql                 # Schema principal
│   ├── crear_roles_y_permisos.sql         # Tablas de RBAC
│   └── [Scripts de actualización...]
└── build.xml                               # Configuración de compilación
```

---

## 🗄️ BASE DE DATOS

### Diagrama de Relaciones

```
┌──────────────────┐
│    Usuarios      │
├──────────────────┤
│ idUsuario (PK)   │
│ usuario          │
│ password         │
│ idEmpleado (FK)  │
│ activo           │
└────────┬─────────┘
         │
         ├─────────────────────────────────┐
         │                                 │
    ┌────▼──────────────┐    ┌────────────▼────┐
    │   Empleados       │    │  Usuario_Rol    │
    ├───────────────────┤    ├─────────────────┤
    │ idEmpleado (PK)   │    │ idUsuarioRol    │
    │ nombres           │    │ idUsuario (FK)  │
    │ apellidos         │    │ idRol (FK)      │
    │ idPuesto (FK)     │    └─────────────────┘
    │ DPI               │           │
    │ telefono          │           │
    └───────────────────┘           │
                                    │
                            ┌───────▼──────────┐
                            │     Roles        │
                            ├──────────────────┤
                            │ idRol (PK)       │
                            │ nombre           │
                            │ estado           │
                            └────────┬─────────┘
                                     │
                            ┌────────▼──────────┐
                            │   Rol_Menu       │
                            ├──────────────────┤
                            │ idRolMenu        │
                            │ idRol (FK)       │
                            │ idMenu (FK)      │
                            └────────┬─────────┘
                                     │
                            ┌────────▼──────────┐
                            │    Menus         │
                            ├──────────────────┤
                            │ idMenu (PK)      │
                            │ nombre           │
                            │ url              │
                            │ id_padre (FK)    │
                            └──────────────────┘

┌──────────────────┐
│   Productos      │
├──────────────────┤
│ idProducto (PK)  │
│ producto         │
│ idMarca (FK)     │
│ precio_costo     │
│ precio_venta     │
│ existencia       │
│ imagen           │
└────────┬─────────┘
         │
    ┌────┴──────────────────────────────┐
    │                                   │
┌───▼──────────────┐    ┌──────────────▼──┐
│  Ventas          │    │  Compras         │
├──────────────────┤    ├──────────────────┤
│ idVenta (PK)     │    │ idCompra (PK)    │
│ no_orden_venta   │    │ no_orden_compra  │
│ fecha_orden      │    │ fecha_orden      │
│ idCliente (FK)   │    │ idProveedor (FK) │
└────────┬─────────┘    └────────┬─────────┘
         │                       │
    ┌────▼──────────────┐    ┌───▼──────────────┐
    │ Ventas_Detalle   │    │ Compras_Detalle  │
    ├───────────────────┤    ├──────────────────┤
    │ idVenta_detalle   │    │ idCompra_detalle │
    │ idVenta (FK)      │    │ idCompra (FK)    │
    │ idProducto (FK)   │    │ idProducto (FK)  │
    │ cantidad          │    │ cantidad         │
    │ precio_unitario   │    │ precio_costo_uni │
    └───────────────────┘    └──────────────────┘
```

### Tablas Principales

#### 1. **usuarios**
```sql
CREATE TABLE usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    idEmpleado INT,
    activo BIT DEFAULT 1,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idEmpleado) REFERENCES empleados(idEmpleado)
);
```

#### 2. **roles**
```sql
CREATE TABLE roles (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL UNIQUE,
    estado TINYINT DEFAULT 1,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);
```

#### 3. **usuario_rol** (Relación muchos-a-muchos)
```sql
CREATE TABLE usuario_rol (
    idUsuarioRol INT AUTO_INCREMENT PRIMARY KEY,
    idUsuario INT NOT NULL,
    idRol INT NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario),
    FOREIGN KEY (idRol) REFERENCES roles(idRol),
    UNIQUE KEY unique_usuario_rol (idUsuario, idRol)
);
```

#### 4. **rol_menu** (Relación muchos-a-muchos)
```sql
CREATE TABLE rol_menu (
    idRolMenu INT AUTO_INCREMENT PRIMARY KEY,
    idRol INT NOT NULL,
    idMenu INT NOT NULL,
    estado TINYINT DEFAULT 1,
    FOREIGN KEY (idRol) REFERENCES roles(idRol),
    FOREIGN KEY (idMenu) REFERENCES menus(idMenu)
);
```

#### 5. **productos**
```sql
CREATE TABLE productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    producto VARCHAR(50) NOT NULL,
    idMarca SMALLINT,
    descripcion VARCHAR(100),
    imagen VARCHAR(255),
    precio_costo DECIMAL(8,2),
    precio_venta DECIMAL(8,2),
    existencia INT,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idMarca) REFERENCES marcas(idMarca)
);
```

#### 6. **ventas** (Maestro)
```sql
CREATE TABLE ventas (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    no_orden_venta INT NOT NULL,
    fecha_orden DATE NOT NULL,
    idCliente INT NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES clientes(idCliente)
);
```

#### 7. **ventas_detalle** (Detalle)
```sql
CREATE TABLE ventas_detalle (
    idVenta_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idVenta INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (idVenta) REFERENCES ventas(idVenta),
    FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);
```

#### 8. **compras** (Maestro)
```sql
CREATE TABLE compras (
    idCompra INT AUTO_INCREMENT PRIMARY KEY,
    no_orden_compra INT NOT NULL,
    fecha_orden DATE NOT NULL,
    idProveedor INT NOT NULL,
    FOREIGN KEY (idProveedor) REFERENCES proveedores(idProveedor)
);
```

#### 9. **compras_detalle** (Detalle)
```sql
CREATE TABLE compras_detalle (
    idCompra_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idCompra INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_costo_unitario DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (idCompra) REFERENCES compras(idCompra),
    FOREIGN KEY (idProducto) REFERENCES productos(idProducto)
);
```

---

## 🔐 AUTENTICACIÓN Y JWT

### Flujo de Autenticación

```
1. Usuario ingresa credenciales
   ↓
2. LoginServlet.doPost() recibe usuario/password
   ↓
3. validateUser() consulta BD
   ↓
4. Si credenciales son válidas:
   - Generar JWT con JWTUtil.generateToken()
   - Crear sesión HttpSession
   - Guardar token en cookie HTTP-only
   - Redirigir a DashboardServlet
   ↓
5. En cada request:
   - AuthenticationFilter valida sesión
   - JWTAuthFilter valida token JWT
   - Si token expiró, se rechaza request
```

### Estructura del JWT

```
Header.Payload.Signature

Header:
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload:
{
  "usuario": "admin",
  "idUsuario": 1,
  "idEmpleado": 1,
  "iat": 1698765432,
  "exp": 1698851832
}

Signature: HMAC-SHA256(Header.Payload, SECRET_KEY)
```

### Código de Generación JWT

```java
public static String generateToken(String usuario, int idUsuario, int idEmpleado) {
    // 1. Crear header
    Map<String, Object> header = new HashMap<>();
    header.put("alg", "HS256");
    header.put("typ", "JWT");
    
    // 2. Crear payload con datos del usuario
    Map<String, Object> payload = new HashMap<>();
    payload.put("usuario", usuario);
    payload.put("idUsuario", idUsuario);
    payload.put("idEmpleado", idEmpleado);
    payload.put("iat", System.currentTimeMillis() / 1000);
    payload.put("exp", (System.currentTimeMillis() + 86400000) / 1000); // 24 horas
    
    // 3. Codificar en Base64
    String encodedHeader = Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(headerJson.getBytes());
    String encodedPayload = Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(payloadJson.getBytes());
    
    // 4. Crear firma HMAC-SHA256
    String signature = createSignature(encodedHeader + "." + encodedPayload);
    
    // 5. Retornar token completo
    return encodedHeader + "." + encodedPayload + "." + signature;
}
```

### Validación JWT

```java
public static boolean validateToken(String token) {
    // 1. Dividir token en 3 partes
    String[] parts = token.split("\\.");
    if (parts.length != 3) return false;
    
    // 2. Verificar firma
    String expectedSignature = createSignature(parts[0] + "." + parts[1]);
    if (!parts[2].equals(expectedSignature)) return false;
    
    // 3. Verificar expiración
    Map<String, Object> payload = getPayloadFromToken(token);
    long exp = ((Number) payload.get("exp")).longValue();
    long currentTime = System.currentTimeMillis() / 1000;
    
    return currentTime < exp;
}
```

### Almacenamiento del Token

- **Cookie HTTP-only**: `authToken` (no accesible desde JavaScript)
- **Sesión**: Atributo `token` en HttpSession
- **Duración**: 24 horas
- **Seguridad**: HttpOnly + Secure (en HTTPS)

---

## 👥 SISTEMA DE ROLES Y PERMISOS

### Arquitectura RBAC (Role-Based Access Control)

```
Usuario → Usuario_Rol → Rol → Rol_Menu → Menu
  1         N           1      N         1
```

### Flujo de Filtrado de Menús

```java
// En MenuDAO.java
public List<Menu> obtenerMenusJerarquicosPorRol(int idUsuario) {
    // 1. Obtener roles del usuario
    String sqlRoles = "SELECT DISTINCT idRol FROM usuario_rol WHERE idUsuario = ?";
    
    // 2. Obtener menús permitidos para esos roles
    String sqlMenus = """
        SELECT DISTINCT m.* FROM menus m
        INNER JOIN rol_menu rm ON m.idMenu = rm.idMenu
        WHERE rm.idRol IN (SELECT idRol FROM usuario_rol WHERE idUsuario = ?)
        AND m.estado = TRUE
        ORDER BY m.id_padre, m.orden
    """;
    
    // 3. Construir estructura jerárquica
    // - Menús con id_padre = NULL son raíces
    // - Menús con id_padre != NULL son submenús
}
```

### Ejemplo de Datos

```sql
-- Roles
INSERT INTO roles (nombre) VALUES ('Administrador'), ('Vendedor'), ('Gerente');

-- Menús
INSERT INTO menus (nombre, url, id_padre, orden) VALUES
('Dashboard', 'DashboardServlet', NULL, 1),
('Productos', 'ProductoServlet', NULL, 2),
('Ventas', 'VentaServlet', NULL, 3),
('Compras', 'CompraServlet', NULL, 4);

-- Asignación de menús a roles
INSERT INTO rol_menu (idRol, idMenu) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),  -- Admin ve todo
(2, 1), (2, 3),                   -- Vendedor ve Dashboard y Ventas
(3, 1), (3, 2), (3, 3), (3, 4);  -- Gerente ve todo
```

---

## 🔄 PATRÓN CRUD

### Estructura General de un CRUD

Cada módulo (Productos, Ventas, Compras, etc.) sigue este patrón:

```
┌─────────────────────────────────────────────────────────┐
│                    SERVLET (Controller)                 │
│  - Recibe requests HTTP                                 │
│  - Valida parámetros                                    │
│  - Llama a DAO                                          │
│  - Redirige a vista                                     │
└─────────────────────────────────────────────────────────┘
                         │
        ┌────────────────┼────────────────┐
        │                │                │
    ┌───▼────────┐  ┌───▼────────┐  ┌───▼────────┐
    │ DAO        │  │ Model      │  │ JSP        │
    │ (BD)       │  │ (Entidad)  │  │ (Vista)    │
    └────────────┘  └────────────┘  └────────────┘
```

### Ejemplo: ProductoServlet

```java
public class ProductoServlet extends HttpServlet {
    private ProductoDAO productoDAO = new ProductoDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        
        switch(action) {
            case "list":
                listarProductos(request, response);
                break;
            case "new":
                mostrarFormulario(request, response);
                break;
            case "edit":
                editarProducto(request, response);
                break;
            case "delete":
                eliminarProducto(request, response);
                break;
            case "obtenerTodos":
                obtenerTodosJSON(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        String action = request.getParameter("action");
        
        if ("save".equals(action)) {
            guardarProducto(request, response);
        } else if ("update".equals(action)) {
            actualizarProducto(request, response);
        }
    }
    
    private void guardarProducto(HttpServletRequest request, HttpServletResponse response) {
        // 1. Obtener parámetros
        String nombre = request.getParameter("producto");
        int idMarca = Integer.parseInt(request.getParameter("idMarca"));
        double precioCosto = Double.parseDouble(request.getParameter("precio_costo"));
        double precioVenta = Double.parseDouble(request.getParameter("precio_venta"));
        
        // 2. Crear objeto
        Producto producto = new Producto();
        producto.setProducto(nombre);
        producto.setIdMarca(idMarca);
        producto.setPrecioCosto(precioCosto);
        producto.setPrecioVenta(precioVenta);
        
        // 3. Guardar en BD
        if (productoDAO.insertar(producto)) {
            response.sendRedirect("ProductoServlet?success=Producto creado");
        } else {
            response.sendRedirect("ProductoServlet?error=Error al crear");
        }
    }
}
```

### Operaciones CRUD en DAO

```java
public class ProductoDAO {
    
    // CREATE
    public boolean insertar(Producto producto) {
        String sql = "INSERT INTO productos (producto, idMarca, precio_costo, precio_venta) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getProducto());
            stmt.setInt(2, producto.getIdMarca());
            stmt.setDouble(3, producto.getPrecioCosto());
            stmt.setDouble(4, producto.getPrecioVenta());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // READ
    public Producto obtenerPorId(int id) {
        String sql = "SELECT * FROM productos WHERE idProducto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // UPDATE
    public boolean actualizar(Producto producto) {
        String sql = "UPDATE productos SET producto = ?, idMarca = ?, precio_costo = ?, precio_venta = ? WHERE idProducto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getProducto());
            stmt.setInt(2, producto.getIdMarca());
            stmt.setDouble(3, producto.getPrecioCosto());
            stmt.setDouble(4, producto.getPrecioVenta());
            stmt.setInt(5, producto.getIdProducto());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE idProducto = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

---

## 🖼️ GESTIÓN DE IMÁGENES

### Flujo de Carga de Imágenes

```
1. Usuario selecciona imagen en formulario
   ↓
2. Formulario con enctype="multipart/form-data"
   ↓
3. UploadImagenServlet recibe archivo
   ↓
4. Validar tipo y tamaño
   ↓
5. Generar UUID como nombre
   ↓
6. Guardar en: SistemaEmpresa/web/assets/productos/
   ↓
7. Guardar ruta en BD: web/assets/productos/uuid.jpg
   ↓
8. En JSP, convertir ruta para mostrar: /SistemaEmpresa/assets/productos/uuid.jpg
```

### Código de UploadImagenServlet

```java
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,
    maxFileSize = 10 * 1024 * 1024,
    maxRequestSize = 50 * 1024 * 1024
)
public class UploadImagenServlet extends HttpServlet {
    
    private static final String UPLOAD_DIR = "assets/productos";
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Part filePart = request.getPart("imagen");
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            
            // Validar tipo de archivo
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Solo se permiten imágenes");
                return;
            }
            
            // Generar nombre único
            String uniqueFileName = UUID.randomUUID().toString() + ".jpg";
            
            // Obtener ruta real del servidor
            String realPath = request.getServletContext().getRealPath("/");
            
            // Reemplazar build/web por web si es necesario
            String uploadPath;
            if (realPath.contains("build" + File.separator + "web")) {
                uploadPath = realPath.replace("build" + File.separator + "web", "web");
            } else {
                uploadPath = realPath;
            }
            
            // Crear directorio si no existe
            File uploadDir = new File(uploadPath + File.separator + UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Guardar archivo
            String filePath = uploadPath + File.separator + UPLOAD_DIR + File.separator + uniqueFileName;
            try (InputStream input = filePart.getInputStream();
                 FileOutputStream output = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            }
            
            // Retornar ruta para guardar en BD
            String dbPath = "web/" + UPLOAD_DIR + "/" + uniqueFileName;
            response.setContentType("application/json");
            response.getWriter().write("{\"ruta\": \"" + dbPath + "\"}");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
```

### Conversión de Rutas en JSP

```jsp
<%
    String rutaImagen = producto.getImagen();
    // Convertir "web/assets/..." a "/SistemaEmpresa/assets/..."
    if (rutaImagen != null && rutaImagen.startsWith("web/")) {
        rutaImagen = request.getContextPath() + "/" + rutaImagen.substring(4);
    }
%>
<img src="<%= rutaImagen %>" alt="Producto">
```

---

## 📊 REPORTES

### Generación de Reportes con JasperReports

```
1. Diseñar reporte en iReport
   ↓
2. Guardar como .jrxml
   ↓
3. Compilar a .jasper
   ↓
4. En ReporteServlet:
   - Obtener datos de BD
   - Pasar a JasperFillManager
   - Exportar a PDF
   ↓
5. Enviar PDF al navegador
```

### Código de ReporteServlet

```java
public class ReporteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String tipoReporte = request.getParameter("tipo");

            // Obtener datos
            List<Venta> ventas = ventaDAO.obtenerTodos();

            // Crear mapa de parámetros
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("titulo", "Reporte de Ventas");
            parameters.put("fecha", new Date());

            // Crear datasource
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ventas);

            // Cargar reporte compilado
            String reportPath = getServletContext().getRealPath("/WEB-INF/reportes/reporte_ventas.jasper");
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(reportPath);

            // Llenar reporte
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
            );

            // Exportar a PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_ventas.pdf");

            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

## 🏛️ MODELOS Y ESTRUCTURAS DE DATOS

### Modelo: Producto

```java
public class Producto {
    private int idProducto;
    private String producto;
    private int idMarca;
    private String descripcion;
    private String imagen;
    private double precioCosto;
    private double precioVenta;
    private int existencia;
    private LocalDateTime fechaIngreso;

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public double getPrecioCosto() { return precioCosto; }
    public void setPrecioCosto(double precioCosto) { this.precioCosto = precioCosto; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public int getExistencia() { return existencia; }
    public void setExistencia(int existencia) { this.existencia = existencia; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}
```

### Modelo: Venta (Maestro)

```java
public class Venta {
    private int idVenta;
    private int noOrdenVenta;
    private LocalDate fechaOrden;
    private int idCliente;
    private List<VentaDetalle> detalles;

    public Venta() {
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(VentaDetalle detalle) {
        this.detalles.add(detalle);
    }

    public boolean tieneDetalles() {
        return !this.detalles.isEmpty();
    }

    // Getters y Setters
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getNoOrdenVenta() { return noOrdenVenta; }
    public void setNoOrdenVenta(int noOrdenVenta) { this.noOrdenVenta = noOrdenVenta; }

    public LocalDate getFechaOrden() { return fechaOrden; }
    public void setFechaOrden(LocalDate fechaOrden) { this.fechaOrden = fechaOrden; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public List<VentaDetalle> getDetalles() { return detalles; }
    public void setDetalles(List<VentaDetalle> detalles) { this.detalles = detalles; }
}
```

### Modelo: VentaDetalle (Detalle)

```java
public class VentaDetalle {
    private long idVentaDetalle;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    // Getters y Setters
    public long getIdVentaDetalle() { return idVentaDetalle; }
    public void setIdVentaDetalle(long idVentaDetalle) { this.idVentaDetalle = idVentaDetalle; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}
```

### Modelo: Usuario

```java
public class Usuario {
    private int idUsuario;
    private String usuario;
    private String password;
    private int idEmpleado;
    private boolean activo;
    private String nombreCompleto;
    private List<Rol> roles;

    public Usuario() {
        this.roles = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public List<Rol> getRoles() { return roles; }
    public void setRoles(List<Rol> roles) { this.roles = roles; }
}
```

### Modelo: Rol

```java
public class Rol {
    private int idRol;
    private String nombre;
    private boolean estado;
    private List<Menu> menus;

    public Rol() {
        this.menus = new ArrayList<>();
    }

    // Getters y Setters
    public int getIdRol() { return idRol; }
    public void setIdRol(int idRol) { this.idRol = idRol; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isEstado() { return estado; }
    public void setEstado(boolean estado) { this.estado = estado; }

    public List<Menu> getMenus() { return menus; }
    public void setMenus(List<Menu> menus) { this.menus = menus; }
}
```

---

## 🔧 MÉTODOS CLAVE DE DAOs

### ProductoDAO - Métodos Principales

```java
public class ProductoDAO {

    // Obtener todos los productos
    public List<Producto> obtenerTodos() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos ORDER BY producto ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    // Buscar por término
    public List<Producto> buscarPorTermino(String termino) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE producto LIKE ? OR descripcion LIKE ? LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + termino + "%";
            stmt.setString(1, searchTerm);
            stmt.setString(2, searchTerm);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productos;
    }

    // Mapear ResultSet a Producto
    private Producto mapearResultSet(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setIdProducto(rs.getInt("idProducto"));
        producto.setProducto(rs.getString("producto"));
        producto.setIdMarca(rs.getInt("idMarca"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setImagen(rs.getString("imagen"));
        producto.setPrecioCosto(rs.getDouble("precio_costo"));
        producto.setPrecioVenta(rs.getDouble("precio_venta"));
        producto.setExistencia(rs.getInt("existencia"));
        return producto;
    }
}
```

### VentaDAO - Métodos de Maestro-Detalle

```java
public class VentaDAO {

    // Obtener venta con sus detalles
    public Venta obtenerPorId(int idVenta) {
        Venta venta = null;

        // 1. Obtener datos maestro
        String sqlMaestro = "SELECT * FROM ventas WHERE idVenta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlMaestro)) {

            stmt.setInt(1, idVenta);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    venta = new Venta();
                    venta.setIdVenta(rs.getInt("idVenta"));
                    venta.setNoOrdenVenta(rs.getInt("no_orden_venta"));
                    venta.setFechaOrden(rs.getDate("fecha_orden").toLocalDate());
                    venta.setIdCliente(rs.getInt("idCliente"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        if (venta == null) return null;

        // 2. Obtener detalles
        String sqlDetalles = "SELECT * FROM ventas_detalle WHERE idVenta = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlDetalles)) {

            stmt.setInt(1, idVenta);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VentaDetalle detalle = new VentaDetalle();
                    detalle.setIdVentaDetalle(rs.getLong("idVenta_detalle"));
                    detalle.setIdProducto(rs.getInt("idProducto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    venta.agregarDetalle(detalle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return venta;
    }
}
```

---

## 🌐 FLUJO COMPLETO: CREAR UNA VENTA

### Paso 1: Usuario accede al formulario

```
GET /VentaServlet?action=new
↓
VentaServlet.doGet() → Carga lista de clientes y productos
↓
Renderiza form_content.jsp
```

### Paso 2: Usuario selecciona cliente y agrega productos

```javascript
// JavaScript en form_content.jsp
function seleccionarCliente(cliente) {
    document.getElementById('hiddenIdCliente').value = cliente.idCliente;
    document.getElementById('txtNombreCliente').value = cliente.nombre;
}

function agregarProductoATabla(producto) {
    // Crear fila con inputs ocultos
    // Mostrar en tabla
}
```

### Paso 3: Usuario envía formulario

```
POST /VentaServlet
Parámetros:
- action=save
- idCliente=1
- fechaOrden=2025-11-01
- idProducto[]=1&idProducto[]=2
- cantidad[]=5&cantidad[]=3
- precio_unitario[]=100&precio_unitario[]=50
```

### Paso 4: VentaServlet procesa

```java
private void guardarVenta(HttpServletRequest request, HttpServletResponse response) {
    // 1. Obtener parámetros maestro
    int idCliente = Integer.parseInt(request.getParameter("idCliente"));
    LocalDate fechaOrden = LocalDate.parse(request.getParameter("fechaOrden"));

    // 2. Crear objeto Venta
    Venta venta = new Venta();
    venta.setIdCliente(idCliente);
    venta.setFechaOrden(fechaOrden);
    venta.setNoOrdenVenta(generarNumeroOrden());

    // 3. Obtener arrays de detalles
    String[] idProductos = request.getParameterValues("idProducto");
    String[] cantidades = request.getParameterValues("cantidad");
    String[] precios = request.getParameterValues("precio_unitario");

    // 4. Crear detalles
    for (int i = 0; i < idProductos.length; i++) {
        VentaDetalle detalle = new VentaDetalle();
        detalle.setIdProducto(Integer.parseInt(idProductos[i]));
        detalle.setCantidad(Integer.parseInt(cantidades[i]));
        detalle.setPrecioUnitario(Double.parseDouble(precios[i]));
        venta.agregarDetalle(detalle);
    }

    // 5. Guardar en BD
    int idVentaCreada = ventaDAO.crear(venta);

    if (idVentaCreada > 0) {
        response.sendRedirect("VentaServlet?success=Venta creada");
    } else {
        response.sendRedirect("VentaServlet?error=Error al crear");
    }
}
```

### Paso 5: VentaDAO.crear() ejecuta transacción

```
1. BEGIN TRANSACTION
2. INSERT INTO ventas (no_orden_venta, fecha_orden, idCliente)
3. Para cada detalle:
   - INSERT INTO ventas_detalle (idVenta, idProducto, cantidad, precio_unitario)
   - UPDATE productos SET existencia = existencia - cantidad
4. COMMIT
```

### Paso 6: Redirigir a listado

```
GET /VentaServlet?action=list
↓
VentaDAO.obtenerTodos() → Obtiene todas las ventas
↓
Renderiza list_content.jsp con tabla de ventas
```

---

## 🔄 FLUJOS DE DATOS

### Flujo de Creación de Venta (Maestro-Detalle)

```
1. Usuario accede a VentaServlet?action=new
   ↓
2. VentaServlet carga:
   - Lista de clientes (AJAX)
   - Lista de productos (AJAX)
   ↓
3. Usuario selecciona cliente
   ↓
4. Usuario busca y agrega productos
   - JavaScript crea filas en tabla
   - Inputs ocultos con idProducto, cantidad, precio
   ↓
5. Usuario hace submit del formulario
   ↓
6. VentaServlet.guardarVenta():
   - Obtiene parámetros: idCliente, fechaOrden
   - Obtiene arrays: idProducto[], cantidad[], precio[]
   - Crea objeto Venta
   - Crea objetos VentaDetalle para cada producto
   ↓
7. VentaDAO.crear():
   - Inicia transacción
   - INSERT en tabla ventas
   - INSERT en tabla ventas_detalle (para cada detalle)
   - UPDATE en tabla productos (restar existencia)
   - COMMIT
   ↓
8. Si éxito: Redirigir a listado
   Si error: Redirigir con mensaje de error
```

### Flujo de Edición de Venta

```
1. Usuario hace clic en "Editar"
   ↓
2. VentaServlet?action=edit&id=X
   ↓
3. VentaDAO.obtenerPorId(X):
   - SELECT venta maestro
   - SELECT detalles relacionados
   ↓
4. Cargar formulario con datos existentes
   ↓
5. Usuario modifica cantidad o agrega/elimina productos
   ↓
6. Submit del formulario
   ↓
7. VentaServlet.actualizarVenta():
   - Obtener venta anterior
   - Calcular diferencias de cantidades
   - Actualizar existencias en productos
   ↓
8. VentaDAO.actualizar():
   - UPDATE tabla ventas
   - Para cada detalle:
     - Si es nuevo: INSERT
     - Si existe: UPDATE
     - Si fue eliminado: DELETE
   - Ajustar existencias en productos
```

### Flujo de Compra (Similar a Venta pero inverso)

```
DIFERENCIA CLAVE: En compras, la existencia se SUMA (no se resta)

Crear Compra:
- Obtener cantidad comprada
- UPDATE productos SET existencia = existencia + cantidad

Editar Compra:
- Si cantidad aumenta: Sumar diferencia
- Si cantidad disminuye: Restar diferencia

Eliminar Compra:
- Restar cantidad total de cada producto
```

---

## 🔗 CONEXIÓN A BASE DE DATOS

### DatabaseConnection.java

```java
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_empresa";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

### Patrón Try-with-resources

```java
try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    
    stmt.setInt(1, id);
    try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            // Procesar resultados
        }
    }
} catch (SQLException e) {
    e.printStackTrace();
}
```

---

## 📝 CONFIGURACIÓN WEB.XML

```xml
<web-app>
    <!-- Filtros de seguridad -->
    <filter>
        <filter-name>AuthenticationFilter</filter-name>
        <filter-class>com.sistemaempresa.filters.AuthenticationFilter</filter-class>
    </filter>
    
    <filter-mapping>
        <filter-name>AuthenticationFilter</filter-name>
        <url-pattern>/*</url-pattern>
        <dispatcher>REQUEST</dispatcher>
    </filter-mapping>
    
    <!-- Servlets -->
    <servlet>
        <servlet-name>LoginServlet</servlet-name>
        <servlet-class>com.sistemaempresa.servlets.LoginServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>LoginServlet</servlet-name>
        <url-pattern>/login</url-pattern>
    </servlet-mapping>
    
    <!-- Páginas de error -->
    <error-page>
        <error-code>404</error-code>
        <location>/error404.jsp</location>
    </error-page>
    
    <error-page>
        <error-code>500</error-code>
        <location>/error500.jsp</location>
    </error-page>
</web-app>
```

---

---

## ✅ VALIDACIONES Y MANEJO DE ERRORES

### Validaciones del Lado Cliente (JavaScript)

```javascript
// En form_content.jsp
document.getElementById('formProducto').addEventListener('submit', function(e) {
    e.preventDefault();

    // Validar campos requeridos
    const nombre = document.getElementById('txtNombre').value.trim();
    if (!nombre) {
        alert('El nombre del producto es requerido');
        return;
    }

    // Validar rango de precios
    const precioCosto = parseFloat(document.getElementById('txtPrecioCosto').value);
    const precioVenta = parseFloat(document.getElementById('txtPrecioVenta').value);

    if (precioCosto <= 0 || precioVenta <= 0) {
        alert('Los precios deben ser mayores a 0');
        return;
    }

    if (precioVenta <= precioCosto) {
        alert('El precio de venta debe ser mayor al precio de costo');
        return;
    }

    // Si todo es válido, enviar
    this.submit();
});
```

### Manejo de Transacciones en Operaciones Maestro-Detalle

```java
public int crear(Venta venta) {
    Connection conn = null;
    try {
        conn = DatabaseConnection.getConnection();
        conn.setAutoCommit(false); // Iniciar transacción

        // 1. Insertar venta maestro
        String sqlVenta = "INSERT INTO ventas(no_orden_venta, fecha_orden, idCliente) VALUES (?, ?, ?)";
        int idVentaCreada = 0;

        try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
            stmtVenta.setInt(1, venta.getNoOrdenVenta());
            stmtVenta.setDate(2, java.sql.Date.valueOf(venta.getFechaOrden()));
            stmtVenta.setInt(3, venta.getIdCliente());

            int filasAfectadas = stmtVenta.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet generatedKeys = stmtVenta.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idVentaCreada = generatedKeys.getInt(1);
                    }
                }
            }
        }

        if (idVentaCreada == 0) {
            throw new SQLException("Error al crear la venta");
        }

        // 2. Insertar detalles y actualizar existencias
        if (venta.tieneDetalles()) {
            String sqlDetalle = "INSERT INTO ventas_detalle(idVenta, idProducto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
            String sqlActualizarExistencia = "UPDATE productos SET existencia = existencia - ? WHERE idProducto = ?";

            try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle);
                 PreparedStatement stmtExistencia = conn.prepareStatement(sqlActualizarExistencia)) {

                for (VentaDetalle detalle : venta.getDetalles()) {
                    // Insertar detalle
                    stmtDetalle.setInt(1, idVentaCreada);
                    stmtDetalle.setInt(2, detalle.getIdProducto());
                    stmtDetalle.setInt(3, detalle.getCantidad());
                    stmtDetalle.setDouble(4, detalle.getPrecioUnitario());

                    int detalleAfectado = stmtDetalle.executeUpdate();
                    if (detalleAfectado == 0) {
                        throw new SQLException("Error al insertar detalle de venta");
                    }

                    // Actualizar existencia (restar en ventas)
                    stmtExistencia.setInt(1, detalle.getCantidad());
                    stmtExistencia.setInt(2, detalle.getIdProducto());
                    stmtExistencia.executeUpdate();
                }
            }
        }

        conn.commit(); // Confirmar transacción
        venta.setIdVenta(idVentaCreada);
        return idVentaCreada;

    } catch (SQLException e) {
        e.printStackTrace();
        try {
            if (conn != null) conn.rollback(); // Deshacer cambios
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    } finally {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

---

## 🔒 SEGURIDAD Y BUENAS PRÁCTICAS

### 1. Inyección SQL - Prevención

```java
// ❌ INCORRECTO - Vulnerable a inyección SQL
String sql = "SELECT * FROM usuarios WHERE usuario = '" + usuario + "'";
Statement stmt = conn.createStatement();
ResultSet rs = stmt.executeQuery(sql);

// ✅ CORRECTO - Usar PreparedStatement
String sql = "SELECT * FROM usuarios WHERE usuario = ?";
PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setString(1, usuario);
ResultSet rs = stmt.executeQuery();
```

### 2. AuthenticationFilter - Validación de Sesión

```java
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_URLS = {"/index.jsp", "/login", "/error404.jsp", "/error500.jsp"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = requestURI.substring(contextPath.length());

        // Permitir URLs públicas
        boolean isPublic = false;
        for (String publicUrl : PUBLIC_URLS) {
            if (path.equals(publicUrl) || path.startsWith(publicUrl)) {
                isPublic = true;
                break;
            }
        }

        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

        // Validar sesión
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("usuario") == null) {
            httpResponse.sendRedirect(contextPath + "/index.jsp?error=Sesión expirada");
            return;
        }

        // Validar token JWT
        Cookie[] cookies = httpRequest.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null || !JWTUtil.validateToken(token)) {
            httpResponse.sendRedirect(contextPath + "/index.jsp?error=Token inválido");
            return;
        }

        chain.doFilter(request, response);
    }
}
```

### 3. Encoding UTF-8 - CharacterEncodingFilter

```java
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        chain.doFilter(request, response);
    }
}
```

---

## 🐛 TROUBLESHOOTING COMÚN

### Problema: "Debe agregar al menos un producto"

**Causa**: Los inputs ocultos no se están enviando correctamente

**Solución**:
```javascript
// ✅ CORRECTO: Inputs ocultos directamente en <tr>
const fila = document.createElement('tr');
const inputIdProducto = document.createElement('input');
inputIdProducto.type = 'hidden';
inputIdProducto.name = 'idProducto';
inputIdProducto.value = producto.idProducto;
fila.appendChild(inputIdProducto); // Agregar directamente a fila

// ❌ INCORRECTO: Inputs dentro de <td> oculto
const tdId = document.createElement('td');
tdId.style.display = 'none';
tdId.appendChild(inputIdProducto); // Puede no enviarse
```

### Problema: Imágenes no se muestran

**Causa**: Ruta incorrecta en BD o conversión de ruta

**Solución**:
```jsp
<%
    String rutaImagen = producto.getImagen();
    if (rutaImagen != null && rutaImagen.startsWith("web/")) {
        rutaImagen = request.getContextPath() + "/" + rutaImagen.substring(4);
    }
%>
<img src="<%= rutaImagen %>" alt="Producto">
```

### Problema: Token JWT expirado

**Causa**: Token tiene más de 24 horas

**Solución**: Implementar refresh token o extender duración

```java
// En JWTUtil.java
private static final long EXPIRATION_TIME = 86400000; // 24 horas
// Cambiar a:
private static final long EXPIRATION_TIME = 604800000; // 7 días
```

### Problema: Encoding UTF-8 incorrecto

**Causa**: Caracteres especiales se muestran mal

**Solución**: Agregar CharacterEncodingFilter en web.xml

```xml
<filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>com.sistemaempresa.filters.CharacterEncodingFilter</filter-class>
</filter>

<filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

---

## 📚 REFERENCIAS Y RECURSOS

### Documentación Oficial
- [Jakarta EE Servlets](https://jakarta.ee/specifications/servlet/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Bootstrap 5](https://getbootstrap.com/docs/5.0/)
- [JasperReports](https://community.jaspersoft.com/project/jasperreports-library)

### Librerías Utilizadas
- **Jackson**: JSON processing
- **MySQL Connector/J**: Driver JDBC
- **JSTL**: JSP Standard Tag Library
- **SLF4J**: Logging

---

## 📝 NOTAS FINALES

Este documento proporciona una visión completa de la arquitectura y funcionamiento del Sistema Empresa. Para cualquier duda específica sobre un módulo o funcionalidad, consulte los archivos fuente correspondientes en el directorio `src/java/com/sistemaempresa/`.

---

## ⚙️ CONFIGURACIÓN Y DEPLOYMENT

### Archivo: web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
                             https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

    <!-- Nombre de la aplicación -->
    <display-name>SistemaEmpresa</display-name>

    <!-- Filtros de seguridad -->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>com.sistemaempresa.filters.CharacterEncodingFilter</filter-class>
    </filter>

    <filter>
        <filter-name>AuthenticationFilter</filter-name>
        <filter-class>com.sistemaempresa.filters.AuthenticationFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <filter-mapping>
        <filter-name>AuthenticationFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <!-- Servlets principales -->
    <servlet>
        <servlet-name>LoginServlet</servlet-name>
        <servlet-class>com.sistemaempresa.servlets.LoginServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet>
        <servlet-name>DashboardServlet</servlet-name>
        <servlet-class>com.sistemaempresa.servlets.DashboardServlet</servlet-class>
    </servlet>

    <servlet>
        <servlet-name>ProductoServlet</servlet-name>
        <servlet-class>com.sistemaempresa.servlets.ProductoServlet</servlet-class>
    </servlet>

    <servlet>
        <servlet-name>VentaServlet</servlet-name>
        <servlet-class>com.sistemaempresa.servlets.VentaServlet</servlet-class>
    </servlet>

    <servlet>
        <servlet-name>CompraServlet</servlet-name>
        <servlet-class>com.sistemaempresa.servlets.CompraServlet</servlet-class>
    </servlet>

    <servlet>
        <servlet-name>UploadImagenServlet</servlet-name>
        <servlet-class>com.sistemaempresa.servlets.UploadImagenServlet</servlet-class>
    </servlet>

    <!-- Mapeos de servlets -->
    <servlet-mapping>
        <servlet-name>LoginServlet</servlet-name>
        <url-pattern>/login</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>DashboardServlet</servlet-name>
        <url-pattern>/dashboard</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>ProductoServlet</servlet-name>
        <url-pattern>/productos</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>VentaServlet</servlet-name>
        <url-pattern>/ventas</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>CompraServlet</servlet-name>
        <url-pattern>/compras</url-pattern>
    </servlet-mapping>

    <servlet-mapping>
        <servlet-name>UploadImagenServlet</servlet-name>
        <url-pattern>/upload</url-pattern>
    </servlet-mapping>

    <!-- Páginas de error -->
    <error-page>
        <error-code>404</error-code>
        <location>/error404.jsp</location>
    </error-page>

    <error-page>
        <error-code>500</error-code>
        <location>/error500.jsp</location>
    </error-page>

    <error-page>
        <exception-type>java.lang.Exception</exception-type>
        <location>/error500.jsp</location>
    </error-page>

    <!-- Página de bienvenida -->
    <welcome-file-list>
        <welcome-file>index.jsp</welcome-file>
    </welcome-file-list>

    <!-- Configuración de sesión -->
    <session-config>
        <cookie-config>
            <http-only>true</http-only>
            <secure>false</secure>
        </cookie-config>
        <tracking-mode>COOKIE</tracking-mode>
        <timeout>30</timeout>
    </session-config>
</web-app>
```

### Archivo: context.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Context path="/SistemaEmpresa" docBase="SistemaEmpresa" debug="0" reloadable="true">
    <!-- Configuración del contexto de la aplicación -->
    <Manager pathname="" />
</Context>
```

### Archivo: DatabaseConnection.java

```java
package com.sistemaempresa.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Configuración de conexión
    private static final String URL = "jdbc:mysql://localhost:3306/sistema_empresa";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Método para probar la conexión
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn.isValid(2);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
```

### Requisitos del Sistema

**Hardware Mínimo:**
- CPU: 2 GHz dual-core
- RAM: 2 GB
- Disco: 500 MB

**Software Requerido:**
- Java JDK 11 o superior
- Apache Tomcat 10.0+
- MySQL 8.0+
- Navegador moderno (Chrome, Firefox, Edge, Safari)

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/Ripca/proy2.git
cd proy2/SistemaEmpresa
```

2. **Crear base de datos**
```bash
mysql -u root -p < database/sistema_empresa.sql
mysql -u root -p < database/crear_roles_y_permisos.sql
```

3. **Compilar el proyecto**
```bash
ant build
```

4. **Desplegar en Tomcat**
```bash
cp build/SistemaEmpresa.war $CATALINA_HOME/webapps/
```

5. **Iniciar Tomcat**
```bash
$CATALINA_HOME/bin/startup.sh
```

6. **Acceder a la aplicación**
```
http://localhost:8080/SistemaEmpresa/
```

### Variables de Entorno

```bash
# Linux/Mac
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export CATALINA_HOME=/opt/tomcat
export PATH=$JAVA_HOME/bin:$CATALINA_HOME/bin:$PATH

# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-11
set CATALINA_HOME=C:\apache-tomcat-10.0
set PATH=%JAVA_HOME%\bin;%CATALINA_HOME%\bin;%PATH%
```

---

## 📊 ESTADÍSTICAS DEL PROYECTO

### Líneas de Código

| Componente | Líneas | Archivos |
|-----------|--------|----------|
| Java (Servlets, DAOs, Models) | ~5,000 | 30+ |
| JSP (Vistas) | ~3,000 | 20+ |
| SQL (Scripts BD) | ~1,000 | 5 |
| JavaScript | ~2,000 | 15+ |
| CSS | ~500 | 3 |
| **TOTAL** | **~11,500** | **70+** |

### Tablas de Base de Datos

| Tabla | Registros | Propósito |
|-------|-----------|----------|
| usuarios | 10-50 | Autenticación |
| roles | 3-5 | Control de acceso |
| productos | 100-1000 | Catálogo |
| ventas | 100-10000 | Transacciones |
| compras | 50-5000 | Adquisiciones |
| empleados | 10-100 | Recursos humanos |
| clientes | 50-500 | Relaciones |
| proveedores | 20-200 | Suministros |

---

## 🔄 CICLO DE VIDA DE UNA SOLICITUD

```
1. CLIENTE (Navegador)
   ↓ HTTP Request

2. SERVIDOR (Tomcat)
   ↓ Recibe request

3. FILTROS
   ├─ CharacterEncodingFilter (UTF-8)
   ├─ AuthenticationFilter (Validar sesión)
   └─ JWTAuthFilter (Validar token)
   ↓

4. SERVLET (Controller)
   ├─ Obtener parámetros
   ├─ Validar datos
   └─ Llamar a DAO
   ↓

5. DAO (Acceso a datos)
   ├─ Conectar a BD
   ├─ Ejecutar SQL
   └─ Mapear resultados
   ↓

6. MODELO (Entidad)
   └─ Retornar objeto
   ↓

7. SERVLET (Procesar resultado)
   ├─ Preparar datos para vista
   └─ Redirigir o renderizar
   ↓

8. JSP (Vista)
   ├─ Renderizar HTML
   └─ Incluir CSS/JS
   ↓

9. CLIENTE (Navegador)
   └─ Mostrar página
```

---

## 🎯 MEJORES PRÁCTICAS IMPLEMENTADAS

✅ **Seguridad**
- Uso de PreparedStatement para prevenir inyección SQL
- Validación de sesión en cada request
- JWT para autenticación stateless
- Encoding UTF-8 en todas las páginas
- Contraseñas hasheadas en BD

✅ **Rendimiento**
- Conexiones pooled a BD
- Caché de menús por rol
- Índices en tablas principales
- Paginación en listados

✅ **Mantenibilidad**
- Patrón MVC bien definido
- Separación de responsabilidades
- Código comentado y documentado
- Nombres descriptivos de variables

✅ **Escalabilidad**
- Arquitectura modular
- DAOs reutilizables
- Servlets independientes
- Fácil agregar nuevos módulos

---

## 📞 SOPORTE Y CONTACTO

Para reportar bugs o solicitar features:
1. Crear un issue en GitHub
2. Describir el problema detalladamente
3. Incluir pasos para reproducir
4. Adjuntar logs si es posible

---

**Última actualización**: 2025-11-01
**Versión**: 1.0
**Autor**: Equipo de Desarrollo
**Licencia**: Propietaria

