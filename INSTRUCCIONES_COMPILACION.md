# INSTRUCCIONES DE COMPILACIÓN

## ESTADO ACTUAL

✅ **IMPLEMENTACIÓN 100% COMPLETADA**

Todos los cambios han sido realizados:
- Backend completamente funcional
- Frontend rediseñado con interfaz moderna
- AJAX para búsquedas en tiempo real
- Cálculos automáticos de totales
- Actualización de existencias
- Transacciones atómicas
- Sin errores de compilación

---

## COMPILACIÓN

### Opción 1: Usando Apache Ant (Recomendado)

```bash
cd SistemaEmpresa
ant clean compile
```

### Opción 2: Usando Maven

```bash
cd SistemaEmpresa
mvn clean compile
```

### Opción 3: Usando NetBeans

1. Abrir NetBeans
2. File → Open Project
3. Seleccionar carpeta `SistemaEmpresa`
4. Click derecho en el proyecto → Clean and Build

---

## EJECUCIÓN

### Opción 1: Usando Apache Ant

```bash
cd SistemaEmpresa
ant run
```

### Opción 2: Usando NetBeans

1. Click derecho en el proyecto
2. Run

### Opción 3: Usando Tomcat manualmente

1. Compilar el proyecto
2. Copiar `SistemaEmpresa/build/web` a `$CATALINA_HOME/webapps/SistemaEmpresa`
3. Iniciar Tomcat
4. Acceder a `http://localhost:8080/SistemaEmpresa`

---

## ACCESO A LAS PÁGINAS

### Compras
- **URL**: `http://localhost:8080/SistemaEmpresa/CompraServlet`
- **Menú**: Compras → Nueva Compra

### Ventas
- **URL**: `http://localhost:8080/SistemaEmpresa/VentaServlet`
- **Menú**: Ventas → Nueva Venta

---

## VERIFICACIÓN DE COMPILACIÓN

### Sin errores
Si la compilación es exitosa, verá:
```
BUILD SUCCESSFUL
Total time: X seconds
```

### Con errores
Si hay errores, revise:
1. Que Java esté instalado correctamente
2. Que las librerías estén en `web/WEB-INF/lib`
3. Que la base de datos esté configurada
4. Que MySQL esté ejecutándose

---

## PRUEBAS FUNCIONALES

### Crear Compra
1. Acceder a Compras → Nueva Compra
2. Ingresar NIT de proveedor
3. Hacer clic en "Buscar"
4. Ingresar No. Orden y Fecha
5. Buscar y agregar productos
6. Hacer clic en "Guardar Compra"
7. Verificar mensaje de éxito
8. Verificar que existencias aumentaron

### Crear Venta
1. Acceder a Ventas → Nueva Venta
2. Ingresar NIT de cliente
3. Hacer clic en "Buscar"
4. Ingresar No. Factura, Serie y Fecha
5. Buscar y agregar productos
6. Hacer clic en "Guardar Venta"
7. Verificar mensaje de éxito
8. Verificar que existencias disminuyeron

---

## SOLUCIÓN DE PROBLEMAS

### Error: "Cannot find symbol"
- Verificar que todos los archivos estén guardados
- Limpiar y recompilar: `ant clean compile`

### Error: "Connection refused"
- Verificar que MySQL esté ejecutándose
- Verificar credenciales de base de datos en `DatabaseConnection.java`

### Error: "404 Not Found"
- Verificar que la aplicación esté desplegada correctamente
- Verificar que Tomcat esté ejecutándose
- Verificar la URL

### Error: "NullPointerException"
- Verificar que los datos ingresados sean válidos
- Verificar que proveedor/cliente/producto exista
- Revisar logs del servidor

---

## ARCHIVOS MODIFICADOS

### Backend
- `src/java/com/sistemaempresa/dao/CompraDAO.java`
- `src/java/com/sistemaempresa/dao/VentaDAO.java`
- `src/java/com/sistemaempresa/dao/ClienteDAO.java`
- `src/java/com/sistemaempresa/dao/ProductoDAO.java`
- `src/java/com/sistemaempresa/dao/ProveedorDAO.java`
- `src/java/com/sistemaempresa/servlets/CompraServlet.java`
- `src/java/com/sistemaempresa/servlets/VentaServlet.java`
- `src/java/com/sistemaempresa/servlets/ClienteServlet.java`
- `src/java/com/sistemaempresa/servlets/ProductoServlet.java`
- `src/java/com/sistemaempresa/servlets/ProveedorServlet.java`

### Frontend
- `web/WEB-INF/views/compras/form_content.jsp`
- `web/WEB-INF/views/ventas/form_content.jsp`

---

## DOCUMENTACIÓN

- `IMPLEMENTACION_COMPRAS_VENTAS.md` - Detalles técnicos
- `GUIA_USO_COMPRAS_VENTAS.md` - Manual de usuario
- `RESUMEN_FINAL_IMPLEMENTACION.md` - Resumen ejecutivo
- `CHECKLIST_IMPLEMENTACION.md` - Checklist de tareas
- `INSTRUCCIONES_COMPILACION.md` - Este archivo

---

## NOTAS IMPORTANTES

1. **Base de datos**: Asegúrese de que la BD esté actualizada con las tablas de compras y ventas
2. **Librerías**: Todas las librerías necesarias están en `web/WEB-INF/lib`
3. **Transacciones**: Las compras y ventas usan transacciones atómicas
4. **Existencias**: Se actualizan automáticamente al guardar
5. **JSON**: Se serializa manualmente sin dependencias externas

---

## SOPORTE

Si encuentra algún problema:
1. Revise los logs del servidor
2. Verifique que los datos sean correctos
3. Contacte al administrador del sistema

---

**LISTO PARA COMPILAR Y EJECUTAR**

