-- Crear la base de datos
CREATE DATABASE PuntoVenta;

-- Usar la base de datos creada
USE PuntoVenta;


-- Crear tabla Puestos
CREATE TABLE Puestos (
    idPuesto SMALLINT AUTO_INCREMENT PRIMARY KEY,
    puesto VARCHAR(50)
);


-- Crear tabla Marcas
CREATE TABLE Marcas (
    idmarca SMALLINT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50)
);


-- Crear tabla Proveedores
CREATE TABLE Proveedores (
    idProveedor INT AUTO_INCREMENT PRIMARY KEY,
    proveedor VARCHAR(60),
    nit VARCHAR(12),
    direccion VARCHAR(80),
    telefono VARCHAR(25)
);


-- Crear tabla Productos
CREATE TABLE Productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    producto VARCHAR(50),
    idMarca SMALLINT,
    descripcion VARCHAR(100),
    imagen VARCHAR(30),
    precio_costo DECIMAL(8,2),
    precio_venta DECIMAL(8,2),
    existencia INT,
    fecha_ingreso DATETIME,
    FOREIGN KEY (idMarca) REFERENCES Marcas(idmarca)
);


-- Crear tabla Empleados
CREATE TABLE Empleados (
    idEmpleado INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60),
    apellidos VARCHAR(60),
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


-- Crear tabla Clientes
CREATE TABLE Clientes (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(60),
    apellidos VARCHAR(60),
    NIT VARCHAR(12),
    genero BIT,
    telefono VARCHAR(25),
    correo_electronico VARCHAR(45),
    fecha_ingreso DATETIME
);


-- Crear tabla Compras
CREATE TABLE Compras (
    idcompra INT AUTO_INCREMENT PRIMARY KEY,
    no_order_compra INT,
    idproveedor INT,
    fecha_order DATE,
    fecha_ingreso DATETIME,
    FOREIGN KEY (idproveedor) REFERENCES Proveedores(idProveedor)
);


-- Crear tabla Compras_detalle
CREATE TABLE Compras_detalle (
    idcompra_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idcompra INT,
    idproducto INT,
    cantidad INT,
    precio_unitario DECIMAL(8,2),
    FOREIGN KEY (idcompra) REFERENCES Compras(idcompra),
    FOREIGN KEY (idproducto) REFERENCES Productos(idProducto)
);


-- Crear tabla Ventas
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

-- Crear tabla Ventas_detalle
CREATE TABLE Ventas_detalle (
    idventa_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    idventa INT,
    idProducto INT,
    cantidad VARCHAR(45),
    precio_unitario DECIMAL(8,2),
    FOREIGN KEY (idventa) REFERENCES Ventas(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);
