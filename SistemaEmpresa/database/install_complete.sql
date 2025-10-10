-- Script de instalación completa del Sistema Empresa
-- Ejecutar este script para crear toda la base de datos

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS sistema_empresa;
USE sistema_empresa;

-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS Clientes (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    NIT VARCHAR(12),
    genero BOOLEAN NOT NULL,
    telefono VARCHAR(25),
    correo_electronico VARCHAR(45),
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Puestos
CREATE TABLE IF NOT EXISTS Puestos (
    idPuesto INT AUTO_INCREMENT PRIMARY KEY,
    puesto VARCHAR(50) NOT NULL
);

-- Tabla de Empleados
CREATE TABLE IF NOT EXISTS Empleados (
    idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    direccion VARCHAR(80),
    telefono VARCHAR(25),
    DPI VARCHAR(15),
    genero BOOLEAN NOT NULL,
    fecha_nacimiento DATE,
    idPuesto INT,
    fecha_inicio_labores DATE,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idPuesto) REFERENCES Puestos(idPuesto)
);

-- Tabla de Marcas
CREATE TABLE IF NOT EXISTS Marcas (
    idMarca INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL
);

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS Productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    producto VARCHAR(50) NOT NULL,
    idMarca INT,
    descripcion TEXT,
    imagen VARCHAR(200),
    precio_costo DECIMAL(8,2),
    precio_venta DECIMAL(8,2),
    existencia INT DEFAULT 0,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idMarca) REFERENCES Marcas(idMarca)
);

-- Tabla de Proveedores
CREATE TABLE IF NOT EXISTS Proveedores (
    idProveedor INT AUTO_INCREMENT PRIMARY KEY,
    proveedor VARCHAR(60) NOT NULL,
    nit VARCHAR(12),
    direccion VARCHAR(80),
    telefono VARCHAR(25)
);

-- Tabla de Ventas
CREATE TABLE IF NOT EXISTS Ventas (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    nofactura INT NOT NULL,
    serie CHAR(1) DEFAULT 'A',
    fechafactura DATE NOT NULL,
    idcliente INT,
    idempleado INT,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idcliente) REFERENCES Clientes(idCliente),
    FOREIGN KEY (idempleado) REFERENCES Empleados(idEmpleado)
);

-- Tabla de Ventas Detalle
CREATE TABLE IF NOT EXISTS Ventas_detalle (
    idventa_detalle INT AUTO_INCREMENT PRIMARY KEY,
    idventa INT,
    idProducto INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (idventa) REFERENCES Ventas(idVenta) ON DELETE CASCADE,
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- Tabla de Compras
CREATE TABLE IF NOT EXISTS Compras (
    idcompra INT AUTO_INCREMENT PRIMARY KEY,
    no_order_compra INT NOT NULL,
    idproveedor INT,
    fecha_order DATE NOT NULL,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idproveedor) REFERENCES Proveedores(idProveedor)
);

-- Tabla de Compras Detalle
CREATE TABLE IF NOT EXISTS Compras_detalle (
    idcompra_detalle INT AUTO_INCREMENT PRIMARY KEY,
    idcompra INT,
    idproducto INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (idcompra) REFERENCES Compras(idcompra) ON DELETE CASCADE,
    FOREIGN KEY (idproducto) REFERENCES Productos(idProducto)
);

-- Tabla de Menús
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

-- Tabla de Usuarios del Sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rol ENUM('Administrador', 'Usuario', 'Vendedor', 'Supervisor') NOT NULL DEFAULT 'Usuario',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATE NOT NULL,
    fecha_ultimo_acceso DATE,
    INDEX idx_usuario (usuario),
    INDEX idx_activo (activo),
    INDEX idx_rol (rol)
);

-- Insertar datos iniciales

-- Puestos
INSERT INTO Puestos (puesto) VALUES 
('Gerente General'),
('Contador'),
('Vendedor'),
('Cajero'),
('Supervisor'),
('Asistente Administrativo');

-- Marcas
INSERT INTO Marcas (marca) VALUES 
('Samsung'),
('Apple'),
('Sony'),
('LG'),
('HP'),
('Dell'),
('Nike'),
('Adidas');

-- Menús del sistema
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

-- Usuarios del sistema
-- Contraseña: admin123 (encriptada con SHA-256)
INSERT INTO usuarios (usuario, password, nombres, apellidos, email, rol, activo, fecha_creacion) 
VALUES (
    'admin', 
    '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 
    'Administrador', 
    'del Sistema', 
    'admin@sistemaempresa.com', 
    'Administrador', 
    TRUE, 
    CURRENT_DATE
) ON DUPLICATE KEY UPDATE 
    password = '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9';

-- Contraseña: usuario123 (encriptada con SHA-256)
INSERT INTO usuarios (usuario, password, nombres, apellidos, email, rol, activo, fecha_creacion) 
VALUES (
    'usuario', 
    'b9c950640e1b3740e98acb93e669c65766f6670dd1609ba91ff41052ba48c6f3', 
    'Usuario', 
    'de Prueba', 
    'usuario@sistemaempresa.com', 
    'Usuario', 
    TRUE, 
    CURRENT_DATE
) ON DUPLICATE KEY UPDATE 
    password = 'b9c950640e1b3740e98acb93e669c65766f6670dd1609ba91ff41052ba48c6f3';

-- Datos de ejemplo para Clientes
INSERT INTO Clientes (nombres, apellidos, NIT, genero, telefono, correo_electronico) VALUES
('Juan Carlos', 'Pérez López', '12345678-9', TRUE, '2234-5678', 'juan.perez@email.com'),
('María Elena', 'García Rodríguez', '98765432-1', FALSE, '5678-9012', 'maria.garcia@email.com'),
('Cliente', 'General', 'C/F', TRUE, '', '');

-- Datos de ejemplo para Proveedores
INSERT INTO Proveedores (proveedor, nit, direccion, telefono) VALUES
('Distribuidora Central S.A.', '123456789', 'Zona 1, Ciudad de Guatemala', '2234-5678'),
('Importaciones del Norte', '987654321', 'Zona 10, Ciudad de Guatemala', '2345-6789'),
('Tecnología Avanzada S.A.', '456789123', 'Zona 4, Ciudad de Guatemala', '2456-7890');

-- Datos de ejemplo para Productos
INSERT INTO Productos (producto, idMarca, descripcion, precio_costo, precio_venta, existencia) VALUES
('Smartphone Galaxy S21', 1, 'Teléfono inteligente Samsung Galaxy S21', 2500.00, 3200.00, 15),
('iPhone 13', 2, 'Teléfono Apple iPhone 13', 3500.00, 4200.00, 10),
('Laptop HP Pavilion', 5, 'Laptop HP Pavilion 15 pulgadas', 3000.00, 3800.00, 8),
('Televisor LG 55"', 4, 'Televisor LG Smart TV 55 pulgadas', 2800.00, 3500.00, 5);

COMMIT;
