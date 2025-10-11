-- Script para crear menú dinámico con estructura de árbol
-- Autor: Sistema Empresa
-- Fecha: 2025-10-11

USE sistema_empresa;

-- Tabla para el menú dinámico
CREATE TABLE IF NOT EXISTS MenuItems (
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

-- Insertar elementos del menú principal
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo) VALUES
-- 1. Productos (Nivel 1)
('Productos', NULL, 'fas fa-box', NULL, 1, TRUE),
    -- 1.1. Marcas (Nivel 2)
    ('Marcas', 'MarcaServlet', 'fas fa-tags', 1, 1, TRUE),

-- 2. Ventas (Nivel 1)
('Ventas', NULL, 'fas fa-shopping-cart', NULL, 2, TRUE),
    -- 2.1. Clientes (Nivel 2)
    ('Clientes', 'ClienteServlet', 'fas fa-users', 2, 1, TRUE),
    -- 2.2. Empleados (Nivel 2)
    ('Empleados', NULL, 'fas fa-user-tie', 2, 2, TRUE),
        -- 2.2.1. Puestos (Nivel 3)
        ('Puestos', 'PuestoServlet', 'fas fa-briefcase', 5, 1, TRUE),

-- 3. Compras (Nivel 1)
('Compras', NULL, 'fas fa-truck', NULL, 3, TRUE),
    -- 3.1. Proveedores (Nivel 2)
    ('Proveedores', 'ProveedorServlet', 'fas fa-building', 7, 1, TRUE),

-- 4. Reportes (Nivel 1)
('Reportes', NULL, 'fas fa-chart-bar', NULL, 4, TRUE);

-- Agregar elementos adicionales del sistema
INSERT INTO MenuItems (titulo, url, icono, padre_id, orden, activo) VALUES
-- Productos adicionales
('Gestión de Productos', 'ProductoServlet', 'fas fa-boxes', 1, 2, TRUE),

-- Ventas adicionales  
('Gestión de Ventas', 'VentaServlet', 'fas fa-cash-register', 2, 3, TRUE),
('Gestión de Empleados', 'EmpleadoServlet', 'fas fa-users-cog', 5, 2, TRUE),

-- Compras adicionales
('Gestión de Compras', 'CompraServlet', 'fas fa-shopping-basket', 7, 2, TRUE),

-- Reportes específicos
('Reporte de Ventas', 'ReporteServlet?tipo=ventas', 'fas fa-chart-line', 9, 1, TRUE),
('Reporte de Compras', 'ReporteServlet?tipo=compras', 'fas fa-chart-area', 9, 2, TRUE),
('Reporte de Inventario', 'ReporteServlet?tipo=inventario', 'fas fa-warehouse', 9, 3, TRUE);

COMMIT;
