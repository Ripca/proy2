-- =====================================================
-- SCRIPT PARA AGREGAR TABLA DE MENÚS DINÁMICOS Y CARRUSEL
-- Sistema Empresa - Actualización de Base de Datos
-- =====================================================

USE sistema-empresa;

-- =====================================================
-- TABLA PARA MENÚS DINÁMICOS
-- =====================================================
CREATE TABLE IF NOT EXISTS menus (
    id_menu INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    icono VARCHAR(50) NOT NULL COMMENT 'Clase de Font Awesome (ej: fas fa-users)',
    url VARCHAR(100) NOT NULL COMMENT 'Ruta relativa (ej: ClienteServlet)',
    id_padre INT NULL COMMENT 'ID del menú padre para submenús',
    orden INT NOT NULL DEFAULT 0 COMMENT 'Orden de visualización',
    estado TINYINT(1) NOT NULL DEFAULT 1 COMMENT '1=Activo, 0=Inactivo',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_padre) REFERENCES menus(id_menu) ON DELETE CASCADE
);

-- =====================================================
-- DATOS INICIALES PARA MENÚS
-- =====================================================
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado) VALUES
-- Menús principales
('Dashboard', 'fas fa-tachometer-alt', 'DashboardServlet', NULL, 1, 1),
('Clientes', 'fas fa-users', 'ClienteServlet', NULL, 2, 1),
('Empleados', 'fas fa-user-tie', 'EmpleadoServlet', NULL, 3, 1),
('Puestos', 'fas fa-briefcase', 'PuestoServlet', NULL, 4, 1),
('Productos', 'fas fa-box', 'ProductoServlet', NULL, 5, 1),
('Marcas', 'fas fa-tags', 'MarcaServlet', NULL, 6, 1),
('Proveedores', 'fas fa-truck', 'ProveedorServlet', NULL, 7, 1),
('Ventas', 'fas fa-shopping-cart', 'VentaServlet', NULL, 8, 1),
('Compras', 'fas fa-shopping-bag', 'CompraServlet', NULL, 9, 1),
('Reportes', 'fas fa-chart-bar', '#', NULL, 10, 1);

-- Submenús de Reportes (ejemplo)
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado) VALUES
('Reporte Ventas', 'fas fa-chart-line', 'ReporteVentasServlet', 10, 1, 1),
('Reporte Compras', 'fas fa-chart-pie', 'ReporteComprasServlet', 10, 2, 1),
('Reporte Inventario', 'fas fa-boxes', 'ReporteInventarioServlet', 10, 3, 1);

-- =====================================================
-- TABLA PARA CARRUSEL DE IMÁGENES
-- =====================================================
CREATE TABLE IF NOT EXISTS carrusel_imagenes (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL COMMENT 'Título descriptivo de la imagen',
    url_imagen TEXT NOT NULL COMMENT 'URL completa de la imagen',
    descripcion TEXT NULL COMMENT 'Descripción opcional de la imagen',
    orden INT NOT NULL DEFAULT 0 COMMENT 'Orden de visualización en el carrusel',
    estado TINYINT(1) NOT NULL DEFAULT 1 COMMENT '1=Activo, 0=Inactivo',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =====================================================
-- DATOS INICIALES PARA CARRUSEL
-- =====================================================
INSERT INTO carrusel_imagenes (titulo, url_imagen, descripcion, orden, estado) VALUES
('Productos Variados', 
 'https://d1ih8jugeo2m5m.cloudfront.net/2025/05/productos_tipos.webp?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD',
 'Amplia variedad de productos para todas las necesidades',
 1, 1),

('Productos Saludables', 
 'https://es.starkist.com/wp-content/uploads/2020/03/healthy_living_lineup_910x445.jpg',
 'Línea de productos para una vida saludable',
 2, 1),

('Productos en Mercadotecnia', 
 'https://uneg.edu.mx/wp-content/uploads/2024/09/producto-en-mercadotecnia.jpg',
 'Estrategias de mercadotecnia para productos exitosos',
 3, 1),

('Productos Coca-Cola FEMSA', 
 'https://cdn-3.expansion.mx/dims4/default/592b0f1/2147483647/strip/true/crop/1200x630+0+0/resize/1200x630!/format/webp/quality/60/?url=https%3A%2F%2Fcdn-3.expansion.mx%2F62%2Fc9%2Fb4fa9d784717b9d10424390183ca%2Fproductos-coca-cola-femsa.jpeg',
 'Productos de la marca Coca-Cola FEMSA',
 4, 1);

-- =====================================================
-- CONSULTAS DE VERIFICACIÓN
-- =====================================================

-- Verificar menús creados
SELECT 
    m1.id_menu,
    m1.nombre,
    m1.icono,
    m1.url,
    m2.nombre AS menu_padre,
    m1.orden,
    m1.estado
FROM menus m1
LEFT JOIN menus m2 ON m1.id_padre = m2.id_menu
ORDER BY m1.id_padre, m1.orden;

-- Verificar imágenes del carrusel
SELECT 
    id_imagen,
    titulo,
    LEFT(url_imagen, 50) AS url_corta,
    orden,
    estado,
    fecha_creacion
FROM carrusel_imagenes
ORDER BY orden;

-- =====================================================
-- COMENTARIOS FINALES
-- =====================================================
/*
INSTRUCCIONES DE USO:

1. MENÚS DINÁMICOS:
   - La tabla 'menus' permite crear menús jerárquicos
   - Campo 'id_padre' NULL = menú principal
   - Campo 'id_padre' con valor = submenú
   - Campo 'orden' define la secuencia de visualización
   - Campo 'estado' permite activar/desactivar menús

2. CARRUSEL:
   - La tabla 'carrusel_imagenes' almacena las imágenes del carrusel
   - Campo 'orden' define la secuencia de visualización
   - Campo 'estado' permite activar/desactivar imágenes
   - URLs externas son soportadas

3. PRÓXIMOS PASOS:
   - Crear MenuDAO.java para consultar menús
   - Crear CarruselDAO.java para consultar imágenes
   - Actualizar dashboard_simple.jsp para cargar datos dinámicamente
   - Crear sidebar.jsp con menú lateral dinámico
*/
