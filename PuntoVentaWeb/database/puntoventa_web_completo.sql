-- ===============================
-- SCRIPT COMPLETO PUNTO DE VENTA WEB
-- Base: RepoPro C# + Extensiones del SistemaEmpresa
-- ===============================

-- Crear la base de datos
DROP DATABASE IF EXISTS PuntoVentaWeb;
CREATE DATABASE PuntoVentaWeb;

-- Usar la base de datos creada
USE PuntoVentaWeb;

-- ===============================
-- TABLAS BÁSICAS DEL SISTEMA (IGUAL QUE REPP C#)
-- ===============================

-- Crear tabla Puestos
CREATE TABLE Puestos (
    idPuesto SMALLINT AUTO_INCREMENT PRIMARY KEY,
    puesto VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100),
    salario_base DECIMAL(8,2),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla Marcas
CREATE TABLE Marcas (
    idMarca SMALLINT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100),
    pais_origen VARCHAR(50),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla Proveedores
CREATE TABLE Proveedores (
    idProveedor INT AUTO_INCREMENT PRIMARY KEY,
    proveedor VARCHAR(60) NOT NULL,
    nit VARCHAR(12) UNIQUE,
    direccion VARCHAR(80),
    telefono VARCHAR(25),
    email VARCHAR(50),
    contacto_principal VARCHAR(60),
    activo BOOLEAN DEFAULT TRUE,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla Productos
CREATE TABLE Productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    producto VARCHAR(50) NOT NULL,
    codigo_barras VARCHAR(50) UNIQUE,
    idMarca SMALLINT,
    descripcion VARCHAR(100),
    imagen VARCHAR(100),
    precio_costo DECIMAL(8,2) NOT NULL,
    precio_venta DECIMAL(8,2) NOT NULL,
    existencia INT DEFAULT 0,
    stock_minimo INT DEFAULT 5,
    activo BOOLEAN DEFAULT TRUE,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idMarca) REFERENCES Marcas(idMarca)
);

-- Crear tabla Empleados
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
    salario DECIMAL(8,2),
    activo BOOLEAN DEFAULT TRUE,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idPuesto) REFERENCES Puestos(idPuesto)
);

-- Crear tabla Clientes
CREATE TABLE Clientes (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60) NOT NULL,
    apellidos VARCHAR(60) NOT NULL,
    NIT VARCHAR(12),
    genero BIT,
    telefono VARCHAR(25),
    correo_electronico VARCHAR(45),
    direccion VARCHAR(80),
    activo BOOLEAN DEFAULT TRUE,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- TABLAS DE USUARIOS Y SEGURIDAD (DE SISTEMAEMPRESA)
-- ===============================

-- Crear tabla Usuarios (para login del sistema)
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

