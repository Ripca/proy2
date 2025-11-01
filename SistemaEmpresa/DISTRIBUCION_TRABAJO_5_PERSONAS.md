# 📋 DISTRIBUCIÓN DEL TRABAJO - 5 PERSONAS

## 👥 EQUIPO DE DESARROLLO

| Persona | Responsabilidad |
|---------|-----------------|
| **Ricardo** | Configuración Inicial y Base |
| **Said** | Módulo de Productos |
| **Brayan** | Módulo de Ventas |
| **Anderson** | Módulo de Compras |
| **Francisco** | Módulo de Reportes y Extras |

---

## 🚀 PERSONA 1: RICARDO - CONFIGURACIÓN INICIAL Y BASE

**Responsabilidad**: Crear la estructura base del proyecto, configurar BD, autenticación y dashboard.

### Archivos y Carpetas a Subir:

#### 1. **Estructura de Carpetas Base**
```
SistemaEmpresa/
├── src/java/com/sistemaempresa/
│   ├── config/
│   ├── dao/
│   ├── models/
│   ├── servlets/
│   ├── filters/
│   └── utils/
├── web/
│   ├── WEB-INF/
│   │   ├── web.xml
│   │   └── views/
│   ├── assets/
│   │   ├── css/
│   │   ├── js/
│   │   └── productos/
│   ├── error404.jsp
│   ├── error500.jsp
│   └── index.jsp
├── database/
└── build.xml
```

#### 2. **Archivos de Configuración**
- `build.xml` - Configuración de compilación
- `web/WEB-INF/web.xml` - Configuración de la aplicación
- `web/error404.jsp` - Página de error 404
- `web/error500.jsp` - Página de error 500

#### 3. **Base de Datos (Scripts SQL)**
- `database/sistema_empresa.sql` - Schema principal con todas las tablas
- `database/crear_roles_y_permisos.sql` - Tablas de RBAC (roles, usuario_rol, rol_menu, menus)
- `database/usuarios.sql` - Datos iniciales de usuarios y roles

#### 4. **Clases de Configuración**
- `src/java/com/sistemaempresa/config/DatabaseConnection.java` - Conexión a BD

#### 5. **Modelos Base**
- `src/java/com/sistemaempresa/models/Usuario.java`
- `src/java/com/sistemaempresa/models/Rol.java`
- `src/java/com/sistemaempresa/models/Menu.java`

#### 6. **DAOs Base**
- `src/java/com/sistemaempresa/dao/UsuarioDAO.java` - Autenticación
- `src/java/com/sistemaempresa/dao/RolDAO.java` - Gestión de roles
- `src/java/com/sistemaempresa/dao/MenuDAO.java` - Menús dinámicos
- `src/java/com/sistemaempresa/dao/UsuarioRolDAO.java` - Relación usuario-rol
- `src/java/com/sistemaempresa/dao/RolMenuDAO.java` - Relación rol-menu

#### 7. **Utilidades de Seguridad**
- `src/java/com/sistemaempresa/utils/JWTUtil.java` - Generación y validación JWT
- `src/java/com/sistemaempresa/utils/SimpleJSONUtil.java` - Procesamiento JSON

#### 8. **Filtros de Seguridad**
- `src/java/com/sistemaempresa/filters/AuthenticationFilter.java` - Validación de sesión
- `src/java/com/sistemaempresa/filters/JWTAuthFilter.java` - Validación JWT
- `src/java/com/sistemaempresa/filters/CharacterEncodingFilter.java` - Encoding UTF-8
- `src/java/com/sistemaempresa/filters/CORSFilter.java` - CORS

#### 9. **Servlets de Autenticación**
- `src/java/com/sistemaempresa/servlets/LoginServlet.java` - Login/Logout
- `src/java/com/sistemaempresa/servlets/ValidateTokenServlet.java` - Validación JWT

#### 10. **Servlet de Dashboard**
- `src/java/com/sistemaempresa/servlets/DashboardServlet.java` - Dashboard principal

