-- Script para crear las tablas de Ventas y Compras con sus detalles
-- Sistema Empresa - Módulos de Ventas y Compras

USE sistemaempresa;

-- =====================================================
-- TABLA: ventas
-- =====================================================
CREATE TABLE IF NOT EXISTS ventas (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    no_factura INT NOT NULL,
    serie VARCHAR(10) NOT NULL,
    fecha_factura DATE NOT NULL,
    id_cliente INT NOT NULL,
    id_empleado INT NOT NULL,
    fecha_ingreso DATE NOT NULL DEFAULT (CURRENT_DATE),
    
    -- Índices para mejorar rendimiento
    INDEX idx_ventas_cliente (id_cliente),
    INDEX idx_ventas_empleado (id_empleado),
    INDEX idx_ventas_fecha (fecha_factura),
    INDEX idx_ventas_factura (no_factura, serie),
    
    -- Claves foráneas
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =====================================================
-- TABLA: ventas_detalle
-- =====================================================
CREATE TABLE IF NOT EXISTS ventas_detalle (
    id_venta_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
    
    -- Índices para mejorar rendimiento
    INDEX idx_ventas_detalle_venta (id_venta),
    INDEX idx_ventas_detalle_producto (id_producto),
    
    -- Claves foráneas
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =====================================================
-- TABLA: compras
-- =====================================================
CREATE TABLE IF NOT EXISTS compras (
    id_compra INT AUTO_INCREMENT PRIMARY KEY,
    no_orden_compra INT NOT NULL,
    fecha_orden DATE NOT NULL,
    fecha_ingreso DATE NOT NULL DEFAULT (CURRENT_DATE),
    id_proveedor INT NOT NULL,
    
    -- Índices para mejorar rendimiento
    INDEX idx_compras_proveedor (id_proveedor),
    INDEX idx_compras_fecha (fecha_orden),
    INDEX idx_compras_orden (no_orden_compra),
    
    -- Claves foráneas
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id_proveedor) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =====================================================
-- TABLA: compras_detalle
-- =====================================================
CREATE TABLE IF NOT EXISTS compras_detalle (
    id_compra_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_compra INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    precio_costo_unitario DECIMAL(10,2) NOT NULL CHECK (precio_costo_unitario >= 0),
    
    -- Índices para mejorar rendimiento
    INDEX idx_compras_detalle_compra (id_compra),
    INDEX idx_compras_detalle_producto (id_producto),
    
    -- Claves foráneas
    FOREIGN KEY (id_compra) REFERENCES compras(id_compra) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- =====================================================
-- DATOS DE EJEMPLO PARA VENTAS
-- =====================================================

-- Insertar algunas ventas de ejemplo
INSERT INTO ventas (no_factura, serie, fecha_factura, id_cliente, id_empleado, fecha_ingreso) VALUES
(1001, 'A', '2024-01-15', 1, 1, '2024-01-15'),
(1002, 'A', '2024-01-16', 2, 1, '2024-01-16'),
(1003, 'B', '2024-01-17', 1, 2, '2024-01-17');

-- Insertar detalles de ventas de ejemplo
INSERT INTO ventas_detalle (id_venta, id_producto, cantidad, precio_unitario) VALUES
-- Venta 1
(1, 1, 2, 15.50),
(1, 2, 1, 25.00),
-- Venta 2
(2, 1, 3, 15.50),
(2, 3, 2, 8.75),
-- Venta 3
(3, 2, 1, 25.00),
(3, 3, 4, 8.75);

-- =====================================================
-- DATOS DE EJEMPLO PARA COMPRAS
-- =====================================================

-- Insertar algunas compras de ejemplo
INSERT INTO compras (no_orden_compra, fecha_orden, fecha_ingreso, id_proveedor) VALUES
(2001, '2024-01-10', '2024-01-10', 1),
(2002, '2024-01-12', '2024-01-12', 2),
(2003, '2024-01-14', '2024-01-14', 1);

-- Insertar detalles de compras de ejemplo
INSERT INTO compras_detalle (id_compra, id_producto, cantidad, precio_costo_unitario) VALUES
-- Compra 1
(1, 1, 50, 12.00),
(1, 2, 30, 20.00),
-- Compra 2
(2, 1, 25, 12.00),
(2, 3, 100, 6.50),
-- Compra 3
(3, 2, 20, 20.00),
(3, 3, 50, 6.50);

-- =====================================================
-- ACTUALIZAR MENÚS PARA INCLUIR VENTAS Y COMPRAS
-- =====================================================

-- Agregar menús para Ventas y Compras
INSERT INTO menus (nombre, url, icono, orden, activo, id_padre) VALUES
('Ventas', '#', 'fas fa-shopping-cart', 6, 1, NULL),
('Compras', '#', 'fas fa-shopping-bag', 7, 1, NULL);

-- Obtener los IDs de los menús padre recién creados
SET @id_ventas = LAST_INSERT_ID() - 1;
SET @id_compras = LAST_INSERT_ID();

-- Agregar submenús para Ventas
INSERT INTO menus (nombre, url, icono, orden, activo, id_padre) VALUES
('Lista de Ventas', 'VentaServlet', 'fas fa-list', 1, 1, @id_ventas),
('Nueva Venta', 'VentaServlet?action=new', 'fas fa-plus', 2, 1, @id_ventas);

-- Agregar submenús para Compras
INSERT INTO menus (nombre, url, icono, orden, activo, id_padre) VALUES
('Lista de Compras', 'CompraServlet', 'fas fa-list', 1, 1, @id_compras),
('Nueva Compra', 'CompraServlet?action=new', 'fas fa-plus', 2, 1, @id_compras);

-- =====================================================
-- VISTAS ÚTILES PARA REPORTES
-- =====================================================

-- Vista para resumen de ventas
CREATE OR REPLACE VIEW vista_resumen_ventas AS
SELECT 
    v.id_venta,
    v.no_factura,
    v.serie,
    v.fecha_factura,
    c.cliente,
    CONCAT(e.nombres, ' ', e.apellidos) as empleado,
    SUM(vd.cantidad * vd.precio_unitario) as total_venta,
    COUNT(vd.id_venta_detalle) as items_vendidos
FROM ventas v
LEFT JOIN clientes c ON v.id_cliente = c.id_cliente
LEFT JOIN empleados e ON v.id_empleado = e.id_empleado
LEFT JOIN ventas_detalle vd ON v.id_venta = vd.id_venta
GROUP BY v.id_venta, v.no_factura, v.serie, v.fecha_factura, c.cliente, e.nombres, e.apellidos;

-- Vista para resumen de compras
CREATE OR REPLACE VIEW vista_resumen_compras AS
SELECT 
    c.id_compra,
    c.no_orden_compra,
    c.fecha_orden,
    p.proveedor,
    SUM(cd.cantidad * cd.precio_costo_unitario) as total_compra,
    COUNT(cd.id_compra_detalle) as items_comprados
FROM compras c
LEFT JOIN proveedores p ON c.id_proveedor = p.id_proveedor
LEFT JOIN compras_detalle cd ON c.id_compra = cd.id_compra
GROUP BY c.id_compra, c.no_orden_compra, c.fecha_orden, p.proveedor;

-- =====================================================
-- COMENTARIOS FINALES
-- =====================================================

/*
TABLAS CREADAS:
1. ventas - Tabla maestra de ventas
2. ventas_detalle - Detalles de cada venta (productos vendidos)
3. compras - Tabla maestra de compras
4. compras_detalle - Detalles de cada compra (productos comprados)

CARACTERÍSTICAS:
- Relaciones maestro-detalle implementadas
- Claves foráneas con restricciones apropiadas
- Índices para mejorar rendimiento
- Datos de ejemplo para pruebas
- Menús actualizados en la base de datos
- Vistas para reportes

PRÓXIMOS PASOS:
1. Ejecutar este script en la base de datos
2. Probar los módulos de Ventas y Compras
3. Verificar que los menús aparezcan correctamente
4. Validar las operaciones CRUD
*/
