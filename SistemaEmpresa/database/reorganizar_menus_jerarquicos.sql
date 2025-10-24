-- ============================================================================
-- Script para reorganizar los menús en estructura jerárquica
-- Basado en la estructura de árbol proporcionada
-- ============================================================================

-- Primero, vamos a actualizar los menús existentes para establecer sus padres
-- Manteniendo todos los menús actuales

-- 1. Dashboard - Sin padre (menú principal) - ID: 1
UPDATE menus SET id_padre = NULL, orden = 1 WHERE id_menu = 1;

-- 2. Productos - Sin padre (menú principal) - ID: 5
UPDATE menus SET id_padre = NULL, orden = 2 WHERE id_menu = 5;

-- 3. Marcas - Hijo de Productos - ID: 6
UPDATE menus SET id_padre = 5, orden = 1 WHERE id_menu = 6;

-- 4. Ventas - Sin padre (menú principal) - ID: 8
UPDATE menus SET id_padre = NULL, orden = 3 WHERE id_menu = 8;

-- 5. Clientes - Hijo de Ventas - ID: 2
UPDATE menus SET id_padre = 8, orden = 1 WHERE id_menu = 2;

-- 6. Empleados - Hijo de Ventas - ID: 3
UPDATE menus SET id_padre = 8, orden = 2 WHERE id_menu = 3;

-- 7. Puestos - Nieto de Ventas (hijo de Empleados) - ID: 4
UPDATE menus SET id_padre = 3, orden = 1 WHERE id_menu = 4;

-- 8. Compras - Sin padre (menú principal) - ID: 9
UPDATE menus SET id_padre = NULL, orden = 4 WHERE id_menu = 9;

-- 9. Proveedores - Hijo de Compras - ID: 7
UPDATE menus SET id_padre = 9, orden = 1 WHERE id_menu = 7;

-- 10. Reportes - Nuevo menú principal (sin URL, solo contenedor)
-- Verificar si existe, si no, insertarlo
INSERT INTO menus (nombre, icono, url, id_padre, orden, estado, fecha_creacion)
SELECT 'Reportes', 'fas fa-chart-bar', '', NULL, 5, 1, NOW()
WHERE NOT EXISTS (SELECT 1 FROM menus WHERE nombre = 'Reportes');

-- ============================================================================
-- Verificación: Mostrar la estructura jerárquica resultante
-- ============================================================================
SELECT 
    CONCAT(REPEAT('  ', CASE WHEN m1.id_padre IS NULL THEN 0 ELSE 1 END), 
           CASE WHEN m2.id_menu IS NOT NULL THEN '└─ ' ELSE '   ' END,
           m1.nombre) as estructura,
    m1.id_menu,
    m1.id_padre,
    m1.orden,
    m1.icono,
    m1.url
FROM menus m1
LEFT JOIN menus m2 ON m1.id_padre = m2.id_menu
ORDER BY m1.id_padre, m1.orden;

-- ============================================================================
-- Notas importantes:
-- ============================================================================
-- 1. Los menús sin URL (como Productos, Ventas, Compras, Reportes) actúan como contenedores
-- 2. Los menús con URL son los que tienen acciones directas (servlets)
-- 3. La estructura soporta hasta 3 niveles: Principal -> Secundario -> Terciario
-- 4. El campo 'orden' controla el orden de visualización dentro de cada nivel
-- 5. El campo 'estado' = 1 significa que el menú está activo
-- ============================================================================

