-- Script para crear la tabla de menús dinámicos
-- Ejecutar este script si la tabla 'menus' no existe

USE sistema_empresa;

-- Crear tabla de menús
CREATE TABLE IF NOT EXISTS menus (
    id_menu INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    icono VARCHAR(50),
    url VARCHAR(100),
    id_padre INT NULL,
    orden INT DEFAULT 0,
    estado BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_padre) REFERENCES menus(id_menu) ON DELETE CASCADE
);

-- Insertar menús principales del sistema
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado) VALUES
('Dashboard', 'fas fa-tachometer-alt', 'DashboardServlet', NULL, 1, TRUE),
('Clientes', 'fas fa-users', 'ClienteServlet', NULL, 2, TRUE),
('Empleados', 'fas fa-user-tie', 'EmpleadoServlet', NULL, 3, TRUE),
('Puestos', 'fas fa-briefcase', 'PuestoServlet', NULL, 4, TRUE),
('Productos', 'fas fa-box', 'ProductoServlet', NULL, 5, TRUE),
('Marcas', 'fas fa-tags', 'MarcaServlet', NULL, 6, TRUE),
('Proveedores', 'fas fa-truck', 'ProveedorServlet', NULL, 7, TRUE),
('Ventas', 'fas fa-shopping-cart', 'VentaServlet', NULL, 8, TRUE),
('Compras', 'fas fa-shopping-bag', 'CompraServlet', NULL, 9, TRUE),
('Usuarios', 'fas fa-users-cog', 'UsuarioServlet', NULL, 10, TRUE);

-- Verificar que los menús se insertaron correctamente
SELECT * FROM menus ORDER BY orden;
