-- Script para actualizar la tabla ventas_detalle para que cantidad sea VARCHAR(45)
-- Esto es necesario para mantener compatibilidad exacta con el proyecto C# RepoPro

USE sistemaempresa;

-- =====================================================
-- ACTUALIZAR ESTRUCTURA DE VENTAS_DETALLE
-- =====================================================

-- Primero, verificar si la tabla existe y tiene datos
SELECT COUNT(*) as total_registros FROM ventas_detalle;

-- Modificar la columna cantidad de INT a VARCHAR(45)
-- Esto mantiene compatibilidad exacta con el proyecto C# donde cantidad es string
ALTER TABLE ventas_detalle 
MODIFY COLUMN cantidad VARCHAR(45) NOT NULL;

-- Verificar el cambio
DESCRIBE ventas_detalle;

-- =====================================================
-- ACTUALIZAR DATOS EXISTENTES (si los hay)
-- =====================================================

-- Los datos INT existentes se convertirán automáticamente a VARCHAR
-- Verificar que los datos se mantuvieron correctamente
SELECT id_venta_detalle, id_venta, id_producto, cantidad, precio_unitario 
FROM ventas_detalle 
LIMIT 10;

-- =====================================================
-- COMENTARIOS
-- =====================================================

/*
CAMBIO REALIZADO:
- Campo cantidad en ventas_detalle cambiado de INT a VARCHAR(45)
- Esto mantiene compatibilidad exacta con el proyecto C# RepoPro
- En el C# el campo cantidad es string: "string cantidad;"

RAZÓN DEL CAMBIO:
- En el proyecto C# (repp/vista/Venta.h), la estructura DetalleVenta 
  tiene: "string cantidad;" (línea 14)
- Para mantener compatibilidad exacta, el campo debe ser VARCHAR en Java

IMPACTO:
- Los DAOs y modelos Java deben manejar cantidad como String
- Las validaciones deben convertir String a número cuando sea necesario
- Los cálculos deben usar Integer.parseInt() o similar
*/
