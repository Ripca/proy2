-- ===============================
-- DATOS INICIALES PUNTO DE VENTA
-- ===============================

-- Usar la base de datos
USE PuntoVenta;

-- ===============================
-- DATOS BÁSICOS DEL SISTEMA
-- ===============================

-- Puestos
INSERT INTO Puestos (puesto, descripcion, salario_base) VALUES 
('Cajero', 'Encargado de cobros y atención al cliente', 3500.00),
('Vendedor', 'Encargado de ventas y atención al cliente', 4000.00),
('Supervisor', 'Supervisión de operaciones diarias', 6000.00),
('Administrador', 'Administración general del sistema', 8000.00),
('Gerente', 'Gerencia general', 12000.00);

-- Marcas
INSERT INTO Marcas (marca, descripcion, pais_origen) VALUES 
('Samsung', 'Electrónicos y electrodomésticos', 'Corea del Sur'),
('LG', 'Electrónicos y electrodomésticos', 'Corea del Sur'),
('Sony', 'Electrónicos y entretenimiento', 'Japón'),
('Panasonic', 'Electrónicos y electrodomésticos', 'Japón'),
('Apple', 'Tecnología y dispositivos móviles', 'Estados Unidos'),
('Whirlpool', 'Electrodomésticos', 'Estados Unidos'),
('Mabe', 'Electrodomésticos', 'México'),
('Electrolux', 'Electrodomésticos', 'Suecia');

-- Proveedores
INSERT INTO Proveedores (proveedor, nit, direccion, telefono, email, contacto_principal) VALUES 
('Distribuidora Central S.A.', '1234567-8', 'Zona 1, Ciudad de Guatemala', '2234-5678', 'ventas@distcentral.com', 'Carlos Méndez'),
('Tecno Importaciones', '8765432-1', 'Zona 10, Ciudad de Guatemala', '2345-6789', 'compras@tecnoimport.com', 'Ana García'),
('Electro Hogar Ltda.', '3216549-3', 'Zona 8, Mixco', '2456-7890', 'info@electrohogar.com', 'Luis Rodríguez'),
('Mundo Electrónico', '6549873-2', 'Zona 12, Villa Nueva', '2567-8901', 'ventas@mundoelectronico.com', 'María López'),
('Importadora Digital', '9876543-0', 'Zona 4, Ciudad de Guatemala', '2678-9012', 'contacto@importadoradigital.com', 'Pedro Morales');

-- Empleados
INSERT INTO Empleados (nombres, apellidos, direccion, telefono, DPI, genero, fecha_nacimiento, idPuesto, fecha_inicio_labores, salario) VALUES 
('Juan Carlos', 'Pérez López', 'Zona 3, Guatemala', '5555-1111', '1234567890101', 1, '1990-05-15', 4, '2023-01-01', 8000.00),
('Ana María', 'Gómez Hernández', 'Zona 5, Mixco', '5555-2222', '1098765432101', 0, '1995-07-22', 3, '2023-06-01', 6000.00),
('Marco Antonio', 'Ramírez Santos', 'Zona 7, Villa Nueva', '5555-3333', '1020304050607', 1, '1988-03-12', 2, '2022-11-15', 4000.00),
('Claudia Patricia', 'Reyes Morales', 'Zona 9, Guatemala', '5555-4444', '7080901020304', 0, '1992-09-30', 1, '2023-03-10', 3500.00),
('Roberto Carlos', 'Díaz Fuentes', 'Zona 6, Mixco', '5555-5555', '5060708090102', 1, '1985-12-08', 2, '2023-02-15', 4000.00);

-- Clientes
INSERT INTO Clientes (nombres, apellidos, NIT, genero, telefono, correo_electronico, direccion) VALUES 
('Carlos Eduardo', 'López Martínez', '1234567-8', 1, '5555-3333', 'carlos.lopez@email.com', 'Zona 1, Guatemala'),
('María Fernanda', 'Hernández García', '8765432-1', 0, '5555-4444', 'maria.hernandez@email.com', 'Zona 2, Mixco'),
('Pedro José', 'Santos Rodríguez', '1111111-1', 1, '5555-5555', 'pedro.santos@email.com', 'Zona 3, Villa Nueva'),
('Lucía Isabel', 'Morales Díaz', '2222222-2', 0, '5555-6666', 'lucia.morales@email.com', 'Zona 4, Guatemala'),
('José Antonio', 'Ramírez López', '3333333-3', 1, '5555-7777', 'jose.ramirez@email.com', 'Zona 5, Mixco'),
('Carmen Rosa', 'Fuentes Pérez', '4444444-4', 0, '5555-8888', 'carmen.fuentes@email.com', 'Zona 6, Guatemala');

