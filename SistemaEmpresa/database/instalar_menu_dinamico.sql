-- Script completo para instalar el menú dinámico
-- Ejecutar este script después de la instalación base del sistema
-- Autor: Sistema Empresa
-- Fecha: 2025-10-11

USE sistema_empresa;

-- Verificar si la tabla ya existe
DROP TABLE IF EXISTS MenuItems;

-- Crear tabla para el menú dinámico
CREATE TABLE MenuItems (
    idMenuItem INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(50) NOT NULL,
    url VARCHAR(100),
    icono VARCHAR(50),
    padre_id INT NULL,
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (padre_id) REFERENCES MenuItems(idMenuItem) ON DELETE CASCADE,
    INDEX idx_padre_orden (padre_id, orden)
);

-- Insertar elementos del menú principal según la estructura solicitada
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo) VALUES
-- 1. Productos (Nivel 1)
('Productos', NULL, 'fas fa-box', NULL, 1, TRUE),

-- 2. Ventas (Nivel 1)  
('Ventas', NULL, 'fas fa-shopping-cart', NULL, 2, TRUE),

-- 3. Compras (Nivel 1)
('Compras', NULL, 'fas fa-truck', NULL, 3, TRUE),

-- 4. Reportes (Nivel 1)
('Reportes', NULL, 'fas fa-chart-bar', NULL, 4, TRUE);

-- Insertar elementos de segundo nivel
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo) VALUES
-- 1.1. Marcas (bajo Productos)
('Marcas', 'MarcaServlet', 'fas fa-tags', 1, 1, TRUE),

-- 2.1. Clientes (bajo Ventas)
('Clientes', 'ClienteServlet', 'fas fa-users', 2, 1, TRUE),

-- 2.2. Empleados (bajo Ventas) - Este será padre de Puestos
('Empleados', NULL, 'fas fa-user-tie', 2, 2, TRUE),

-- 3.1. Proveedores (bajo Compras)
('Proveedores', 'ProveedorServlet', 'fas fa-building', 3, 1, TRUE);

-- Insertar elementos de tercer nivel
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo) VALUES
-- 2.2.1. Puestos (bajo Empleados)
('Puestos', 'PuestoServlet', 'fas fa-briefcase', 7, 1, TRUE);

-- Insertar elementos adicionales del sistema
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo) VALUES
-- Productos adicionales
('Gestión de Productos', 'ProductoServlet', 'fas fa-boxes', 1, 2, TRUE),

-- Ventas adicionales  
('Gestión de Ventas', 'VentaServlet', 'fas fa-cash-register', 2, 3, TRUE),
('Gestión de Empleados', 'EmpleadoServlet', 'fas fa-users-cog', 7, 2, TRUE),

-- Compras adicionales
('Gestión de Compras', 'CompraServlet', 'fas fa-shopping-basket', 3, 2, TRUE),

-- Reportes específicos
('Reporte de Ventas', 'ReporteServlet?tipo=ventas', 'fas fa-chart-line', 4, 1, TRUE),
('Reporte de Compras', 'ReporteServlet?tipo=compras', 'fas fa-chart-area', 4, 2, TRUE),
('Reporte de Inventario', 'ReporteServlet?tipo=inventario', 'fas fa-warehouse', 4, 3, TRUE);

-- Verificar la estructura creada
SELECT 
    m1.titulo as 'Nivel 1',
    m2.titulo as 'Nivel 2', 
    m3.titulo as 'Nivel 3',
    COALESCE(m3.url, m2.url, m1.url) as 'URL'
FROM MenuItems m1
LEFT JOIN MenuItems m2 ON m2.padre_id = m1.idMenuItem
LEFT JOIN MenuItems m3 ON m3.padre_id = m2.idMenuItem
WHERE m1.padre_id IS NULL
ORDER BY m1.orden, m2.orden, m3.orden;

COMMIT;

-- Mensaje de confirmación
SELECT 'Menú dinámico instalado correctamente' as 'Estado';
SELECT COUNT(*) as 'Total de elementos del menú' FROM MenuItems WHERE activo = TRUE;
