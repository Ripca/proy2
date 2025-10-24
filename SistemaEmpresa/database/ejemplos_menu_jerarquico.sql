-- ============================================================================
-- Ejemplos de cómo trabajar con el menú jerárquico
-- ============================================================================

-- ============================================================================
-- 1. VER LA ESTRUCTURA ACTUAL DEL MENÚ
-- ============================================================================
SELECT 
    CONCAT(REPEAT('  ', CASE WHEN m1.id_padre IS NULL THEN 0 ELSE 1 END), 
           CASE WHEN m2.id_menu IS NOT NULL THEN '└─ ' ELSE '   ' END,
           m1.nombre) as estructura,
    m1.id_menu,
    m1.id_padre,
    m1.orden,
    m1.icono,
    m1.url,
    m1.estado
FROM menus m1
LEFT JOIN menus m2 ON m1.id_padre = m2.id_menu
ORDER BY m1.id_padre, m1.orden;

-- ============================================================================
-- 2. AGREGAR UN NUEVO MENÚ COMO HIJO DE PRODUCTOS
-- ============================================================================
-- Agregar "Categorías" como hijo de Productos (ID: 5)
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado, fecha_creacion)
VALUES ('Categorías', 'fas fa-list', 'CategoriaServlet', 5, 2, 1, NOW());

-- Verificar que se agregó correctamente
SELECT * FROM menus WHERE nombre = 'Categorías';

-- ============================================================================
-- 3. AGREGAR UN NUEVO MENÚ COMO HIJO DE VENTAS
-- ============================================================================
-- Agregar "Reportes de Ventas" como hijo de Ventas (ID: 8)
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado, fecha_creacion)
VALUES ('Reportes de Ventas', 'fas fa-chart-line', 'ReporteVentasServlet', 8, 3, 1, NOW());

-- ============================================================================
-- 4. AGREGAR UN NUEVO MENÚ COMO NIETO (HIJO DE HIJO)
-- ============================================================================
-- Primero, obtener el ID del menú "Reportes de Ventas" que acabamos de crear
-- Luego agregar un hijo a ese menú
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado, fecha_creacion)
VALUES ('Ventas por Período', 'fas fa-calendar', 'ReporteVentasPeriodoServlet', 
        (SELECT id_menu FROM menus WHERE nombre = 'Reportes de Ventas' LIMIT 1), 
        1, 1, NOW());

-- ============================================================================
-- 5. MOVER UN MENÚ A OTRO PADRE
-- ============================================================================
-- Mover "Puestos" de ser hijo de "Empleados" a ser hijo de "Productos"
UPDATE menus SET id_padre = 5, orden = 3 WHERE id_menu = 4;

-- Verificar el cambio
SELECT * FROM menus WHERE id_menu = 4;

-- ============================================================================
-- 6. CAMBIAR EL ORDEN DE UN MENÚ
-- ============================================================================
-- Cambiar el orden de "Marcas" para que aparezca después de "Categorías"
UPDATE menus SET orden = 3 WHERE id_menu = 6;

-- Cambiar el orden de "Categorías" para que aparezca primero
UPDATE menus SET orden = 1 WHERE nombre = 'Categorías';

-- ============================================================================
-- 7. DESACTIVAR UN MENÚ (SIN ELIMINARLO)
-- ============================================================================
-- Desactivar el menú "Reportes"
UPDATE menus SET estado = 0 WHERE nombre = 'Reportes';

-- Reactivar el menú "Reportes"
UPDATE menus SET estado = 1 WHERE nombre = 'Reportes';

-- ============================================================================
-- 8. CREAR UN MENÚ CONTENEDOR (SIN URL)
-- ============================================================================
-- Crear un menú contenedor "Administración" sin URL
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado, fecha_creacion)
VALUES ('Administración', 'fas fa-cog', '', NULL, 6, 1, NOW());

-- Agregar menús hijos a "Administración"
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado, fecha_creacion)
VALUES ('Usuarios', 'fas fa-users', 'UsuarioServlet', 
        (SELECT id_menu FROM menus WHERE nombre = 'Administración' LIMIT 1), 
        1, 1, NOW());

INSERT INTO menus (nombre, icono, url, id_padre, orden, estado, fecha_creacion)
VALUES ('Configuración', 'fas fa-sliders-h', 'ConfiguracionServlet', 
        (SELECT id_menu FROM menus WHERE nombre = 'Administración' LIMIT 1), 
        2, 1, NOW());

-- ============================================================================
-- 9. OBTENER TODOS LOS HIJOS DE UN MENÚ ESPECÍFICO
-- ============================================================================
-- Obtener todos los hijos de "Ventas" (ID: 8)
SELECT * FROM menus WHERE id_padre = 8 ORDER BY orden;

-- ============================================================================
-- 10. OBTENER TODOS LOS MENÚS PRINCIPALES (SIN PADRE)
-- ============================================================================
SELECT * FROM menus WHERE id_padre IS NULL AND estado = 1 ORDER BY orden;

-- ============================================================================
-- 11. OBTENER LA PROFUNDIDAD DE UN MENÚ
-- ============================================================================
-- Contar cuántos niveles tiene un menú específico
WITH RECURSIVE menu_tree AS (
    SELECT id_menu, nombre, id_padre, 0 as nivel
    FROM menus
    WHERE id_padre IS NULL
    
    UNION ALL
    
    SELECT m.id_menu, m.nombre, m.id_padre, mt.nivel + 1
    FROM menus m
    INNER JOIN menu_tree mt ON m.id_padre = mt.id_menu
)
SELECT id_menu, nombre, nivel FROM menu_tree ORDER BY nivel, id_menu;

-- ============================================================================
-- 12. ELIMINAR UN MENÚ Y SUS HIJOS
-- ============================================================================
-- ADVERTENCIA: Esto eliminará el menú y todos sus hijos
-- Primero, obtener el ID del menú a eliminar
-- DELETE FROM menus WHERE id_menu = 10;

-- ============================================================================
-- 13. CAMBIAR EL ICONO DE UN MENÚ
-- ============================================================================
-- Cambiar el icono de "Productos"
UPDATE menus SET icono = 'fas fa-shopping-bag' WHERE id_menu = 5;

-- ============================================================================
-- 14. CAMBIAR LA URL DE UN MENÚ
-- ============================================================================
-- Cambiar la URL de "Clientes"
UPDATE menus SET url = 'ClienteServlet' WHERE id_menu = 2;

-- ============================================================================
-- 15. OBTENER MENÚS POR NIVEL
-- ============================================================================
-- Obtener todos los menús de nivel 1 (principales)
SELECT * FROM menus WHERE id_padre IS NULL ORDER BY orden;

-- Obtener todos los menús de nivel 2 (secundarios)
SELECT m.* FROM menus m
WHERE m.id_padre IS NOT NULL 
AND m.id_padre IN (SELECT id_menu FROM menus WHERE id_padre IS NULL)
ORDER BY m.id_padre, m.orden;

-- ============================================================================
-- NOTAS IMPORTANTES:
-- ============================================================================
-- 1. El campo 'orden' controla la posición dentro de cada nivel
-- 2. El campo 'estado' = 1 significa que el menú está activo
-- 3. El campo 'id_padre' NULL significa que es un menú principal
-- 4. Los menús sin URL actúan como contenedores
-- 5. Siempre verifica la estructura después de hacer cambios
-- ============================================================================