-- Productos
INSERT INTO Productos (producto, codigo_barras, idMarca, descripcion, imagen, precio_costo, precio_venta, existencia, stock_minimo) VALUES 
('Smart TV 55"', '7501234567890', 1, 'Televisor inteligente Samsung 55 pulgadas 4K', 'tv_samsung_55.jpg', 2500.00, 3200.00, 15, 3),
('Refrigeradora 14ft', '7501234567891', 2, 'Refrigeradora LG 14 pies cúbicos', 'refri_lg_14.jpg', 1800.00, 2500.00, 8, 2),
('Minicomponente', '7501234567892', 3, 'Minicomponente Sony 500W con Bluetooth', 'sony_minicomp.jpg', 900.00, 1200.00, 12, 3),
('Microondas 25L', '7501234567893', 4, 'Microondas Panasonic 25 litros', 'micro_panasonic.jpg', 600.00, 850.00, 20, 5),
('iPhone 14', '7501234567894', 5, 'iPhone 14 128GB', 'iphone14.jpg', 4500.00, 6000.00, 5, 2),
('Lavadora 18kg', '7501234567895', 6, 'Lavadora Whirlpool 18 kilogramos', 'lava_whirlpool.jpg', 2200.00, 2800.00, 6, 2),
('Estufa 4 quemadores', '7501234567896', 7, 'Estufa Mabe 4 quemadores', 'estufa_mabe.jpg', 1200.00, 1600.00, 10, 3),
('Aspiradora', '7501234567897', 8, 'Aspiradora Electrolux 1200W', 'aspira_electrolux.jpg', 800.00, 1100.00, 8, 2);

-- ===============================
-- USUARIOS DEL SISTEMA
-- ===============================

-- Usuarios (password: admin123, supervisor123, vendedor123, cajero123)
INSERT INTO Usuarios (usuario, password, idEmpleado, rol) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye1VdLSnqq3vyPGnxHhGGvG1lItLdstS6', 1, 'ADMIN'),
('supervisor', '$2a$10$N9qo8uLOickgx2ZMRZoMye1VdLSnqq3vyPGnxHhGGvG1lItLdstS6', 2, 'SUPERVISOR'),
('vendedor1', '$2a$10$N9qo8uLOickgx2ZMRZoMye1VdLSnqq3vyPGnxHhGGvG1lItLdstS6', 3, 'VENDEDOR'),
('cajero1', '$2a$10$N9qo8uLOickgx2ZMRZoMye1VdLSnqq3vyPGnxHhGGvG1lItLdstS6', 4, 'CAJERO'),
('vendedor2', '$2a$10$N9qo8uLOickgx2ZMRZoMye1VdLSnqq3vyPGnxHhGGvG1lItLdstS6', 5, 'VENDEDOR');

-- ===============================
-- MENÚS DEL SISTEMA
-- ===============================

-- Menús principales
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Dashboard', 'DashboardServlet', 'fas fa-tachometer-alt', 1, NULL, 'ADMIN,SUPERVISOR,VENDEDOR,CAJERO'),
('Ventas', '#', 'fas fa-shopping-cart', 2, NULL, 'ADMIN,SUPERVISOR,VENDEDOR,CAJERO'),
('Compras', '#', 'fas fa-truck', 3, NULL, 'ADMIN,SUPERVISOR'),
('Inventario', '#', 'fas fa-boxes', 4, NULL, 'ADMIN,SUPERVISOR,VENDEDOR'),
('Clientes', '#', 'fas fa-users', 5, NULL, 'ADMIN,SUPERVISOR,VENDEDOR'),
('Proveedores', '#', 'fas fa-industry', 6, NULL, 'ADMIN,SUPERVISOR'),
('Empleados', '#', 'fas fa-user-tie', 7, NULL, 'ADMIN,SUPERVISOR'),
('Configuración', '#', 'fas fa-cogs', 8, NULL, 'ADMIN'),
('Reportes', '#', 'fas fa-chart-bar', 9, NULL, 'ADMIN,SUPERVISOR');

-- Submenús de Ventas
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Nueva Venta', 'VentaServlet?action=new', 'fas fa-plus-circle', 1, 2, 'ADMIN,SUPERVISOR,VENDEDOR,CAJERO'),
('Lista de Ventas', 'VentaServlet?action=list', 'fas fa-list', 2, 2, 'ADMIN,SUPERVISOR,VENDEDOR,CAJERO');

-- Submenús de Compras
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Nueva Compra', 'CompraServlet?action=new', 'fas fa-plus-circle', 1, 3, 'ADMIN,SUPERVISOR'),
('Lista de Compras', 'CompraServlet?action=list', 'fas fa-list', 2, 3, 'ADMIN,SUPERVISOR');

-- Submenús de Inventario
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Productos', 'ProductoServlet?action=list', 'fas fa-box', 1, 4, 'ADMIN,SUPERVISOR,VENDEDOR'),
('Marcas', 'MarcaServlet?action=list', 'fas fa-tags', 2, 4, 'ADMIN,SUPERVISOR');

-- Submenús de Clientes
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Lista de Clientes', 'ClienteServlet?action=list', 'fas fa-list', 1, 5, 'ADMIN,SUPERVISOR,VENDEDOR'),
('Nuevo Cliente', 'ClienteServlet?action=new', 'fas fa-user-plus', 2, 5, 'ADMIN,SUPERVISOR,VENDEDOR');