#### 11. **Vistas JSP Base**
- `web/index.jsp` - Página de login
- `web/WEB-INF/views/login.jsp` - Formulario de login
- `web/WEB-INF/views/dashboard.jsp` - Dashboard principal
- `web/WEB-INF/views/layout/header.jsp` - Header con menú dinámico
- `web/WEB-INF/views/layout/footer.jsp` - Footer
- `web/WEB-INF/views/layout/sidebar.jsp` - Sidebar con menú

#### 12. **Archivos CSS y JS Base**
- `web/assets/css/style.css` - Estilos principales
- `web/assets/css/bootstrap.min.css` - Bootstrap 5
- `web/assets/js/main.js` - Scripts principales
- `web/assets/js/jquery.min.js` - jQuery
- `web/assets/js/bootstrap.bundle.min.js` - Bootstrap JS

#### 13. **Librerías JAR** (Copiar a raíz del proyecto)
- `mysql-connector-j-9.4.0.jar`
- `jakarta.servlet.jsp.jstl-api-3.0.1.jar`
- `jakarta.servlet.jsp.jstl-3.0.1.jar`
- `jackson-core-3.0.0.jar`
- `jackson-databind-3.0.0.jar`
- `jackson-annotations-2.20.jar`
- `slf4j-api-2.0.10.jar`
- `slf4j-simple-2.0.10.jar`

#### 14. **Ejemplo CRUD: Módulo de Marcas** (Para referencia)
- `src/java/com/sistemaempresa/models/Marca.java`
- `src/java/com/sistemaempresa/dao/MarcaDAO.java`
- `src/java/com/sistemaempresa/servlets/MarcaServlet.java`
- `web/WEB-INF/views/marcas/list_content.jsp`
- `web/WEB-INF/views/marcas/form_content.jsp`

#### 15. **Ejemplo de Reporte** (Para referencia)
- `src/java/com/sistemaempresa/servlets/ReporteServlet.java`
- `web/WEB-INF/reportes/reporte_ejemplo.jrxml`

### Tareas de Ricardo:
1. ✅ Crear estructura de carpetas
2. ✅ Configurar BD (crear todas las tablas)
3. ✅ Implementar autenticación JWT
4. ✅ Crear filtros de seguridad
5. ✅ Implementar menús dinámicos por rol
6. ✅ Crear dashboard principal
7. ✅ Crear ejemplo CRUD (Marcas)
8. ✅ Crear ejemplo de reporte
9. ✅ Documentar estructura para los demás

**Tiempo estimado**: 5-7 días

---

## 📦 PERSONA 2: SAID - MÓDULO DE PRODUCTOS

**Responsabilidad**: Crear CRUD completo de Productos con carga de imágenes.

### Archivos a Subir:

#### 1. **Modelo**
- `src/java/com/sistemaempresa/models/Producto.java`

#### 2. **DAO**
- `src/java/com/sistemaempresa/dao/ProductoDAO.java`

#### 3. **Servlets**
- `src/java/com/sistemaempresa/servlets/ProductoServlet.java`
- `src/java/com/sistemaempresa/servlets/UploadImagenServlet.java`

#### 4. **Vistas JSP**
- `web/WEB-INF/views/productos/list_content.jsp` - Listado de productos
- `web/WEB-INF/views/productos/form_content.jsp` - Formulario (crear/editar)

#### 5. **Scripts SQL**
- `database/agregar_columna_imagen_productos.sql` - Migración para imagen

#### 6. **Carpeta de Imágenes**
- `web/assets/productos/` - Carpeta para almacenar imágenes

### Funcionalidades:
- ✅ Listar productos
- ✅ Crear producto
- ✅ Editar producto
- ✅ Eliminar producto
- ✅ Cargar imagen
- ✅ Mostrar imagen en listado y formulario
- ✅ Validaciones (precio, existencia, etc.)

**Tiempo estimado**: 3-4 días

---

## 💰 PERSONA 3: BRAYAN - MÓDULO DE VENTAS

**Responsabilidad**: Crear CRUD de Ventas con patrón maestro-detalle.

### Archivos a Subir:

#### 1. **Modelos**
- `src/java/com/sistemaempresa/models/Venta.java`
- `src/java/com/sistemaempresa/models/VentaDetalle.java`
- `src/java/com/sistemaempresa/models/Cliente.java`

