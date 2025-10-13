-- ===============================
-- SCRIPT COMPLETO PUNTO DE VENTA WEB
-- Base: RepoPro C# + Extensiones del SistemaEmpresa
-- Autor: Sistema integrado
-- Fecha: 2024
-- ===============================

-- Crear la base de datos
DROP DATABASE IF EXISTS PuntoVentaWebCompleto;
CREATE DATABASE PuntoVentaWebCompleto;

-- Usar la base de datos creada
USE PuntoVentaWebCompleto;

-- ===============================
-- TABLAS BÁSICAS DEL SISTEMA (EXACTAS DEL C#)
-- ===============================

-- Crear tabla Puestos (igual que C#)
CREATE TABLE Puestos (
    idPuesto SMALLINT AUTO_INCREMENT PRIMARY KEY,
    puesto VARCHAR(50) NOT NULL
);

-- Crear tabla Marcas (igual que C#)
CREATE TABLE Marcas (
    idMarca SMALLINT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL
);

-- Crear tabla Proveedores (igual que C#)
CREATE TABLE Proveedores (
    idProveedor INT AUTO_INCREMENT PRIMARY KEY,
    proveedor VARCHAR(60) NOT NULL,
    nit VARCHAR(12),
    direccion VARCHAR(80),
    telefono VARCHAR(25)
);

-- Crear tabla Productos (igual que C#)
CREATE TABLE Productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    producto VARCHAR(50) NOT NULL,
    idMarca SMALLINT,
    descripcion VARCHAR(100),
    imagen VARCHAR(30),
    precio_costo DECIMAL(8,2),
    precio_venta DECIMAL(8,2),
    existencia INT,
    fecha_ingreso DATETIME,
    FOREIGN KEY (idMarca) REFERENCES Marcas(idMarca)
);

-- Crear tabla Empleados (igual que C#)
CREATE TABLE Empleados (
    idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    direccion VARCHAR(80),
    telefono VARCHAR(25),
    DPI VARCHAR(15),
    genero BIT,
    fecha_nacimiento DATE,
    idPuesto SMALLINT,
    fecha_inicio_labores DATE,
    fecha_ingreso DATETIME,
    FOREIGN KEY (idPuesto) REFERENCES Puestos(idPuesto)
);

-- Crear tabla Clientes (igual que C#)
CREATE TABLE Clientes (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    NIT VARCHAR(12),
    genero BIT,
    telefono VARCHAR(25),
    correo_electronico VARCHAR(45),
    fecha_ingreso DATETIME
);

-- Crear tabla Compras (EXACTA del C#)
CREATE TABLE Compras (
    idcompra INT AUTO_INCREMENT PRIMARY KEY,
    no_order_compra INT,
    idproveedor INT,
    fecha_order DATE,
    fecha_ingreso DATETIME,
    FOREIGN KEY (idproveedor) REFERENCES Proveedores(idProveedor)
);

-- Crear tabla Compras_detalle (EXACTA del C#)
CREATE TABLE Compras_detalle (
    idcompra_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idcompra INT,
    idproducto INT,
    cantidad INT,
    precio_unitario DECIMAL(8,2),
    FOREIGN KEY (idcompra) REFERENCES Compras(idcompra),
    FOREIGN KEY (idproducto) REFERENCES Productos(idProducto)
);

-- Crear tabla Ventas (EXACTA del C#)
CREATE TABLE Ventas (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    nofactura INT,
    serie CHAR(1),
    fechafactura DATE,
    idcliente INT,
    idempleado INT,
    fecha_ingreso DATETIME,
    FOREIGN KEY (idcliente) REFERENCES Clientes(idCliente),
    FOREIGN KEY (idempleado) REFERENCES Empleados(idEmpleado)
);

-- Crear tabla Ventas_detalle (EXACTA del C#)
CREATE TABLE Ventas_detalle (
    idventa_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idventa INT,
    idProducto INT,
    cantidad VARCHAR(45),
    precio_unitario DECIMAL(8,2),
    FOREIGN KEY (idventa) REFERENCES Ventas(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- ===============================
-- EXTENSIONES PARA EL SISTEMA WEB (DE SISTEMAEMPRESA)
-- ===============================

-- Tabla de Usuarios para autenticación
CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    idEmpleado INT,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso DATETIME,
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado)
);

-- Tabla de menús dinámicos
CREATE TABLE menus (
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

-- Tabla para carrusel de imágenes
CREATE TABLE carrusel_imagenes (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    imagen_url VARCHAR(255) NOT NULL,
    enlace_url VARCHAR(255),
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- DATOS INICIALES DEL SISTEMA
-- ===============================

-- Insertar Puestos
INSERT INTO Puestos (puesto) VALUES 
('Cajero'),
('Vendedor'),
('Supervisor'),
('Administrador');

-- Insertar Marcas
INSERT INTO Marcas (marca) VALUES 
('Samsung'),
('LG'),
('Sony'),
('Panasonic');

-- Insertar Proveedores
INSERT INTO Proveedores (proveedor, nit, direccion, telefono) VALUES 
('Distribuidora Central', '1234567-8', 'Zona 1, Ciudad', '5555-1234'),
('Tecno S.A.', '8765432-1', 'Zona 10, Ciudad', '5555-5678'),
('Electro Hogar', '3216549-3', 'Zona 8, Ciudad', '5555-9876'),
('Mundo Electrónico', '6549873-2', 'Zona 12, Ciudad', '5555-4321');

-- Insertar Productos
INSERT INTO Productos (producto, idMarca, descripcion, imagen, precio_costo, precio_venta, existencia, fecha_ingreso) VALUES 
('Televisor 42"', 1, 'Smart TV Samsung 42 pulgadas', 'tv42.jpg', 2500.00, 3200.00, 10, NOW()),
('Refrigeradora 14ft', 2, 'Refrigeradora LG 14 pies', 'fridge14.jpg', 1800.00, 2500.00, 8, NOW()),
('Minicomponente', 3, 'Minicomponente Sony 500W', 'sony500.jpg', 900.00, 1200.00, 15, NOW()),
('Microondas', 4, 'Microondas Panasonic 25L', 'panamicro.jpg', 600.00, 850.00, 20, NOW());

-- Insertar Empleados
INSERT INTO Empleados (nombres, apellidos, direccion, telefono, DPI, genero, fecha_nacimiento, idPuesto, fecha_inicio_labores, fecha_ingreso) VALUES 
('Luis', 'Pérez', 'Zona 3', '5555-1111', '1234567890101', 1, '1990-05-15', 1, '2023-01-01', NOW()),
('Ana', 'Gómez', 'Zona 5', '5555-2222', '1098765432101', 0, '1995-07-22', 2, '2023-06-01', NOW()),
('Marco', 'Ramírez', 'Zona 7', '5555-3333', '1020304050607', 1, '1988-03-12', 3, '2022-11-15', NOW()),
('Claudia', 'Reyes', 'Zona 9', '5555-4444', '7080901020304', 0, '1992-09-30', 4, '2023-03-10', NOW());

-- Insertar Usuarios del sistema
INSERT INTO Usuarios (usuario, password, idEmpleado) VALUES 
('admin', '123456', 4),
('supervisor', '123456', 3),
('cajero1', '123456', 1),
('vendedor1', '123456', 2);

-- Insertar Clientes
INSERT INTO Clientes (nombres, apellidos, NIT, genero, telefono, correo_electronico, fecha_ingreso) VALUES 
('Carlos', 'López', '1234567-8', 1, '5555-3333', 'carlos@example.com', NOW()),
('Maria', 'Hernández', '8765432-1', 0, '5555-4444', 'maria@example.com', NOW()),
('Pedro', 'Santos', '1111111-1', 1, '5555-5555', 'pedro@example.com', NOW()),
('Lucía', 'Morales', '2222222-2', 0, '5555-6666', 'lucia@example.com', NOW());

-- Insertar menús del sistema (copiado de SistemaEmpresa)
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado) VALUES
('Dashboard', 'fas fa-tachometer-alt', 'DashboardServlet', NULL, 1, TRUE),
('Clientes', 'fas fa-users', 'ClienteServlet', NULL, 2, TRUE),
('Empleados', 'fas fa-user-tie', 'EmpleadoServlet', NULL, 3, TRUE),
('Puestos', 'fas fa-briefcase', 'PuestoServlet', NULL, 4, TRUE),
('Productos', 'fas fa-box', 'ProductoServlet', NULL, 5, TRUE),
('Marcas', 'fas fa-tags', 'MarcaServlet', NULL, 6, TRUE),
('Proveedores', 'fas fa-truck', 'ProveedorServlet', NULL, 7, TRUE),
('Ventas', 'fas fa-shopping-cart', 'VentaServlet', NULL, 8, TRUE),
('Compras', 'fas fa-shopping-bag', 'CompraServlet', NULL, 9, TRUE);

-- Insertar imágenes del carrusel
INSERT INTO carrusel_imagenes (titulo, descripcion, imagen_url, enlace_url, orden, activo) VALUES
('Bienvenido al Sistema', 'Sistema de Punto de Venta completo', 'assets/images/carousel1.jpg', '#', 1, TRUE),
('Gestión de Ventas', 'Controla todas tus ventas', 'assets/images/carousel2.jpg', 'VentaServlet', 2, TRUE),
('Control de Inventario', 'Mantén tu inventario actualizado', 'assets/images/carousel3.jpg', 'ProductoServlet', 3, TRUE);

-- ===============================
-- DATOS DE EJEMPLO (IGUAL QUE C#)
-- ===============================

-- Insertar compras de ejemplo
INSERT INTO Compras (no_order_compra, idproveedor, fecha_order, fecha_ingreso) VALUES 
(1001, 1, '2024-06-01', NOW()),
(1002, 2, '2024-06-10', NOW()),
(1003, 3, '2024-06-15', NOW()),
(1004, 4, '2024-06-20', NOW());

-- Insertar detalles de compras
INSERT INTO Compras_detalle (idcompra, idproducto, cantidad, precio_unitario) VALUES 
(1, 1, 5, 2500.00),
(2, 2, 3, 1800.00),
(3, 3, 6, 900.00),
(4, 4, 4, 600.00);

-- Insertar ventas de ejemplo
INSERT INTO Ventas (nofactura, serie, fechafactura, idcliente, idempleado, fecha_ingreso) VALUES 
(2001, 'A', '2024-06-15', 1, 1, NOW()),
(2002, 'A', '2024-06-20', 2, 2, NOW()),
(2003, 'A', '2024-06-25', 3, 3, NOW()),
(2004, 'A', '2024-06-28', 4, 4, NOW());

-- Insertar detalles de ventas
INSERT INTO Ventas_detalle (idventa, idProducto, cantidad, precio_unitario) VALUES 
(1, 1, '1', 3200.00),
(2, 2, '2', 2500.00),
(3, 3, '1', 1200.00),
(4, 4, '2', 850.00);