-- Submenús de Proveedores
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Lista de Proveedores', 'ProveedorServlet?action=list', 'fas fa-list', 1, 6, 'ADMIN,SUPERVISOR'),
('Nuevo Proveedor', 'ProveedorServlet?action=new', 'fas fa-plus', 2, 6, 'ADMIN,SUPERVISOR');

-- Submenús de Empleados
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Lista de Empleados', 'EmpleadoServlet?action=list', 'fas fa-list', 1, 7, 'ADMIN,SUPERVISOR'),
('Nuevo Empleado', 'EmpleadoServlet?action=new', 'fas fa-user-plus', 2, 7, 'ADMIN,SUPERVISOR'),
('Puestos', 'PuestoServlet?action=list', 'fas fa-briefcase', 3, 7, 'ADMIN,SUPERVISOR');

-- Submenús de Configuración
INSERT INTO Menus (nombre, url, icono, orden, idMenuPadre, roles_permitidos) VALUES 
('Usuarios', 'UsuarioServlet?action=list', 'fas fa-users-cog', 1, 8, 'ADMIN'),
('Menús', 'MenuServlet?action=list', 'fas fa-sitemap', 2, 8, 'ADMIN'),
('Carrusel', 'CarruselServlet?action=list', 'fas fa-images', 3, 8, 'ADMIN');

-- ===============================
-- IMÁGENES DEL CARRUSEL
-- ===============================

INSERT INTO CarruselImagenes (titulo, descripcion, imagen, url_enlace, orden) VALUES 
('Bienvenido al Sistema de Punto de Venta', 'Gestiona tu negocio de manera eficiente', 'carousel1.jpg', '#', 1),
('Nuevos Productos Disponibles', 'Revisa nuestro catálogo actualizado', 'carousel2.jpg', 'ProductoServlet?action=list', 2),
('Ofertas Especiales', 'No te pierdas nuestras promociones', 'carousel3.jpg', '#', 3);

-- ===============================
-- DATOS DE EJEMPLO PARA TRANSACCIONES
-- ===============================

-- Compras de ejemplo
INSERT INTO Compras (no_orden_compra, idProveedor, fecha_orden, fecha_entrega, estado, observaciones, idUsuario) VALUES 
(1001, 1, '2024-01-15', '2024-01-20', 'RECIBIDA', 'Primera compra del año', 1),
(1002, 2, '2024-01-20', '2024-01-25', 'RECIBIDA', 'Compra de productos tecnológicos', 1),
(1003, 3, '2024-02-01', NULL, 'PENDIENTE', 'Pendiente de entrega', 2);

-- Detalles de compras
INSERT INTO Compras_detalle (idCompra, idProducto, cantidad, precio_unitario, subtotal) VALUES 
(1, 1, 5, 2500.00, 12500.00),
(1, 2, 3, 1800.00, 5400.00),
(2, 3, 6, 900.00, 5400.00),
(2, 4, 4, 600.00, 2400.00),
(3, 5, 2, 4500.00, 9000.00);

-- Ventas de ejemplo
INSERT INTO Ventas (noFactura, serie, fechaFactura, idCliente, idEmpleado, metodo_pago, estado, idUsuario) VALUES 
(2001, 'A', '2024-01-25', 1, 3, 'EFECTIVO', 'COMPLETADA', 3),
(2002, 'A', '2024-01-26', 2, 4, 'TARJETA', 'COMPLETADA', 4),
(2003, 'A', '2024-02-01', 3, 3, 'TRANSFERENCIA', 'COMPLETADA', 3);

-- Detalles de ventas
INSERT INTO Ventas_detalle (idVenta, idProducto, cantidad, precio_unitario, subtotal) VALUES 
(1, 1, 1, 3200.00, 3200.00),
(1, 4, 2, 850.00, 1700.00),
(2, 2, 1, 2500.00, 2500.00),
(3, 3, 1, 1200.00, 1200.00),
(3, 8, 1, 1100.00, 1100.00);

-- ===============================
-- ACTUALIZAR EXISTENCIAS DESPUÉS DE TRANSACCIONES
-- ===============================

-- Actualizar existencias después de las compras
UPDATE Productos SET existencia = existencia + 5 WHERE idProducto = 1;
UPDATE Productos SET existencia = existencia + 3 WHERE idProducto = 2;
UPDATE Productos SET existencia = existencia + 6 WHERE idProducto = 3;
UPDATE Productos SET existencia = existencia + 4 WHERE idProducto = 4;
UPDATE Productos SET existencia = existencia + 2 WHERE idProducto = 5;

-- Actualizar existencias después de las ventas
UPDATE Productos SET existencia = existencia - 1 WHERE idProducto = 1;
UPDATE Productos SET existencia = existencia - 2 WHERE idProducto = 4;
UPDATE Productos SET existencia = existencia - 1 WHERE idProducto = 2;
UPDATE Productos SET existencia = existencia - 1 WHERE idProducto = 3;
UPDATE Productos SET existencia = existencia - 1 WHERE idProducto = 8;
