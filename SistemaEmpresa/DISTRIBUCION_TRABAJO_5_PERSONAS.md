# DISTRIBUCION DEL TRABAJO - 5 PERSONAS

## EQUIPO DE DESARROLLO

| Persona | Responsabilidad |
|---------|-----------------|
| Ricardo | Configuracion Inicial y Base |
| Said | Modulo de Productos |
| Brayan | Modulo de Ventas |
| Anderson | Modulo de Compras |
| Francisco | Modulo de Reportes y Extras |

---

## PERSONA 1: RICARDO - CONFIGURACION INICIAL Y BASE

Responsabilidad: Crear la estructura base del proyecto, configurar BD, autenticacion y dashboard.

### Archivos y Carpetas a Subir:

#### 1. Estructura de Carpetas Base
- src/java/com/sistemaempresa/ (config, dao, models, servlets, filters, utils)
- web/WEB-INF/ (web.xml, views)
- web/assets/ (css, js, productos)
- web/ (error404.jsp, error500.jsp, index.jsp)
- database/
- build.xml

#### 2. Archivos de Configuracion
- build.xml
- web/WEB-INF/web.xml
- web/error404.jsp
- web/error500.jsp

#### 3. Base de Datos (Scripts SQL)
- database/sistema_empresa.sql
- database/crear_roles_y_permisos.sql
- database/usuarios.sql

#### 4. Clases de Configuracion
- src/java/com/sistemaempresa/config/DatabaseConnection.java

#### 5. Modelos Base
- src/java/com/sistemaempresa/models/Usuario.java
- src/java/com/sistemaempresa/models/Rol.java
- src/java/com/sistemaempresa/models/Menu.java

#### 6. DAOs Base
- src/java/com/sistemaempresa/dao/UsuarioDAO.java
- src/java/com/sistemaempresa/dao/RolDAO.java
- src/java/com/sistemaempresa/dao/MenuDAO.java
- src/java/com/sistemaempresa/dao/UsuarioRolDAO.java
- src/java/com/sistemaempresa/dao/RolMenuDAO.java

#### 7. Utilidades de Seguridad
- src/java/com/sistemaempresa/utils/JWTUtil.java
- src/java/com/sistemaempresa/utils/SimpleJSONUtil.java

#### 8. Filtros de Seguridad
- src/java/com/sistemaempresa/filters/AuthenticationFilter.java
- src/java/com/sistemaempresa/filters/JWTAuthFilter.java
- src/java/com/sistemaempresa/filters/CharacterEncodingFilter.java
- src/java/com/sistemaempresa/filters/CORSFilter.java

#### 9. Servlets de Autenticacion
- src/java/com/sistemaempresa/servlets/LoginServlet.java
- src/java/com/sistemaempresa/servlets/ValidateTokenServlet.java

#### 10. Servlet de Dashboard
- src/java/com/sistemaempresa/servlets/DashboardServlet.java

#### 11. Vistas JSP Base
- web/index.jsp
- web/WEB-INF/views/login.jsp
- web/WEB-INF/views/dashboard.jsp
- web/WEB-INF/views/layout/header.jsp
- web/WEB-INF/views/layout/footer.jsp
- web/WEB-INF/views/layout/sidebar.jsp

#### 12. Archivos CSS y JS Base
- web/assets/css/style.css
- web/assets/css/bootstrap.min.css
- web/assets/js/main.js
- web/assets/js/jquery.min.js
- web/assets/js/bootstrap.bundle.min.js

#### 13. Librerias JAR (Copiar a raiz del proyecto)
- mysql-connector-j-9.4.0.jar
- jakarta.servlet.jsp.jstl-api-3.0.1.jar
- jakarta.servlet.jsp.jstl-3.0.1.jar
- jackson-core-3.0.0.jar
- jackson-databind-3.0.0.jar
- jackson-annotations-2.20.jar
- slf4j-api-2.0.10.jar
- slf4j-simple-2.0.10.jar

#### 14. Ejemplo CRUD: Modulo de Marcas
- src/java/com/sistemaempresa/models/Marca.java
- src/java/com/sistemaempresa/dao/MarcaDAO.java
- src/java/com/sistemaempresa/servlets/MarcaServlet.java
- web/WEB-INF/views/marcas/list_content.jsp
- web/WEB-INF/views/marcas/form_content.jsp

#### 15. Ejemplo de Reporte
- src/java/com/sistemaempresa/servlets/ReporteServlet.java
- web/WEB-INF/reportes/reporte_ejemplo.jrxml

Tiempo estimado: 5-7 dias

---

## PERSONA 2: SAID - MODULO DE PRODUCTOS

