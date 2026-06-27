CREATE DATABASE IF NOT EXISTS trazaalimentos;
USE trazaalimentos;

CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol ENUM('PRODUCTOR', 'DISTRIBUIDOR', 'COMERCIANTE', 'ADMIN') NOT NULL,
    empresa VARCHAR(150),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    certificacion_organica VARCHAR(255),
    estado BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE productos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    productor_id BIGINT NOT NULL,
    tipo_producto VARCHAR(100),
    certificacion_organica VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (productor_id) REFERENCES usuarios(id)
);

CREATE TABLE lotes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo_lote VARCHAR(100) UNIQUE NOT NULL,
    producto_id BIGINT NOT NULL,
    productor_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    unidad VARCHAR(50),
    hash_inicial VARCHAR(256),
    codigo_qr LONGBLOB,
    estado ENUM('CREADO', 'EN_TRANSITO', 'EN_COMERCIO', 'VENDIDO') DEFAULT 'CREADO',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (producto_id) REFERENCES productos(id),
    FOREIGN KEY (productor_id) REFERENCES usuarios(id)
);

CREATE TABLE bloques (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lote_id BIGINT NOT NULL,
    numero_bloque INT NOT NULL,
    actor_id BIGINT NOT NULL,
    actor_rol VARCHAR(50) NOT NULL,
    accion VARCHAR(100) NOT NULL,
    hash_anterior VARCHAR(256),
    hash_actual VARCHAR(256) UNIQUE NOT NULL,
    datos_json LONGTEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (actor_id) REFERENCES usuarios(id)
);

CREATE TABLE transacciones (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    lote_id BIGINT NOT NULL,
    remitente_id BIGINT NOT NULL,
    destinatario_id BIGINT NOT NULL,
    tipo_transaccion VARCHAR(50) NOT NULL,
    ubicacion_origen VARCHAR(255),
    ubicacion_destino VARCHAR(255),
    fecha_transaccion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('PENDIENTE', 'CONFIRMADA', 'COMPLETADA') DEFAULT 'PENDIENTE',
    FOREIGN KEY (lote_id) REFERENCES lotes(id),
    FOREIGN KEY (remitente_id) REFERENCES usuarios(id),
    FOREIGN KEY (destinatario_id) REFERENCES usuarios(id)
);

CREATE INDEX idx_usuario_email ON usuarios(email);
CREATE INDEX idx_usuario_rol ON usuarios(rol);
CREATE INDEX idx_lote_codigo ON lotes(codigo_lote);
CREATE INDEX idx_lote_productor ON lotes(productor_id);
CREATE INDEX idx_bloque_lote ON bloques(lote_id);
CREATE INDEX idx_transaccion_lote ON transacciones(lote_id);