-- ===============================
-- TABLAS DE MENÚS DINÁMICOS (DE SISTEMAEMPRESA)
-- ===============================

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
-- TABLAS DE COMPRAS (MEJORADAS DEL C#)
-- ===============================

-- Crear tabla Compras (igual que C# pero con mejoras)
CREATE TABLE Compras (
    idcompra INT AUTO_INCREMENT PRIMARY KEY,
    no_order_compra INT NOT NULL,
    idproveedor INT NOT NULL,
    fecha_order DATE NOT NULL,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) DEFAULT 0.00,
    descuento DECIMAL(10,2) DEFAULT 0.00,
    total DECIMAL(10,2) DEFAULT 0.00,
    estado ENUM('PENDIENTE', 'RECIBIDA', 'CANCELADA') DEFAULT 'PENDIENTE',
    observaciones TEXT,
    idUsuario INT,
    FOREIGN KEY (idproveedor) REFERENCES Proveedores(idProveedor),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);

-- Crear tabla Compras_detalle (igual que C#)
CREATE TABLE Compras_detalle (
    idcompra_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idcompra INT NOT NULL,
    idproducto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(8,2) NOT NULL,
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    FOREIGN KEY (idcompra) REFERENCES Compras(idcompra) ON DELETE CASCADE,
    FOREIGN KEY (idproducto) REFERENCES Productos(idProducto)
);

-- ===============================
-- TABLAS DE VENTAS (MEJORADAS DEL C#)
-- ===============================

-- Crear tabla Ventas (igual que C# pero con mejoras)
CREATE TABLE Ventas (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    nofactura INT NOT NULL,
    serie CHAR(1) DEFAULT 'A',
    fechafactura DATE NOT NULL,
    idcliente INT NOT NULL,
    idempleado INT NOT NULL,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    subtotal DECIMAL(10,2) DEFAULT 0.00,
    descuento DECIMAL(10,2) DEFAULT 0.00,
    total DECIMAL(10,2) DEFAULT 0.00,
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA') DEFAULT 'EFECTIVO',
    estado ENUM('COMPLETADA', 'CANCELADA', 'PENDIENTE') DEFAULT 'COMPLETADA',
    observaciones TEXT,
    idUsuario INT,
    FOREIGN KEY (idcliente) REFERENCES Clientes(idCliente),
    FOREIGN KEY (idempleado) REFERENCES Empleados(idEmpleado),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
    UNIQUE KEY unique_factura (nofactura, serie)
);

-- Crear tabla Ventas_detalle (igual que C# pero mejorada)
CREATE TABLE Ventas_detalle (
    idventa_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idventa INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(8,2) NOT NULL,
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precio_unitario) STORED,
    FOREIGN KEY (idventa) REFERENCES Ventas(idVenta) ON DELETE CASCADE,
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- ===============================
-- TRIGGERS PARA ACTUALIZAR TOTALES (FUNCIONALIDAD DEL C#)
-- ===============================

-- Trigger para actualizar total de compras
DELIMITER //
CREATE TRIGGER tr_compras_total_insert
AFTER INSERT ON Compras_detalle
FOR EACH ROW
BEGIN
    UPDATE Compras 
    SET subtotal = (
        SELECT COALESCE(SUM(subtotal), 0) 
        FROM Compras_detalle 
        WHERE idcompra = NEW.idcompra
    ),
    total = subtotal - descuento
    WHERE idcompra = NEW.idcompra;
END//

CREATE TRIGGER tr_compras_total_update
AFTER UPDATE ON Compras_detalle
FOR EACH ROW
BEGIN
    UPDATE Compras 
    SET subtotal = (
        SELECT COALESCE(SUM(subtotal), 0) 
        FROM Compras_detalle 
        WHERE idcompra = NEW.idcompra
    ),
    total = subtotal - descuento
    WHERE idcompra = NEW.idcompra;
END//

CREATE TRIGGER tr_compras_total_delete
AFTER DELETE ON Compras_detalle
FOR EACH ROW
BEGIN
    UPDATE Compras 
    SET subtotal = (
        SELECT COALESCE(SUM(subtotal), 0) 
        FROM Compras_detalle 
        WHERE idcompra = OLD.idcompra
    ),
    total = subtotal - descuento
    WHERE idcompra = OLD.idcompra;
END//

-- Trigger para actualizar total de ventas
CREATE TRIGGER tr_ventas_total_insert
AFTER INSERT ON Ventas_detalle
FOR EACH ROW
BEGIN
    UPDATE Ventas 
    SET subtotal = (
        SELECT COALESCE(SUM(subtotal), 0) 
        FROM Ventas_detalle 
        WHERE idventa = NEW.idventa
    ),
    total = subtotal - descuento
    WHERE idVenta = NEW.idventa;
END//

CREATE TRIGGER tr_ventas_total_update
AFTER UPDATE ON Ventas_detalle
FOR EACH ROW
BEGIN
    UPDATE Ventas 
    SET subtotal = (
        SELECT COALESCE(SUM(subtotal), 0) 
        FROM Ventas_detalle 
        WHERE idventa = NEW.idventa
    ),
    total = subtotal - descuento
    WHERE idVenta = NEW.idventa;
END//

CREATE TRIGGER tr_ventas_total_delete
AFTER DELETE ON Ventas_detalle
FOR EACH ROW
BEGIN
    UPDATE Ventas 
    SET subtotal = (
        SELECT COALESCE(SUM(subtotal), 0) 
        FROM Ventas_detalle 
        WHERE idventa = OLD.idventa
    ),
    total = subtotal - descuento
    WHERE idVenta = OLD.idventa;
END//

DELIMITER ;

-- ===============================
-- DATOS INICIALES DEL SISTEMA
-- ===============================

-- Insertar Puestos
INSERT INTO Puestos (puesto, descripcion, salario_base) VALUES
('Cajero', 'Encargado de caja y atención al cliente', 3500.00),
('Vendedor', 'Vendedor de mostrador', 4000.00),
('Supervisor', 'Supervisor de ventas', 5500.00),
('Administrador', 'Administrador del sistema', 8000.00),
('Gerente', 'Gerente general', 12000.00);

-- Insertar Marcas
INSERT INTO Marcas (marca, descripcion, pais_origen) VALUES
('Samsung', 'Electrónicos y electrodomésticos', 'Corea del Sur'),
('LG', 'Línea blanca y electrónicos', 'Corea del Sur'),
('Sony', 'Electrónicos y entretenimiento', 'Japón'),
('Panasonic', 'Electrodomésticos', 'Japón'),
('Whirlpool', 'Línea blanca', 'Estados Unidos');

-- Insertar Proveedores
INSERT INTO Proveedores (proveedor, nit, direccion, telefono, email, contacto_principal) VALUES
('Distribuidora Central', '1234567-8', 'Zona 1, Ciudad de Guatemala', '2555-1234', 'ventas@distcentral.com', 'Juan Pérez'),
('Tecno S.A.', '8765432-1', 'Zona 10, Ciudad de Guatemala', '2555-5678', 'info@tecnosa.com', 'María García'),
('Electro Hogar', '3216549-3', 'Zona 8, Ciudad de Guatemala', '2555-9876', 'compras@electrohogar.com', 'Carlos López'),
('Mundo Electrónico', '6549873-2', 'Zona 12, Ciudad de Guatemala', '2555-4321', 'ventas@mundoelectronico.com', 'Ana Rodríguez');

-- Insertar Empleados
INSERT INTO Empleados (nombres, apellidos, direccion, telefono, DPI, genero, fecha_nacimiento, idPuesto, fecha_inicio_labores, salario) VALUES
('Luis', 'Pérez', 'Zona 3, Guatemala', '5555-1111', '1234567890101', 1, '1990-05-15', 1, '2023-01-01', 3500.00),
('Ana', 'Gómez', 'Zona 5, Guatemala', '5555-2222', '1098765432101', 0, '1995-07-22', 2, '2023-06-01', 4000.00),
('Marco', 'Ramírez', 'Zona 7, Guatemala', '5555-3333', '1020304050607', 1, '1988-03-12', 3, '2022-11-15', 5500.00),
('Claudia', 'Reyes', 'Zona 9, Guatemala', '5555-4444', '7080901020304', 0, '1992-09-30', 4, '2023-03-10', 8000.00),
('Roberto', 'Morales', 'Zona 11, Guatemala', '5555-5555', '5060708090101', 1, '1985-12-08', 5, '2022-01-15', 12000.00);

-- Insertar Usuarios del sistema
INSERT INTO Usuarios (usuario, password, idEmpleado) VALUES
('admin', '123456', 4),
('supervisor', '123456', 3),
('cajero1', '123456', 1),
('vendedor1', '123456', 2),
('gerente', '123456', 5);

-- Insertar Productos
INSERT INTO Productos (producto, codigo_barras, idMarca, descripcion, imagen, precio_costo, precio_venta, existencia, stock_minimo) VALUES
('Televisor 42"', '7501234567890', 1, 'Smart TV Samsung 42 pulgadas 4K', 'tv42_samsung.jpg', 2500.00, 3200.00, 10, 2),
('Refrigeradora 14ft', '7501234567891', 2, 'Refrigeradora LG 14 pies cúbicos', 'fridge14_lg.jpg', 1800.00, 2500.00, 8, 1),
('Minicomponente', '7501234567892', 3, 'Minicomponente Sony 500W Bluetooth', 'sony500.jpg', 900.00, 1200.00, 15, 3),
('Microondas', '7501234567893', 4, 'Microondas Panasonic 25L digital', 'panamicro.jpg', 600.00, 850.00, 20, 5),
('Lavadora', '7501234567894', 5, 'Lavadora Whirlpool 18kg automática', 'lavadora18.jpg', 2200.00, 2800.00, 6, 2);

-- Insertar Clientes
INSERT INTO Clientes (nombres, apellidos, NIT, genero, telefono, correo_electronico, direccion) VALUES
('Carlos', 'López', '1234567-8', 1, '5555-3333', 'carlos@example.com', 'Zona 1, Guatemala'),
('María', 'Hernández', '8765432-1', 0, '5555-4444', 'maria@example.com', 'Zona 4, Guatemala'),
('Pedro', 'Santos', '1111111-1', 1, '5555-5555', 'pedro@example.com', 'Zona 6, Guatemala'),
('Lucía', 'Morales', '2222222-2', 0, '5555-6666', 'lucia@example.com', 'Zona 8, Guatemala'),
('José', 'Ramírez', '3333333-3', 1, '5555-7777', 'jose@example.com', 'Zona 10, Guatemala');

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
('Compras', 'fas fa-shopping-bag', 'CompraServlet', NULL, 9, TRUE),
('Reportes', 'fas fa-chart-bar', '#', NULL, 10, TRUE);

-- Insertar submenús
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado) VALUES
('Nueva Venta', 'fas fa-plus-circle', 'VentaServlet?action=form', 8, 1, TRUE),
('Listar Ventas', 'fas fa-list', 'VentaServlet?action=list', 8, 2, TRUE),
('Nueva Compra', 'fas fa-plus-circle', 'CompraServlet?action=form', 9, 1, TRUE),
('Listar Compras', 'fas fa-list', 'CompraServlet?action=list', 9, 2, TRUE),
('Reporte Ventas', 'fas fa-chart-line', 'ReporteServlet?tipo=ventas', 10, 1, TRUE),
('Reporte Compras', 'fas fa-chart-area', 'ReporteServlet?tipo=compras', 10, 2, TRUE);

-- Insertar imágenes del carrusel
INSERT INTO carrusel_imagenes (titulo, descripcion, imagen_url, enlace_url, orden, activo) VALUES
('Bienvenido al Sistema', 'Sistema de Punto de Venta completo y fácil de usar', 'assets/images/carousel1.jpg', '#', 1, TRUE),
('Gestión de Ventas', 'Controla todas tus ventas de manera eficiente', 'assets/images/carousel2.jpg', 'VentaServlet', 2, TRUE),
('Control de Inventario', 'Mantén tu inventario siempre actualizado', 'assets/images/carousel3.jpg', 'ProductoServlet', 3, TRUE);

-- ===============================
-- DATOS DE EJEMPLO PARA COMPRAS Y VENTAS
-- ===============================

-- Insertar compras de ejemplo
INSERT INTO Compras (no_order_compra, idproveedor, fecha_order, estado, observaciones, idUsuario) VALUES
(1001, 1, '2024-01-15', 'RECIBIDA', 'Compra de televisores Samsung', 1),
(1002, 2, '2024-01-20', 'RECIBIDA', 'Compra de refrigeradoras LG', 1),
(1003, 3, '2024-01-25', 'PENDIENTE', 'Compra de equipos de sonido', 1);

-- Insertar detalles de compras
INSERT INTO Compras_detalle (idcompra, idproducto, cantidad, precio_unitario) VALUES
(1, 1, 5, 2500.00),
(2, 2, 3, 1800.00),
(3, 3, 6, 900.00);

-- Insertar ventas de ejemplo
INSERT INTO Ventas (nofactura, serie, fechafactura, idcliente, idempleado, metodo_pago, estado, idUsuario) VALUES
(2001, 'A', '2024-02-01', 1, 1, 'EFECTIVO', 'COMPLETADA', 1),
(2002, 'A', '2024-02-02', 2, 2, 'TARJETA', 'COMPLETADA', 1),
(2003, 'A', '2024-02-03', 3, 1, 'EFECTIVO', 'COMPLETADA', 1);

-- Insertar detalles de ventas
INSERT INTO Ventas_detalle (idventa, idProducto, cantidad, precio_unitario) VALUES
(1, 1, 1, 3200.00),
(2, 2, 1, 2500.00),
(3, 3, 2, 1200.00);