#### 2. **DAOs**
- `src/java/com/sistemaempresa/dao/VentaDAO.java` - Maestro-detalle con transacciones
- `src/java/com/sistemaempresa/dao/ClienteDAO.java`

#### 3. **Servlets**
- `src/java/com/sistemaempresa/servlets/VentaServlet.java`
- `src/java/com/sistemaempresa/servlets/ClienteServlet.java`

#### 4. **Vistas JSP**
- `web/WEB-INF/views/ventas/list_content.jsp` - Listado de ventas
- `web/WEB-INF/views/ventas/form_content.jsp` - Formulario (crear/editar con detalles)
- `web/WEB-INF/views/clientes/list_content.jsp` - Listado de clientes
- `web/WEB-INF/views/clientes/form_content.jsp` - Formulario de clientes

#### 5. **Scripts SQL**
- `database/ventas_compras_tables.sql` - Tablas de ventas y detalles

### Funcionalidades:
- ✅ Listar ventas
- ✅ Crear venta (maestro + detalles)
- ✅ Editar venta (actualizar detalles)
- ✅ Eliminar venta
- ✅ Agregar/quitar productos en detalle
- ✅ Actualizar existencias (restar en productos)
- ✅ Transacciones ACID
- ✅ Validaciones

**Tiempo estimado**: 4-5 días

---

## 📥 PERSONA 4: ANDERSON - MÓDULO DE COMPRAS

**Responsabilidad**: Crear CRUD de Compras con patrón maestro-detalle.

### Archivos a Subir:

#### 1. **Modelos**
- `src/java/com/sistemaempresa/models/Compra.java`
- `src/java/com/sistemaempresa/models/CompraDetalle.java`
- `src/java/com/sistemaempresa/models/Proveedor.java`

#### 2. **DAOs**
- `src/java/com/sistemaempresa/dao/CompraDAO.java` - Maestro-detalle con transacciones
- `src/java/com/sistemaempresa/dao/ProveedorDAO.java`

#### 3. **Servlets**
- `src/java/com/sistemaempresa/servlets/CompraServlet.java`
- `src/java/com/sistemaempresa/servlets/ProveedorServlet.java`

#### 4. **Vistas JSP**
- `web/WEB-INF/views/compras/list_content.jsp` - Listado de compras
- `web/WEB-INF/views/compras/form_content.jsp` - Formulario (crear/editar con detalles)
- `web/WEB-INF/views/proveedores/list_content.jsp` - Listado de proveedores
- `web/WEB-INF/views/proveedores/form_content.jsp` - Formulario de proveedores

### Funcionalidades:
- ✅ Listar compras
- ✅ Crear compra (maestro + detalles)
- ✅ Editar compra (actualizar detalles)
- ✅ Eliminar compra
- ✅ Agregar/quitar productos en detalle
- ✅ Actualizar existencias (sumar en productos)
- ✅ Transacciones ACID
- ✅ Validaciones

**Tiempo estimado**: 4-5 días

---

## 📊 PERSONA 5: FRANCISCO - REPORTES Y EXTRAS

**Responsabilidad**: Crear reportes y funcionalidades adicionales.

### Archivos a Subir:

#### 1. **Servlets**
- `src/java/com/sistemaempresa/servlets/ReporteServlet.java`
- `src/java/com/sistemaempresa/servlets/ExportarServlet.java`

#### 2. **Reportes JasperReports**
- `web/WEB-INF/reportes/reporte_ventas.jrxml`
- `web/WEB-INF/reportes/reporte_compras.jrxml`
- `web/WEB-INF/reportes/reporte_productos.jrxml`
- `web/WEB-INF/reportes/reporte_existencias.jrxml`

#### 3. **Librerías de Reportes** (Copiar a raíz)
- `jasperreports-6.20.0.jar`
- `commons-beanutils-1.9.4.jar`
- `commons-collections-3.2.1.jar`
- `commons-digester-2.1.jar`
- `commons-logging-1.1.1.jar`
- `itext-2.1.7.jar`

