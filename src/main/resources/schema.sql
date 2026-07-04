-- TrazaAlimentos Database Schema
-- Version 2.0 - Con Entidades Certificadoras, Eventos, Solicitudes y Certificados

CREATE DATABASE IF NOT EXISTS trazaalimentos;
USE trazaalimentos;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('PRODUCTOR', 'DISTRIBUIDOR', 'COMERCIANTE', 'ADMIN', 'EVALUADOR', 'INSPECTOR') NOT NULL,
    empresa VARCHAR(255),
    documento_identidad VARCHAR(50) UNIQUE,
    telefono VARCHAR(20),
    direccion TEXT,
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    latitud DOUBLE,
    longitud DOUBLE,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_rol (rol),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Entidades Certificadoras
CREATE TABLE IF NOT EXISTS entidad_certificadora (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    ruc VARCHAR(50) NOT NULL UNIQUE,
    direccion TEXT NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    logo LONGBLOB,
    tipos_certificacion VARCHAR(1000),
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_nombre (nombre),
    INDEX idx_ruc (ruc),
    INDEX idx_correo (correo),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Productos
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    descripcion TEXT,
    tipo_producto VARCHAR(100),
    variedad VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Lotes
CREATE TABLE IF NOT EXISTS lotes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    codigo_lote VARCHAR(100) NOT NULL UNIQUE,
    fecha_siembra DATE NOT NULL,
    cantidad_producida DECIMAL(10, 2),
    unidad_medida VARCHAR(50),
    ubicacion VARCHAR(255),
    latitud DOUBLE,
    longitud DOUBLE,
    estado_cultivo VARCHAR(100),
    hash_inicial VARCHAR(256),
    qr_code LONGBLOB,
    qr_url VARCHAR(500),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_codigo_lote (codigo_lote),
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_producto_id (producto_id),
    INDEX idx_fecha_siembra (fecha_siembra)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Bloques (Blockchain Simulado)
CREATE TABLE IF NOT EXISTS bloques (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lote_id BIGINT NOT NULL,
    numero_bloque INT NOT NULL,
    hash_actual VARCHAR(256) NOT NULL,
    hash_anterior VARCHAR(256),
    data TEXT NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lote_id) REFERENCES lotes(id) ON DELETE CASCADE,
    INDEX idx_lote_id (lote_id),
    INDEX idx_hash_actual (hash_actual),
    UNIQUE KEY unique_lote_bloque (lote_id, numero_bloque)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Eventos de Trazabilidad
CREATE TABLE IF NOT EXISTS evento_trazabilidad (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lote_id BIGINT NOT NULL,
    tipo_evento ENUM('SIEMBRA', 'FERTILIZACION', 'RIEGO', 'CONTROL_SANITARIO', 'COSECHA', 'TRANSPORTE', 'DISTRIBUCION', 'COMERCIALIZACION', 'OTRA') NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_evento TIMESTAMP NOT NULL,
    ubicacion TEXT,
    observaciones TEXT,
    hash_anterior VARCHAR(256),
    hash_actual VARCHAR(256) NOT NULL,
    usuario_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lote_id) REFERENCES lotes(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    INDEX idx_lote_id (lote_id),
    INDEX idx_tipo_evento (tipo_evento),
    INDEX idx_fecha_evento (fecha_evento),
    INDEX idx_usuario_id (usuario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Solicitudes de Certificación
CREATE TABLE IF NOT EXISTS solicitud_certificacion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lote_id BIGINT NOT NULL,
    entidad_certificadora_id BIGINT NOT NULL,
    productor_id BIGINT NOT NULL,
    estado ENUM('PENDIENTE', 'EN_EVALUACION', 'APROBADA', 'RECHAZADA', 'OBSERVACIONES') DEFAULT 'PENDIENTE',
    observaciones TEXT,
    razon_rechazo TEXT,
    fecha_solicitud TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_evaluacion TIMESTAMP,
    evaluador_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lote_id) REFERENCES lotes(id) ON DELETE CASCADE,
    FOREIGN KEY (entidad_certificadora_id) REFERENCES entidad_certificadora(id) ON DELETE CASCADE,
    FOREIGN KEY (productor_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (evaluador_id) REFERENCES usuarios(id),
    INDEX idx_lote_id (lote_id),
    INDEX idx_entidad_id (entidad_certificadora_id),
    INDEX idx_productor_id (productor_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_solicitud (fecha_solicitud),
    UNIQUE KEY unique_lote_entidad (lote_id, entidad_certificadora_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Certificados
CREATE TABLE IF NOT EXISTS certificado (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero_certificado VARCHAR(100) NOT NULL UNIQUE,
    lote_id BIGINT NOT NULL,
    entidad_certificadora_id BIGINT NOT NULL,
    solicitud_certificacion_id BIGINT,
    tipo_certificacion VARCHAR(100) NOT NULL,
    fecha_emision DATE NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    documento_pdf LONGBLOB,
    url_documento VARCHAR(500),
    detalles TEXT,
    hash_bloque VARCHAR(256) NOT NULL,
    estado ENUM('ACTIVO', 'VENCIDO', 'REVOCADO', 'SUSPENDIDO') DEFAULT 'ACTIVO',
    usuario_emisor_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lote_id) REFERENCES lotes(id) ON DELETE CASCADE,
    FOREIGN KEY (entidad_certificadora_id) REFERENCES entidad_certificadora(id) ON DELETE CASCADE,
    FOREIGN KEY (solicitud_certificacion_id) REFERENCES solicitud_certificacion(id),
    FOREIGN KEY (usuario_emisor_id) REFERENCES usuarios(id),
    INDEX idx_numero (numero_certificado),
    INDEX idx_lote_id (lote_id),
    INDEX idx_entidad_id (entidad_certificadora_id),
    INDEX idx_estado (estado),
    INDEX idx_fecha_vencimiento (fecha_vencimiento)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Transacciones (Distribuidor - Comerciante)
CREATE TABLE IF NOT EXISTS transacciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lote_id BIGINT NOT NULL,
    tipo_transaccion ENUM('DISTRIBUIDOR', 'COMERCIANTE') NOT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_recepcion TIMESTAMP,
    vehiculo VARCHAR(100),
    responsable_transporte VARCHAR(255),
    condiciones_traslado TEXT,
    destino VARCHAR(255),
    punto_venta VARCHAR(255),
    estado_producto VARCHAR(100),
    disponibilidad_venta BOOLEAN,
    hash_bloque VARCHAR(256),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (lote_id) REFERENCES lotes(id) ON DELETE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_lote_id (lote_id),
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_tipo_transaccion (tipo_transaccion),
    INDEX idx_fecha_recepcion (fecha_recepcion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Comerciantes
CREATE TABLE IF NOT EXISTS comerciante (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    empresa VARCHAR(255) NOT NULL,
    documento_identidad VARCHAR(50) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    direccion TEXT NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    latitud DOUBLE NOT NULL,
    longitud DOUBLE NOT NULL,
    puntos_venta VARCHAR(500),
    descripcion_negocio TEXT,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_usuario_id (usuario_id),
    INDEX idx_documento (documento_identidad),
    INDEX idx_empresa (empresa),
    INDEX idx_activo (activo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- INSERTS DE EJEMPLO

-- Insertar Entidades Certificadoras
INSERT INTO entidad_certificadora (nombre, ruc, direccion, correo, telefono, tipos_certificacion, descripcion, activo)
VALUES 
('Control Union', '12345678901', 'Av. Principal 123, Lima, Perú', 'info@controlunion.com', '+51 999 123 456', 'Orgánica, Fair Trade, Rainforest', 'Certificadora internacional', TRUE),
('CERES', '98765432101', 'Calle Secundaria 456, Arequipa, Perú', 'contacto@ceres.com', '+51 999 654 321', 'Orgánica, Sostenible', 'Certificadora local', TRUE),
('Bio Latina', '55555555501', 'Paseo del Bosque 789, Cusco, Perú', 'support@biolatina.com', '+51 999 789 012', 'Orgánica, Biodinámica', 'Certificadora especializada', TRUE);

-- Insertar Usuario Productor de Ejemplo
INSERT INTO usuarios (nombre, email, password, rol, empresa, documento_identidad, telefono, direccion, ciudad, pais, latitud, longitud, activo)
VALUES 
('Juan Pérez', 'juan@productor.com', 'hashed_password_123', 'PRODUCTOR', 'Granja La Esperanza', '12345678', '+51 999 111 111', 'Jr. Agrícola 100', 'Lima', 'Perú', -12.0462, -77.0428, TRUE),
('María García', 'maria@distribuidor.com', 'hashed_password_456', 'DISTRIBUIDOR', 'Distribuidora Premium', '87654321', '+51 999 222 222', 'Av. Transporte 200', 'Lima', 'Perú', -12.0500, -77.0500, TRUE),
('Carlos Rodríguez', 'carlos@comerciante.com', 'hashed_password_789', 'COMERCIANTE', 'Supermercado Central', '11111111', '+51 999 333 333', 'Calle Comercio 300', 'Lima', 'Perú', -12.0400, -77.0300, TRUE),
('Admin Sistema', 'admin@trazaalimentos.com', 'hashed_admin_pass', 'ADMIN', 'TrazaAlimentos', '99999999', '+51 999 000 000', 'Lima', 'Lima', 'Perú', -12.0462, -77.0428, TRUE);

-- Insertar Usuario Evaluador de Ejemplo
INSERT INTO usuarios (nombre, email, password, rol, empresa, documento_identidad, telefono, direccion, ciudad, pais, activo)
VALUES 
('Inspector Flores', 'inspector@controlunion.com', 'hashed_inspector', 'INSPECTOR', 'Control Union', '44444444', '+51 999 444 444', 'Av. Principal 123', 'Lima', 'Perú', TRUE),
('Evaluador López', 'evaluador@ceres.com', 'hashed_evaluador', 'EVALUADOR', 'CERES', '55555555', '+51 999 555 555', 'Calle Secundaria 456', 'Arequipa', 'Perú', TRUE);

-- Insertar Productos de Ejemplo
INSERT INTO productos (usuario_id, nombre, descripcion, tipo_producto, variedad)
VALUES 
((SELECT id FROM usuarios WHERE email = 'juan@productor.com'), 'Palta', 'Palta orgánica de calidad premium', 'Fruta', 'Hass'),
((SELECT id FROM usuarios WHERE email = 'juan@productor.com'), 'Café', 'Café de altura orgánico', 'Grano', 'Arábica');

-- Insertar Lotes de Ejemplo
INSERT INTO lotes (usuario_id, producto_id, codigo_lote, fecha_siembra, cantidad_producida, unidad_medida, ubicacion, latitud, longitud, estado_cultivo, activo)
VALUES 
((SELECT id FROM usuarios WHERE email = 'juan@productor.com'), 
 (SELECT id FROM productos WHERE nombre = 'Palta' LIMIT 1),
 'LT-2024-001',
 '2024-01-15',
 5000,
 'kg',
 'Huaral, Lima',
 -11.4086,
 -77.2500,
 'En cosecha',
 TRUE);

-- Insertar Comerciante de Ejemplo
INSERT INTO comerciante (usuario_id, nombre, empresa, documento_identidad, telefono, direccion, ciudad, pais, latitud, longitud, puntos_venta, descripcion_negocio, activo)
VALUES 
((SELECT id FROM usuarios WHERE email = 'carlos@comerciante.com'),
 'Carlos Rodríguez',
 'Supermercado Central',
 '11111111',
 '+51 999 333 333',
 'Calle Comercio 300',
 'Lima',
 'Perú',
 -12.0400,
 -77.0300,
 '3 tiendas en Lima',
 'Supermercado con sección de productos orgánicos',
 TRUE);

-- Crear índices adicionales para mejor rendimiento
CREATE INDEX IF NOT EXISTS idx_usuarios_email_activo ON usuarios(email, activo);
CREATE INDEX IF NOT EXISTS idx_lotes_usuario_activo ON lotes(usuario_id, activo);
CREATE INDEX IF NOT EXISTS idx_eventos_lote_fecha ON evento_trazabilidad(lote_id, fecha_evento);
CREATE INDEX IF NOT EXISTS idx_solicitudes_estado_fecha ON solicitud_certificacion(estado, fecha_solicitud);
CREATE INDEX IF NOT EXISTS idx_certificados_lote_estado ON certificado(lote_id, estado);

-- Mensaje de finalización
SELECT 'Base de datos TrazaAlimentos v2.0 creada exitosamente!' as Mensaje;
