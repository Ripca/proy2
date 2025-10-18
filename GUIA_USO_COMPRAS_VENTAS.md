# GUÍA DE USO - COMPRAS Y VENTAS

## ACCESO A LAS PÁGINAS

### Compras
- **URL**: `http://localhost:8080/SistemaEmpresa/CompraServlet`
- **Botón**: Menú → Compras → Nueva Compra

### Ventas
- **URL**: `http://localhost:8080/SistemaEmpresa/VentaServlet`
- **Botón**: Menú → Ventas → Nueva Venta

## CREAR UNA COMPRA

### Paso 1: Seleccionar Proveedor
1. En el campo "Proveedor", ingrese el NIT del proveedor
2. Haga clic en el botón "Buscar" o presione Enter
3. El sistema mostrará:
   - ID del proveedor (oculto)
   - Nombre del proveedor
   - Teléfono del proveedor

### Paso 2: Datos de la Compra
1. Ingrese el **No. Orden** (número único de la orden)
2. Seleccione la **Fecha Orden** (fecha de la compra)
3. El teléfono del proveedor se muestra automáticamente

### Paso 3: Agregar Productos
1. En el campo "Buscar Producto", ingrese:
   - Código del producto, O
   - Nombre del producto
2. Haga clic en "Buscar" o presione Enter
3. El producto se agregará a la tabla con:
   - Nombre del producto
   - Cantidad (editable, por defecto 1)
   - Precio de costo
   - Subtotal (se calcula automáticamente)

### Paso 4: Editar Cantidades
1. Haga clic en el campo de cantidad
2. Modifique el valor
3. El subtotal se actualiza automáticamente
4. Los totales se recalculan

### Paso 5: Eliminar Productos
1. Haga clic en el botón "Eliminar" (papelera) en la fila del producto
2. El producto se elimina de la tabla
3. Los totales se recalculan

### Paso 6: Guardar Compra
1. Verifique que:
   - Haya un proveedor seleccionado
   - Haya al menos un producto
   - Las cantidades sean correctas
2. Haga clic en "Guardar Compra"
3. El sistema:
   - Crea la compra en la BD
   - Crea los detalles de compra
   - Actualiza las existencias de productos (suma)
   - Muestra mensaje de éxito

## CREAR UNA VENTA

### Paso 1: Seleccionar Cliente
1. En el campo "Cliente", ingrese el NIT del cliente
2. Haga clic en el botón "Buscar" o presione Enter
3. El sistema mostrará:
   - ID del cliente (oculto)
   - Nombre del cliente
   - Teléfono del cliente

### Paso 2: Datos de la Venta
1. Ingrese el **No. Factura** (número único de la factura)
2. Ingrese la **Serie** (serie de facturación, ej: A, B, C)
3. Seleccione la **Fecha Venta** (fecha de la venta)
4. El teléfono del cliente se muestra automáticamente

### Paso 3: Agregar Productos
1. En el campo "Buscar Producto", ingrese:
   - Código del producto, O
   - Nombre del producto
2. Haga clic en "Buscar" o presione Enter
3. El producto se agregará a la tabla con:
   - Nombre del producto
   - Cantidad (editable, por defecto 1)
   - Precio de venta
   - Subtotal (se calcula automáticamente)

### Paso 4: Editar Cantidades
1. Haga clic en el campo de cantidad
2. Modifique el valor
3. El subtotal se actualiza automáticamente
4. Los totales se recalculan

### Paso 5: Eliminar Productos
1. Haga clic en el botón "Eliminar" (papelera) en la fila del producto
2. El producto se elimina de la tabla
3. Los totales se recalculan

### Paso 6: Guardar Venta
1. Verifique que:
   - Haya un cliente seleccionado
   - Haya al menos un producto
   - Las cantidades sean correctas
2. Haga clic en "Guardar Venta"
3. El sistema:
   - Crea la venta en la BD
   - Crea los detalles de venta
   - Actualiza las existencias de productos (resta)
   - Muestra mensaje de éxito

## VALIDACIONES

### Compras
- ❌ No se puede guardar sin proveedor
- ❌ No se puede guardar sin productos
- ❌ No se puede guardar sin No. Orden
- ❌ No se puede guardar sin Fecha

### Ventas
- ❌ No se puede guardar sin cliente
- ❌ No se puede guardar sin productos
- ❌ No se puede guardar sin No. Factura
- ❌ No se puede guardar sin Serie
- ❌ No se puede guardar sin Fecha

## CÁLCULOS AUTOMÁTICOS

### Compras
- **Subtotal**: Suma de (Cantidad × Precio Costo) de todos los productos
- **Descuento**: 0.00 (no aplica en esta versión)
- **Total**: Subtotal - Descuento

### Ventas
- **Subtotal**: Suma de (Cantidad × Precio Venta) de todos los productos
- **Descuento**: 0.00 (no aplica en esta versión)
- **Total**: Subtotal - Descuento

## ACTUALIZACIÓN DE EXISTENCIAS

### Compras
- Al guardar una compra, las existencias de los productos **AUMENTAN**
- Ejemplo: Si compra 10 unidades de un producto, su existencia aumenta en 10

### Ventas
- Al guardar una venta, las existencias de los productos **DISMINUYEN**
- Ejemplo: Si vende 5 unidades de un producto, su existencia disminuye en 5

## MENSAJES DEL SISTEMA

### Éxito
- ✅ "Compra creada exitosamente"
- ✅ "Venta creada exitosamente"

### Errores
- ❌ "Debe seleccionar un proveedor/cliente"
- ❌ "Debe agregar al menos un producto"
- ❌ "Proveedor/Cliente no encontrado"
- ❌ "Producto no encontrado"
- ❌ "Error al buscar"

## NOTAS IMPORTANTES

1. **Búsqueda de Proveedor/Cliente**: Se realiza por NIT exacto
2. **Búsqueda de Producto**: Se realiza por código o nombre (parcial)
3. **Cantidades**: Deben ser números positivos mayores a 0
4. **Precios**: Se obtienen automáticamente de la BD
5. **Existencias**: Se actualizan automáticamente al guardar
6. **Transacciones**: Si hay error, se revierte todo (rollback)

## SOPORTE

Si encuentra algún error:
1. Verifique que los datos sean correctos
2. Revise que el proveedor/cliente exista
3. Revise que el producto exista
4. Contacte al administrador del sistema

