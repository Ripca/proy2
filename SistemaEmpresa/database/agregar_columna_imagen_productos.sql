-- Script para agregar columna imagen a la tabla productos
-- Autor: Sistema Empresa
-- Fecha: 2025-10-30

USE sistema_empresa;

-- Agregar columna imagen si no existe
ALTER TABLE productos ADD COLUMN IF NOT EXISTS imagen VARCHAR(255) NULL AFTER descripcion;

-- Crear índice para mejor rendimiento
CREATE INDEX IF NOT EXISTS idx_productos_imagen ON productos(imagen);

-- Mensaje de confirmación
SELECT 'Columna imagen agregada a la tabla productos' as 'Estado';