### Archivos a Subir:
- src/java/com/sistemaempresa/models/Producto.java
- src/java/com/sistemaempresa/dao/ProductoDAO.java
- src/java/com/sistemaempresa/servlets/ProductoServlet.java
- src/java/com/sistemaempresa/servlets/UploadImagenServlet.java
- web/WEB-INF/views/productos/list_content.jsp
- web/WEB-INF/views/productos/form_content.jsp
- database/agregar_columna_imagen_productos.sql
- web/assets/productos/ (carpeta)

Funcionalidades: CRUD completo, carga de imagenes, validaciones
Tiempo estimado: 3-4 dias

---

## PERSONA 3: BRAYAN - MODULO DE VENTAS

### Archivos a Subir:
- src/java/com/sistemaempresa/models/Venta.java
- src/java/com/sistemaempresa/models/VentaDetalle.java
- src/java/com/sistemaempresa/models/Cliente.java
- src/java/com/sistemaempresa/dao/VentaDAO.java
- src/java/com/sistemaempresa/dao/ClienteDAO.java
- src/java/com/sistemaempresa/servlets/VentaServlet.java
- src/java/com/sistemaempresa/servlets/ClienteServlet.java
- web/WEB-INF/views/ventas/list_content.jsp
- web/WEB-INF/views/ventas/form_content.jsp
- web/WEB-INF/views/clientes/list_content.jsp
- web/WEB-INF/views/clientes/form_content.jsp
- database/ventas_compras_tables.sql

Funcionalidades: CRUD maestro-detalle, transacciones, actualizacion de existencias
Tiempo estimado: 4-5 dias

---

## PERSONA 4: ANDERSON - MODULO DE COMPRAS

### Archivos a Subir:
- src/java/com/sistemaempresa/models/Compra.java
- src/java/com/sistemaempresa/models/CompraDetalle.java
- src/java/com/sistemaempresa/models/Proveedor.java
- src/java/com/sistemaempresa/dao/CompraDAO.java
- src/java/com/sistemaempresa/dao/ProveedorDAO.java
- src/java/com/sistemaempresa/servlets/CompraServlet.java
- src/java/com/sistemaempresa/servlets/ProveedorServlet.java
- web/WEB-INF/views/compras/list_content.jsp
- web/WEB-INF/views/compras/form_content.jsp
- web/WEB-INF/views/proveedores/list_content.jsp
- web/WEB-INF/views/proveedores/form_content.jsp

Funcionalidades: CRUD maestro-detalle, transacciones, actualizacion de existencias
Tiempo estimado: 4-5 dias

---

## PERSONA 5: FRANCISCO - REPORTES Y EXTRAS

### Archivos a Subir:
- src/java/com/sistemaempresa/servlets/ReporteServlet.java
- src/java/com/sistemaempresa/servlets/ExportarServlet.java
- web/WEB-INF/reportes/reporte_ventas.jrxml
- web/WEB-INF/reportes/reporte_compras.jrxml
- web/WEB-INF/reportes/reporte_productos.jrxml
- web/WEB-INF/reportes/reporte_existencias.jrxml
- jasperreports-6.20.0.jar
- commons-beanutils-1.9.4.jar
- commons-collections-3.2.1.jar
- commons-digester-2.1.jar
- commons-logging-1.1.1.jar
- itext-2.1.7.jar
- web/WEB-INF/views/reportes/list_content.jsp
- web/WEB-INF/views/reportes/filtros.jsp
- src/java/com/sistemaempresa/models/Empleado.java
- src/java/com/sistemaempresa/models/Puesto.java
- src/java/com/sistemaempresa/dao/EmpleadoDAO.java
- src/java/com/sistemaempresa/dao/PuestoDAO.java
- src/java/com/sistemaempresa/servlets/EmpleadoServlet.java
- src/java/com/sistemaempresa/servlets/PuestoServlet.java
- web/WEB-INF/views/empleados/list_content.jsp
- web/WEB-INF/views/empleados/form_content.jsp
- web/WEB-INF/views/puestos/list_content.jsp
- web/WEB-INF/views/puestos/form_content.jsp

Funcionalidades: Reportes PDF, exportacion Excel, CRUD Empleados y Puestos
Tiempo estimado: 4-5 dias

---

## CRONOGRAMA

| Semana | Ricardo | Said | Brayan | Anderson | Francisco |
|--------|---------|------|--------|----------|-----------|
| 1 | Configuracion | Espera | Espera | Espera | Espera |
| 2 | Finaliza | Productos | Espera | Espera | Espera |
| 3 | Soporte | Productos | Ventas | Espera | Espera |
| 4 | Soporte | Finaliza | Ventas | Compras | Espera |
| 5 | Soporte | Testing | Finaliza | Compras | Reportes |
| 6 | Integracion | Integracion | Integracion | Finaliza | Reportes |
| 7 | Testing | Testing | Testing | Testing | Finaliza |

---

DEPENDENCIAS: Ricardo (Base) -> Said, Brayan, Anderson -> Francisco

COMUNICACION: Reunion diaria 15 min, reunion integracion cada 2 dias, testing antes de entrega
