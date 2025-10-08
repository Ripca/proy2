-- Script de creación de base de datos para Sistema Empresa
-- Basado en el diagrama ER proporcionado

DROP DATABASE IF EXISTS sistema_empresa;
CREATE DATABASE sistema_empresa;
USE sistema_empresa;

-- Tabla de Puestos
CREATE TABLE Puestos (
    idPuesto SMALLINT AUTO_INCREMENT PRIMARY KEY,
    puesto VARCHAR(50) NOT NULL
);

-- Tabla de Empleados
CREATE TABLE Empleados (
    idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    direccion VARCHAR(80),
    telefono VARCHAR(25),
    DPI VARCHAR(15) UNIQUE,
    genero BIT,
    fecha_nacimiento DATE,
    idPuesto SMALLINT,
    fecha_inicio_labores DATE,
    fechaingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idPuesto) REFERENCES Puestos(idPuesto)
);

-- Tabla de Clientes
CREATE TABLE Clientes (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    NIT VARCHAR(12),
    genero BIT,
    telefono VARCHAR(25),
    correo_electronico VARCHAR(45),
    fechaingreso DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Marcas
CREATE TABLE Marcas (
    idMarca SMALLINT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL
);

-- Tabla de Productos
CREATE TABLE Productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    producto VARCHAR(50) NOT NULL,
    idMarca SMALLINT,
    descripcion VARCHAR(100),
    imagen VARCHAR(30),
    precio_costo DECIMAL(8,2),
    precio_venta DECIMAL(8,2),
    existencia INT,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idMarca) REFERENCES Marcas(idMarca)
);

-- Tabla de Proveedores
CREATE TABLE Proveedores (
    idProveedor INT AUTO_INCREMENT PRIMARY KEY,
    proveedor VARCHAR(60) NOT NULL,
    nit VARCHAR(12),
    direccion VARCHAR(80),
    telefono VARCHAR(25)
);

-- Tabla de Ventas
CREATE TABLE Ventas (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    noFactura INT,
    serie CHAR(1),
    fechaFactura DATE,
    idCliente INT,
    idEmpleado INT,
    fechaingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idCliente) REFERENCES Clientes(idCliente),
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado)
);

-- Tabla de Ventas_detalle
CREATE TABLE Ventas_detalle (
    idVenta_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idVenta INT,
    idProducto INT,
    cantidad INT,
    precio_unitario DECIMAL(8,2),
    FOREIGN KEY (idVenta) REFERENCES Ventas(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- Tabla de Compras
CREATE TABLE Compras (
    idCompra INT AUTO_INCREMENT PRIMARY KEY,
    no_orden_compra INT,
    idProveedor INT,
    fecha_orden DATE,
    fechaingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idProveedor) REFERENCES Proveedores(idProveedor)
);

-- Tabla de Compras_detalle
CREATE TABLE Compras_detalle (
    idCompra_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idCompra INT,
    idProducto INT,
    cantidad INT,
    precio_costo_unitario DECIMAL(8,2),
    FOREIGN KEY (idCompra) REFERENCES Compras(idCompra),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- Tabla de Usuarios para autenticación
CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    idEmpleado INT,
    activo BIT DEFAULT 1,
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado)
);

-- Insertar datos de prueba

-- Puestos
INSERT INTO Puestos (puesto) VALUES 
('Administrador'),
('Vendedor'),
('Cajero'),
('Gerente'),
('Contador');

-- Empleados
INSERT INTO Empleados (nombres, apellidos, direccion, telefono, DPI, genero, fecha_nacimiento, idPuesto, fecha_inicio_labores) VALUES
('Juan Carlos', 'Pérez López', 'Zona 1, Guatemala', '12345678', '1234567890101', 1, '1990-05-15', 1, '2023-01-15'),
('María Elena', 'García Rodríguez', 'Zona 10, Guatemala', '87654321', '9876543210987', 0, '1985-08-20', 2, '2023-02-01'),
('Pedro Antonio', 'Martínez Hernández', 'Zona 7, Guatemala', '11223344', '1122334455667', 1, '1992-12-10', 3, '2023-03-01');

-- Usuarios (password: 123456 - en producción debería estar hasheada)
INSERT INTO Usuarios (usuario, password, idEmpleado) VALUES
('admin', '123456', 1),
('vendedor1', '123456', 2),
('cajero1', '123456', 3);

-- Clientes
INSERT INTO Clientes (nombres, apellidos, NIT, genero, telefono, correo_electronico) VALUES
('Ana Sofía', 'López Morales', '12345678901', 0, '55667788', 'ana.lopez@email.com'),
('Carlos Eduardo', 'Ramírez Castillo', '98765432109', 1, '44556677', 'carlos.ramirez@email.com');

-- Marcas
INSERT INTO Marcas (marca) VALUES
('Samsung'),
('Apple'),
('LG'),
('Sony'),
('HP');

-- Productos
INSERT INTO Productos (producto, idMarca, descripcion, precio_costo, precio_venta, existencia) VALUES
('Smartphone Galaxy S21', 1, 'Teléfono inteligente Samsung Galaxy S21', 500.00, 750.00, 10),
('iPhone 13', 2, 'Teléfono inteligente Apple iPhone 13', 800.00, 1200.00, 5),
('Smart TV 55"', 3, 'Televisor inteligente LG 55 pulgadas', 400.00, 600.00, 8);

-- Proveedores
INSERT INTO Proveedores (proveedor, nit, direccion, telefono) VALUES
('Distribuidora Tech S.A.', '12345678901', 'Zona 4, Guatemala', '23456789'),
('Importadora Digital', '98765432109', 'Zona 9, Guatemala', '34567890');

-- Ventas
INSERT INTO Ventas (noFactura, serie, fechaFactura, idCliente, idEmpleado) VALUES
(1, 'A', '2024-01-15', 1, 2),
(2, 'A', '2024-01-16', 2, 2);

-- Ventas detalle
INSERT INTO Ventas_detalle (idVenta, idProducto, cantidad, precio_unitario) VALUES
(1, 1, 1, 750.00),
(2, 2, 1, 1200.00);

-- Compras
INSERT INTO Compras (no_orden_compra, idProveedor, fecha_orden) VALUES
(1001, 1, '2024-01-10'),
(1002, 2, '2024-01-12');

-- Compras detalle
INSERT INTO Compras_detalle (idCompra, idProducto, cantidad, precio_costo_unitario) VALUES
(1, 1, 20, 500.00),
(2, 2, 10, 800.00);
