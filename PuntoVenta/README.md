# PuntoVenta - Sistema de Gestión Comercial

Sistema completo de punto de venta desarrollado en Java Web con JSP, basado en el proyecto RepoPro de C++ pero adaptado para web con arquitectura MVC.

## 🚀 Características Principales

### ✅ **Funcionalidades Implementadas**

#### **Autenticación y Seguridad**
- Sistema de login con JWT (JSON Web Tokens)
- Autenticación basada en roles (ADMIN, SUPERVISOR, VENDEDOR, CAJERO)
- Filtros de seguridad para páginas protegidas
- Sesiones persistentes con "Recordarme"
- Hash de contraseñas con SHA-256

#### **Gestión de Ventas**
- Interfaz avanzada para crear ventas
- Búsqueda de productos por código de barras
- Formato de entrada: "cantidad*código" o solo "código"
- Modales para selección de clientes y productos
- Cálculo automático de subtotales, descuentos y totales
- Múltiples métodos de pago
- Actualización automática de inventario

#### **Gestión de Compras**
- Sistema completo de órdenes de compra
- Estados: PENDIENTE, RECIBIDA, CANCELADA
- Gestión de proveedores
- Actualización automática de stock al recibir compras
- Seguimiento de fechas de entrega

#### **Gestión de Inventario**
- CRUD completo de productos
- Control de stock mínimo
- Alertas de productos con stock bajo
- Gestión de marcas y categorías
- Códigos de barras únicos
- Cálculo automático de márgenes de ganancia

#### **Gestión de Clientes y Proveedores**
- CRUD completo con validaciones
- Búsqueda por NIT, nombre, etc.
- Historial de transacciones
- Top clientes por volumen de compras

#### **Dashboard Interactivo**
- Estadísticas en tiempo real
- Carrusel de imágenes promocionales
- Alertas de stock bajo
- Compras pendientes
- Gráficos y métricas

#### **Sistema de Usuarios**
- Gestión completa de empleados
- Asignación de puestos y salarios
- Control de acceso basado en roles
- Bloqueo automático por intentos fallidos

### 🏗️ **Arquitectura Técnica**

#### **Patrón MVC Implementado**
```
├── Models (com.puntoventa.models)
│   ├── Usuario.java
│   ├── Empleado.java
│   ├── Cliente.java
│   ├── Producto.java
│   ├── Venta.java / VentaDetalle.java
│   ├── Compra.java / CompraDetalle.java
│   └── ...
├── DAOs (com.puntoventa.dao)
│   ├── UsuarioDAO.java
│   ├── ProductoDAO.java
│   ├── VentaDAO.java
│   ├── CompraDAO.java
│   └── ...
├── Controllers (com.puntoventa.servlets)
│   ├── LoginServlet.java
│   ├── DashboardServlet.java
│   ├── ProductoServlet.java
│   ├── VentaServlet.java
│   └── ...
└── Views (web/WEB-INF/views)
    ├── login.jsp
    ├── dashboard.jsp
    ├── productos/
    ├── ventas/
    └── ...
```

#### **Base de Datos MySQL**
- **15+ tablas** con relaciones bien definidas
- **Triggers automáticos** para cálculo de totales
- **Índices optimizados** para consultas rápidas
- **Datos iniciales** completos para pruebas

#### **Tecnologías Utilizadas**
- **Backend**: Java 8+, Servlets, JSP, JDBC
- **Frontend**: Bootstrap 5, jQuery, Font Awesome
- **Base de Datos**: MySQL 8.0+
- **Seguridad**: JWT, SHA-256, Filtros de autenticación
- **APIs**: RESTful endpoints para AJAX

## 📦 **Estructura del Proyecto**

```
PuntoVenta/
├── database/
│   ├── puntoventa_completo.sql     # Schema completo
│   └── datos_iniciales.sql         # Datos de prueba
├── src/java/com/puntoventa/
│   ├── dao/                        # Data Access Objects
│   ├── models/                     # Modelos de datos
│   ├── servlets/                   # Controladores
│   ├── utils/                      # Utilidades (JWT, DB)
│   └── filters/                    # Filtros de seguridad
├── web/
│   ├── WEB-INF/
│   │   ├── views/                  # Páginas JSP
│   │   └── web.xml                 # Configuración
│   └── assets/                     # CSS, JS, imágenes
└── README.md
```

## 🛠️ **Instalación y Configuración**

### **Prerrequisitos**
- Java JDK 8 o superior
- Apache Tomcat 9.0+
- MySQL 8.0+
- IDE (NetBeans, IntelliJ, Eclipse)

### **Pasos de Instalación**

1. **Configurar Base de Datos**
```sql
-- Crear base de datos
CREATE DATABASE PuntoVenta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Ejecutar scripts
mysql -u root -p PuntoVenta < database/puntoventa_completo.sql
mysql -u root -p PuntoVenta < database/datos_iniciales.sql
```

