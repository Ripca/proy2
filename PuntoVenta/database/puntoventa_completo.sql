-- ===============================
-- SCRIPT COMPLETO PUNTO DE VENTA
-- Base: RepoPro + Extensiones del SistemaEmpresa
-- ===============================

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS PuntoVenta;

-- Usar la base de datos creada
USE PuntoVenta;

-- ===============================
-- TABLAS BÁSICAS DEL SISTEMA
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
-- TABLAS DE USUARIOS Y SEGURIDAD
-- ===============================

-- Crear tabla Usuarios (para login del sistema)
CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    idEmpleado INT,
    rol ENUM('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'CAJERO') DEFAULT 'VENDEDOR',
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_ultimo_acceso DATETIME,
    intentos_fallidos INT DEFAULT 0,
    bloqueado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado)
);

-- ===============================
-- TABLAS DE MENÚS Y NAVEGACIÓN
-- ===============================

-- Crear tabla Menus (para navegación dinámica)
CREATE TABLE Menus (
    idMenu INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    url VARCHAR(100),
    icono VARCHAR(50),
    orden INT DEFAULT 0,
    idMenuPadre INT NULL,
    activo BOOLEAN DEFAULT TRUE,
    roles_permitidos VARCHAR(100) DEFAULT 'ADMIN,SUPERVISOR,VENDEDOR,CAJERO',
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idMenuPadre) REFERENCES Menus(idMenu)
);

-- ===============================
-- TABLAS DE CARRUSEL E IMÁGENES
-- ===============================

-- Crear tabla CarruselImagenes (para dashboard)
CREATE TABLE CarruselImagenes (
    idImagen INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100),
    descripcion TEXT,
    imagen VARCHAR(100) NOT NULL,
    url_enlace VARCHAR(200),
    orden INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ===============================
-- TABLAS DE TRANSACCIONES
-- ===============================

-- Crear tabla Compras
CREATE TABLE Compras (
    idCompra INT AUTO_INCREMENT PRIMARY KEY,
    no_orden_compra INT UNIQUE,
    idProveedor INT NOT NULL,
    fecha_orden DATE NOT NULL,
    fecha_entrega DATE,
    subtotal DECIMAL(10,2) DEFAULT 0,
    descuento DECIMAL(10,2) DEFAULT 0,
    total DECIMAL(10,2) DEFAULT 0,
    estado ENUM('PENDIENTE', 'RECIBIDA', 'CANCELADA') DEFAULT 'PENDIENTE',
    observaciones TEXT,
    idUsuario INT,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idProveedor) REFERENCES Proveedores(idProveedor),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);

-- Crear tabla Compras_detalle
CREATE TABLE Compras_detalle (
    idCompraDetalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idCompra INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(8,2) NOT NULL,
    descuento DECIMAL(8,2) DEFAULT 0,
    subtotal DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (idCompra) REFERENCES Compras(idCompra) ON DELETE CASCADE,
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- Crear tabla Ventas
CREATE TABLE Ventas (
    idVenta INT AUTO_INCREMENT PRIMARY KEY,
    noFactura INT UNIQUE,
    serie CHAR(1) DEFAULT 'A',
    fechaFactura DATE NOT NULL,
    idCliente INT NOT NULL,
    idEmpleado INT NOT NULL,
    subtotal DECIMAL(10,2) DEFAULT 0,
    descuento DECIMAL(10,2) DEFAULT 0,
    total DECIMAL(10,2) DEFAULT 0,
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'TRANSFERENCIA', 'MIXTO') DEFAULT 'EFECTIVO',
    estado ENUM('PENDIENTE', 'COMPLETADA', 'CANCELADA') DEFAULT 'COMPLETADA',
    observaciones TEXT,
    idUsuario INT,
    fecha_ingreso DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idCliente) REFERENCES Clientes(idCliente),
    FOREIGN KEY (idEmpleado) REFERENCES Empleados(idEmpleado),
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);

