# Menú Dinámico - Sistema Empresa

## 📋 Descripción

Implementación de un **menú dinámico con estructura de árbol** que se genera automáticamente desde la base de datos, siguiendo la estructura jerárquica solicitada:

```
1. Productos
   1.1. Marcas
   1.2. Gestión de Productos

2. Ventas  
   2.1. Clientes
   2.2. Empleados
        2.2.1. Puestos
        2.2.2. Gestión de Empleados
   2.3. Gestión de Ventas

3. Compras
   3.1. Proveedores  
   3.2. Gestión de Compras

4. Reportes
   4.1. Reporte de Ventas
   4.2. Reporte de Compras
   4.3. Reporte de Inventario
```

## 🚀 Instalación

### 1. Ejecutar el script de base de datos

```sql
-- Ejecutar en MySQL
source SistemaEmpresa/database/instalar_menu_dinamico.sql;
```

O ejecutar manualmente:
```bash
mysql -u root -p sistema_empresa < SistemaEmpresa/database/instalar_menu_dinamico.sql
```

### 2. Compilar y desplegar la aplicación

Los siguientes archivos han sido creados/modificados:

**Nuevos archivos:**
- `MenuItem.java` - Modelo del menú
- `MenuItemDAO.java` - DAO para operaciones de menú
- `MenuServlet.java` - Servlet para manejar el menú
- `menu_sidebar.jsp` - Componente del menú para sidebar
- `menu_dinamico.jsp` - Componente del menú para navbar
- `menu_test.jsp` - Página de prueba del menú

**Archivos modificados:**
- `template.jsp` - Actualizado para usar el menú dinámico
- `web.xml` - Agregado MenuServlet

## 🔧 Características

### ✅ Funcionalidades Implementadas

- **Estructura jerárquica** de hasta 3 niveles
- **Almacenamiento en BD** con tabla `MenuItems`
- **Ordenamiento automático** por campo `orden`
- **Iconos personalizables** con Font Awesome
- **URLs dinámicas** configurables
- **Estado activo/inactivo** para cada elemento
- **Responsive design** compatible con móviles
- **Navegación intuitiva** con collapse/expand
- **Indicador visual** del elemento activo

### 🎨 Componentes del Sistema

1. **Modelo (`MenuItem.java`)**
   - Propiedades: id, titulo, url, icono, padre_id, orden, activo
   - Métodos: tieneHijos(), esElementoPadre(), agregarHijo()

2. **DAO (`MenuItemDAO.java`)**
   - `obtenerMenuEstructurado()` - Construye árbol completo
   - `obtenerElementosRaiz()` - Solo elementos nivel 1
   - `obtenerHijos(padreId)` - Elementos hijos específicos
   - `insertar(MenuItem)` - Agregar nuevos elementos

3. **Servlet (`MenuServlet.java`)**
   - Endpoint: `/MenuServlet`
   - Acciones: `getMenu`, `getMenuJson`
   - Soporte para AJAX y JSP

4. **Vistas JSP**
   - `menu_sidebar.jsp` - Menú lateral con collapse
   - `menu_dinamico.jsp` - Menú superior con dropdowns
   - `menu_test.jsp` - Página de prueba y visualización

## 📊 Estructura de Base de Datos

```sql
CREATE TABLE MenuItems (
    idMenuItem INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    url VARCHAR(100),
    icono VARCHAR(50),
    padre_id INT NULL,
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (padre_id) REFERENCES MenuItems(idMenuItem) ON DELETE CASCADE
);
```

## 🧪 Pruebas

### Acceder a la página de prueba:
```
http://localhost:8080/SistemaEmpresa/MenuServlet?action=getMenu
```

### Obtener menú en JSON:
```
http://localhost:8080/SistemaEmpresa/MenuServlet?action=getMenuJson
```

## 🔄 Mantenimiento

### Agregar nuevos elementos del menú:

```sql
-- Ejemplo: Agregar "Configuración" como elemento principal
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo) 
VALUES ('Configuración', 'ConfigServlet', 'fas fa-cog', NULL, 5, TRUE);

-- Ejemplo: Agregar subelemento bajo "Configuración"  
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo)
VALUES ('Usuarios', 'UsuarioServlet', 'fas fa-users-cog', 
        (SELECT idMenuItem FROM MenuItems WHERE titulo = 'Configuración'), 1, TRUE);
```

### Desactivar elementos:

```sql
-- Desactivar un elemento sin eliminarlo
UPDATE MenuItems SET activo = FALSE WHERE titulo = 'Elemento a desactivar';
```

### Reordenar elementos:

```sql
-- Cambiar el orden de los elementos
UPDATE MenuItems SET orden = 1 WHERE titulo = 'Productos';
UPDATE MenuItems SET orden = 2 WHERE titulo = 'Ventas';
UPDATE MenuItems SET orden = 3 WHERE titulo = 'Compras';
UPDATE MenuItems SET orden = 4 WHERE titulo = 'Reportes';
```

## 🎯 Próximos Pasos

1. **Compilar** la aplicación con los nuevos archivos
2. **Ejecutar** el script de instalación del menú
3. **Probar** la navegación en el sistema
4. **Personalizar** iconos y URLs según necesidades
5. **Agregar** nuevos elementos del menú según requerimientos

## 📝 Notas Importantes

- El menú se carga automáticamente en cada página que use `template.jsp`
- Los elementos sin URL actúan como contenedores (solo agrupan)
- El orden se respeta automáticamente en la visualización
- Compatible con el sistema de autenticación JWT existente
- Mantiene el diseño responsive del sistema actual