#### 4. **Vistas JSP**
- `web/WEB-INF/views/reportes/list_content.jsp` - Listado de reportes disponibles
- `web/WEB-INF/views/reportes/filtros.jsp` - Filtros para reportes

#### 5. **Modelos Adicionales**
- `src/java/com/sistemaempresa/models/Empleado.java`
- `src/java/com/sistemaempresa/models/Puesto.java`

#### 6. **DAOs Adicionales**
- `src/java/com/sistemaempresa/dao/EmpleadoDAO.java`
- `src/java/com/sistemaempresa/dao/PuestoDAO.java`

#### 7. **Servlets Adicionales**
- `src/java/com/sistemaempresa/servlets/EmpleadoServlet.java`
- `src/java/com/sistemaempresa/servlets/PuestoServlet.java`

#### 8. **Vistas JSP Adicionales**
- `web/WEB-INF/views/empleados/list_content.jsp`
- `web/WEB-INF/views/empleados/form_content.jsp`
- `web/WEB-INF/views/puestos/list_content.jsp`
- `web/WEB-INF/views/puestos/form_content.jsp`

### Funcionalidades:
- ✅ Reporte de Ventas (PDF)
- ✅ Reporte de Compras (PDF)
- ✅ Reporte de Productos (PDF)
- ✅ Reporte de Existencias (PDF)
- ✅ Exportar a Excel
- ✅ CRUD de Empleados
- ✅ CRUD de Puestos
- ✅ Filtros por fecha, producto, etc.

**Tiempo estimado**: 4-5 días

---

## 📅 CRONOGRAMA SUGERIDO

| Semana | Ricardo | Said | Brayan | Anderson | Francisco |
|--------|---------|------|--------|----------|-----------|
| 1 | Configuración inicial | Espera | Espera | Espera | Espera |
| 2 | Finaliza base | Productos | Espera | Espera | Espera |
| 3 | Soporte | Productos | Ventas | Espera | Espera |
| 4 | Soporte | Finaliza | Ventas | Compras | Espera |
| 5 | Soporte | Testing | Finaliza | Compras | Reportes |
| 6 | Integración | Integración | Integración | Finaliza | Reportes |
| 7 | Testing | Testing | Testing | Testing | Finaliza |

---

## 🔄 DEPENDENCIAS

```
Ricardo (Base)
    ↓
    ├─→ Said (Productos)
    ├─→ Brayan (Ventas) → Necesita Productos de Said
    ├─→ Anderson (Compras) → Necesita Productos de Said
    └─→ Francisco (Reportes) → Necesita todo lo anterior
```

---

## ✅ CHECKLIST DE ENTREGA

### Ricardo
- [ ] BD creada y poblada
- [ ] Autenticación JWT funcionando
- [ ] Dashboard con menús dinámicos
- [ ] Ejemplo CRUD (Marcas)
- [ ] Ejemplo Reporte
- [ ] Documentación de estructura

### Said
- [ ] CRUD Productos completo
- [ ] Carga de imágenes funcionando
- [ ] Validaciones implementadas
- [ ] Pruebas unitarias

### Brayan
- [ ] CRUD Ventas completo
- [ ] Maestro-detalle funcionando
- [ ] Actualización de existencias
- [ ] Transacciones ACID
- [ ] Pruebas unitarias

### Anderson
- [ ] CRUD Compras completo
- [ ] Maestro-detalle funcionando
- [ ] Actualización de existencias
- [ ] Transacciones ACID
- [ ] Pruebas unitarias

### Francisco
- [ ] 4 Reportes en PDF
- [ ] Exportación a Excel
- [ ] CRUD Empleados y Puestos
- [ ] Filtros de reportes
- [ ] Pruebas unitarias

---

## 📞 COMUNICACIÓN

- **Reunión diaria**: 15 minutos (estado del proyecto)
- **Reunión de integración**: Cada 2 días
- **Reunión de testing**: Antes de cada entrega
- **Documentación**: Cada persona documenta su módulo

---

**Nota**: Este plan asume que Ricardo termina su parte en 5-7 días. Los demás pueden empezar en paralelo una vez que Ricardo entregue la base.