-- Crear tabla Ventas_detalle
CREATE TABLE Ventas_detalle (
    idVentaDetalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idVenta INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(8,2) NOT NULL,
    descuento DECIMAL(8,2) DEFAULT 0,
    subtotal DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (idVenta) REFERENCES Ventas(idVenta) ON DELETE CASCADE,
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- ===============================
-- TABLAS DE AUDITORÍA
-- ===============================

-- Crear tabla LogActividades (para auditoría)
CREATE TABLE LogActividades (
    idLog BIGINT AUTO_INCREMENT PRIMARY KEY,
    idUsuario INT,
    accion VARCHAR(100) NOT NULL,
    tabla_afectada VARCHAR(50),
    id_registro INT,
    datos_anteriores JSON,
    datos_nuevos JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    fecha_accion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario)
);

-- ===============================
-- ÍNDICES PARA OPTIMIZACIÓN
-- ===============================

-- Índices para búsquedas frecuentes
CREATE INDEX idx_productos_codigo ON Productos(codigo_barras);
CREATE INDEX idx_productos_nombre ON Productos(producto);
CREATE INDEX idx_clientes_nit ON Clientes(NIT);
CREATE INDEX idx_ventas_fecha ON Ventas(fechaFactura);
CREATE INDEX idx_compras_fecha ON Compras(fecha_orden);
CREATE INDEX idx_usuarios_login ON Usuarios(usuario);

-- ===============================
-- TRIGGERS PARA CÁLCULOS AUTOMÁTICOS
-- ===============================

-- Trigger para actualizar totales en Compras
DELIMITER //
CREATE TRIGGER tr_compras_detalle_insert 
AFTER INSERT ON Compras_detalle
FOR EACH ROW
BEGIN
    UPDATE Compras 
    SET subtotal = (
        SELECT SUM(subtotal) 
        FROM Compras_detalle 
        WHERE idCompra = NEW.idCompra
    ),
    total = subtotal - descuento
    WHERE idCompra = NEW.idCompra;
END//

CREATE TRIGGER tr_compras_detalle_update 
AFTER UPDATE ON Compras_detalle
FOR EACH ROW
BEGIN
    UPDATE Compras 
    SET subtotal = (
        SELECT SUM(subtotal) 
        FROM Compras_detalle 
        WHERE idCompra = NEW.idCompra
    ),
    total = subtotal - descuento
    WHERE idCompra = NEW.idCompra;
END//

CREATE TRIGGER tr_compras_detalle_delete 
AFTER DELETE ON Compras_detalle
FOR EACH ROW
BEGIN
    UPDATE Compras 
    SET subtotal = COALESCE((
        SELECT SUM(subtotal) 
        FROM Compras_detalle 
        WHERE idCompra = OLD.idCompra
    ), 0),
    total = subtotal - descuento
    WHERE idCompra = OLD.idCompra;
END//

-- Trigger para actualizar totales en Ventas
CREATE TRIGGER tr_ventas_detalle_insert 
AFTER INSERT ON Ventas_detalle
FOR EACH ROW
BEGIN
    UPDATE Ventas 
    SET subtotal = (
        SELECT SUM(subtotal) 
        FROM Ventas_detalle 
        WHERE idVenta = NEW.idVenta
    ),
    total = subtotal - descuento
    WHERE idVenta = NEW.idVenta;
END//

CREATE TRIGGER tr_ventas_detalle_update 
AFTER UPDATE ON Ventas_detalle
FOR EACH ROW
BEGIN
    UPDATE Ventas 
    SET subtotal = (
        SELECT SUM(subtotal) 
        FROM Ventas_detalle 
        WHERE idVenta = NEW.idVenta
    ),
    total = subtotal - descuento
    WHERE idVenta = NEW.idVenta;
END//

CREATE TRIGGER tr_ventas_detalle_delete 
AFTER DELETE ON Ventas_detalle
FOR EACH ROW
BEGIN
    UPDATE Ventas 
    SET subtotal = COALESCE((
        SELECT SUM(subtotal) 
        FROM Ventas_detalle 
        WHERE idVenta = OLD.idVenta
    ), 0),
    total = subtotal - descuento
    WHERE idVenta = OLD.idVenta;
END//

DELIMITER ;
