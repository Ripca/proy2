# INSTRUCCIONES DE PRUEBA - FUNCIONALIDAD AJAX

## 🎯 OBJETIVO

Verificar que toda la funcionalidad AJAX funciona correctamente en ambas pantallas (Compras y Ventas).

---

## 📋 CHECKLIST DE PRUEBAS

### PANTALLA DE COMPRAS

#### 1. Búsqueda de Proveedor por NIT
- [ ] Ir a Compras → Nueva Compra
- [ ] Ingresa un NIT válido en el campo "Proveedor"
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe mostrar datos del proveedor
- [ ] Ingresa un NIT inválido
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe preguntar si desea crear proveedor
- [ ] Haz clic en "Sí"
- [ ] ✅ Debe redirigir a formulario de crear proveedor

#### 2. Listar Proveedores (Modal)
- [ ] Ir a Compras → Nueva Compra
- [ ] Haz clic en botón "Listar"
- [ ] ✅ Debe abrir modal con tabla de proveedores
- [ ] ✅ Tabla debe mostrar NIT, Nombre, Teléfono
- [ ] Haz clic en "Seleccionar" en una fila
- [ ] ✅ Debe llenar datos del proveedor
- [ ] ✅ Modal debe cerrarse automáticamente

#### 3. Búsqueda de Producto
- [ ] Ir a Compras → Nueva Compra
- [ ] Selecciona un proveedor
- [ ] Ingresa código/nombre de producto válido
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe agregar producto a tabla
- [ ] Ingresa código/nombre inválido
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe preguntar si desea crear producto
- [ ] Haz clic en "Sí"
- [ ] ✅ Debe redirigir a formulario de crear producto

#### 4. Enter en Campos
- [ ] Ir a Compras → Nueva Compra
- [ ] Ingresa NIT en campo "Proveedor"
- [ ] Presiona Enter
- [ ] ✅ Debe buscar proveedor (sin hacer clic)
- [ ] Ingresa código de producto
- [ ] Presiona Enter
- [ ] ✅ Debe buscar producto (sin hacer clic)

---

### PANTALLA DE VENTAS

#### 1. Búsqueda de Cliente por NIT
- [ ] Ir a Ventas → Nueva Venta
- [ ] Ingresa un NIT válido en el campo "Cliente"
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe mostrar datos del cliente
- [ ] Ingresa un NIT inválido
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe preguntar si desea crear cliente
- [ ] Haz clic en "Sí"
- [ ] ✅ Debe redirigir a formulario de crear cliente

#### 2. Listar Clientes (Modal)
- [ ] Ir a Ventas → Nueva Venta
- [ ] Haz clic en botón "Listar"
- [ ] ✅ Debe abrir modal con tabla de clientes
- [ ] ✅ Tabla debe mostrar NIT, Nombres, Apellidos, Teléfono
- [ ] Haz clic en "Seleccionar" en una fila
- [ ] ✅ Debe llenar datos del cliente
- [ ] ✅ Modal debe cerrarse automáticamente

#### 3. Búsqueda de Producto
- [ ] Ir a Ventas → Nueva Venta
- [ ] Selecciona un cliente
- [ ] Ingresa código/nombre de producto válido
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe agregar producto a tabla
- [ ] Ingresa código/nombre inválido
- [ ] Haz clic en botón "Buscar"
- [ ] ✅ Debe preguntar si desea crear producto
- [ ] Haz clic en "Sí"
- [ ] ✅ Debe redirigir a formulario de crear producto

#### 4. Enter en Campos
- [ ] Ir a Ventas → Nueva Venta
- [ ] Ingresa NIT en campo "Cliente"
- [ ] Presiona Enter
- [ ] ✅ Debe buscar cliente (sin hacer clic)
- [ ] Ingresa código de producto
- [ ] Presiona Enter
- [ ] ✅ Debe buscar producto (sin hacer clic)

---

## 🔍 VERIFICACIÓN TÉCNICA

### Consola del Navegador (F12)
- [ ] No debe haber errores JavaScript
- [ ] AJAX requests deben ser exitosas (status 200)
- [ ] JSON responses deben ser válidos

### Network Tab (F12)
- [ ] Verificar que se envíen requests AJAX
- [ ] Verificar que se reciban responses JSON
- [ ] Verificar que no haya errores 404 o 500

### Datos en Base de Datos
- [ ] Verificar que los datos se carguen correctamente
- [ ] Verificar que la búsqueda encuentre registros existentes
- [ ] Verificar que la búsqueda no encuentre registros inexistentes

---

## 🐛 POSIBLES PROBLEMAS Y SOLUCIONES

### Problema: Botón no dispara búsqueda
**Solución**: Verificar que jQuery esté cargado correctamente

### Problema: Modal no abre
**Solución**: Verificar que Bootstrap 5 esté cargado correctamente

### Problema: AJAX retorna error 404
**Solución**: Verificar que los servlets estén correctamente mapeados

### Problema: JSON no se parsea
**Solución**: Verificar que el Content-Type sea application/json

### Problema: Modal no se cierra
**Solución**: Verificar que bootstrap.Modal esté disponible

---

## 📊 RESULTADOS ESPERADOS

### Búsqueda Exitosa
```
Usuario ingresa NIT válido
    ↓
Hace clic en "Buscar"
    ↓
AJAX request enviado
    ↓
Servidor retorna JSON con datos
    ↓
Formulario se llena automáticamente
```

### Búsqueda Fallida
```
Usuario ingresa NIT inválido
    ↓
Hace clic en "Buscar"
    ↓
AJAX request enviado
    ↓
Servidor retorna JSON vacío
    ↓
Sistema pregunta si desea crear
    ↓
Usuario hace clic en "Sí"
    ↓
Redirige a formulario de crear
```

### Modal de Selección
```
Usuario hace clic en "Listar"
    ↓
Modal se abre
    ↓
AJAX carga todos los registros
    ↓
Tabla se llena con datos
    ↓
Usuario selecciona uno
    ↓
Formulario se llena
    ↓
Modal se cierra automáticamente
```

---

## ✅ CRITERIOS DE ÉXITO

- ✅ Todos los botones disparan AJAX
- ✅ Búsqueda encuentra registros existentes
- ✅ Búsqueda pregunta si crear cuando no encuentra
- ✅ Modal carga y muestra todos los registros
- ✅ Selección en modal llena formulario
- ✅ Modal se cierra automáticamente
- ✅ Enter en campos dispara búsqueda
- ✅ No hay errores en consola
- ✅ No hay errores en Network
- ✅ Ambas pantallas funcionan igual

---

## 🚀 PRÓXIMOS PASOS DESPUÉS DE PRUEBAS

1. Si todo funciona → Compilar proyecto
2. Si hay errores → Revisar consola del navegador
3. Si hay errores AJAX → Revisar Network tab
4. Si hay errores de servidor → Revisar logs del servidor

---

**LISTO PARA PROBAR**

