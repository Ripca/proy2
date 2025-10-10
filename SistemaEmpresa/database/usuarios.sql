-- Tabla de usuarios del sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    rol ENUM('Administrador', 'Usuario', 'Vendedor', 'Supervisor') NOT NULL DEFAULT 'Usuario',
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion DATE NOT NULL,
    fecha_ultimo_acceso DATE,
    INDEX idx_usuario (usuario),
    INDEX idx_activo (activo),
    INDEX idx_rol (rol)
);

-- Insertar usuario administrador por defecto
-- Contraseña: admin123 (encriptada con SHA-256)
INSERT INTO usuarios (usuario, password, nombres, apellidos, email, rol, activo, fecha_creacion) 
VALUES (
    'admin', 
    '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 
    'Administrador', 
    'del Sistema', 
    'admin@sistemaempresa.com', 
    'Administrador', 
    TRUE, 
    CURRENT_DATE
) ON DUPLICATE KEY UPDATE 
    password = '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9';

-- Insertar usuario de prueba
-- Contraseña: usuario123 (encriptada con SHA-256)
INSERT INTO usuarios (usuario, password, nombres, apellidos, email, rol, activo, fecha_creacion) 
VALUES (
    'usuario', 
    'b9c950640e1b3740e98acb93e669c65766f6670dd1609ba91ff41052ba48c6f3', 
    'Usuario', 
    'de Prueba', 
    'usuario@sistemaempresa.com', 
    'Usuario', 
    TRUE, 
    CURRENT_DATE
) ON DUPLICATE KEY UPDATE 
    password = 'b9c950640e1b3740e98acb93e669c65766f6670dd1609ba91ff41052ba48c6f3';
