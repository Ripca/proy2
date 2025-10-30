-- Script para crear tablas de roles y permisos
-- Autor: Sistema Empresa
-- Fecha: 2025-10-30

USE sistema_empresa;

-- 1. Tabla de Roles
CREATE TABLE IF NOT EXISTS roles (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL UNIQUE,
    estado TINYINT DEFAULT 1,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de relación Rol-Menu
CREATE TABLE IF NOT EXISTS rol_menu (
    idRolMenu INT AUTO_INCREMENT PRIMARY KEY,
    idRol INT NOT NULL,
    idMenu INT NOT NULL,
    estado TINYINT DEFAULT 1,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idRol) REFERENCES roles(idRol) ON DELETE CASCADE,
    FOREIGN KEY (idMenu) REFERENCES MenuItems(idMenuItem) ON DELETE CASCADE,
    UNIQUE KEY unique_rol_menu (idRol, idMenu)
);

-- 3. Tabla de relación Usuario-Rol
CREATE TABLE IF NOT EXISTS usuario_rol (
    idRolUsu INT AUTO_INCREMENT PRIMARY KEY,
    idRol INT NOT NULL,
    idUsuario INT NOT NULL,
    estado TINYINT DEFAULT 1,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idRol) REFERENCES roles(idRol) ON DELETE CASCADE,
    FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario) ON DELETE CASCADE,
    UNIQUE KEY unique_usuario_rol (idUsuario, idRol)
);

-- Insertar roles por defecto
INSERT INTO roles (nombre, estado) VALUES
('Admin', 1),
('Cajero', 1),
('Gerente', 1),
('Vendedor', 1),
('Almacenero', 1);

-- Crear índices para mejor rendimiento
CREATE INDEX idx_rol_menu_rol ON rol_menu(idRol);
CREATE INDEX idx_rol_menu_menu ON rol_menu(idMenu);
CREATE INDEX idx_usuario_rol_usuario ON usuario_rol(idUsuario);
CREATE INDEX idx_usuario_rol_rol ON usuario_rol(idRol);

-- Mensaje de confirmación
SELECT 'Tablas de roles y permisos creadas correctamente' as 'Estado';
SELECT COUNT(*) as 'Total de roles' FROM roles WHERE estado = 1;