2. **Configurar Conexión**
Editar `src/java/com/puntoventa/utils/DatabaseConnection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/PuntoVenta";
private static final String USER = "tu_usuario";
private static final String PASSWORD = "tu_password";
```

3. **Desplegar en Tomcat**
- Copiar el proyecto a `webapps/PuntoVenta`
- Iniciar Tomcat
- Acceder a `http://localhost:8080/PuntoVenta`

## 👥 **Usuarios de Prueba**

| Usuario    | Contraseña | Rol        | Permisos                    |
|------------|------------|------------|-----------------------------|
| admin      | admin123   | ADMIN      | Acceso completo al sistema  |
| supervisor | super123   | SUPERVISOR | Ventas, compras, reportes   |
| vendedor1  | vend123    | VENDEDOR   | Ventas y consultas          |
| cajero1    | caj123     | CAJERO     | Solo ventas                 |

## 🔧 **Funcionalidades Avanzadas**

### **Sistema de Ventas**
- **Escáner de códigos de barras**: Soporte para formato "cantidad*código"
- **Modales dinámicos**: Búsqueda rápida de clientes y productos
- **DataTables avanzados**: Tabla interactiva con 8 columnas
- **Cálculos automáticos**: Subtotal, descuento, total en tiempo real
- **Métodos de pago**: Efectivo, tarjeta, transferencia
- **Validaciones**: Stock disponible, datos requeridos

### **Sistema de Compras**
- **Órdenes de compra**: Numeración automática
- **Estados de seguimiento**: Pendiente → Recibida → Cancelada
- **Actualización de inventario**: Automática al recibir mercancía
- **Gestión de proveedores**: Búsqueda por NIT y nombre

### **Dashboard Inteligente**
- **Métricas en tiempo real**: Productos, clientes, ventas del día
- **Alertas automáticas**: Stock bajo, compras pendientes
- **Carrusel promocional**: Imágenes configurables
- **Auto-refresh**: Actualización cada 5 minutos

## 🔐 **Seguridad Implementada**

### **Autenticación JWT**
- Tokens con expiración de 24 horas
- Información del usuario encriptada
- Validación automática en cada petición

### **Control de Acceso**
- Filtros de autenticación en todas las páginas protegidas
- Verificación de permisos por rol
- Bloqueo automático tras 3 intentos fallidos

### **Validaciones**
- Sanitización de entradas
- Prevención de SQL Injection con PreparedStatements
- Validación de tipos de datos

## 📊 **Base de Datos**

### **Tablas Principales**
- **Usuarios**: Sistema de autenticación
- **Empleados**: Información personal y laboral
- **Productos**: Inventario con códigos de barras
- **Clientes/Proveedores**: Gestión de contactos
- **Ventas/Compras**: Transacciones maestro-detalle
- **Menus**: Sistema de navegación dinámico

### **Triggers Automáticos**
```sql
-- Ejemplo: Cálculo automático de totales en ventas
CREATE TRIGGER tr_ventas_detalle_insert 
AFTER INSERT ON Ventas_detalle
FOR EACH ROW
BEGIN
    UPDATE Ventas 
    SET subtotal = (SELECT SUM(subtotal) FROM Ventas_detalle WHERE idVenta = NEW.idVenta),
        total = subtotal - descuento
    WHERE idVenta = NEW.idVenta;
END
```

## 🚀 **Próximas Mejoras**

### **Funcionalidades Pendientes**
- [ ] Módulo de reportes avanzados
- [ ] Sistema de facturación electrónica
- [ ] Integración con APIs de bancos
- [ ] Módulo de contabilidad
- [ ] App móvil para vendedores
- [ ] Sistema de promociones y descuentos

### **Mejoras Técnicas**
- [ ] Migración a Spring Boot
- [ ] API REST completa
- [ ] Autenticación OAuth2
- [ ] Cache con Redis
- [ ] Microservicios
- [ ] Contenedores Docker

## 📝 **Notas de Desarrollo**

Este proyecto fue desarrollado siguiendo las mejores prácticas de Java Web:

1. **Patrón MVC estricto**: Separación clara de responsabilidades
2. **DAO Pattern**: Abstracción de acceso a datos
3. **Prepared Statements**: Prevención de SQL Injection
4. **JWT Tokens**: Autenticación moderna y segura
5. **Responsive Design**: Compatible con dispositivos móviles
6. **AJAX**: Experiencia de usuario fluida
7. **Validaciones**: Cliente y servidor
8. **Logging**: Manejo de errores detallado

## 📞 **Soporte**

Para soporte técnico o consultas sobre el proyecto:
- Revisar la documentación en el código
- Verificar los logs de Tomcat para errores
- Consultar la estructura de base de datos

---

**PuntoVenta v1.0.0** - Sistema de Gestión Comercial Completo
Desarrollado con ❤️ usando Java Web Technologies
