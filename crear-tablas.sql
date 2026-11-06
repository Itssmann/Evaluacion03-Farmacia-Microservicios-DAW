CREATE TABLE IF NOT EXISTS bd_medicamento.medicamento (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255), descripcion VARCHAR(255), laboratorio VARCHAR(255),
    presentacion VARCHAR(255), precio_unitario DECIMAL(14,2), stock INT,
    fecha_vencimiento DATE
);

CREATE TABLE IF NOT EXISTS bd_medicamento.movimiento_stock (
    venta_id BIGINT NOT NULL PRIMARY KEY,
    medicamento_id BIGINT NOT NULL, cantidad INT NOT NULL,
    precio_unitario DECIMAL(14,2), repuesto BIT NOT NULL
);

CREATE TABLE IF NOT EXISTS bd_proveedor.proveedor (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    razon_social VARCHAR(255), ruc VARCHAR(11) UNIQUE, direccion VARCHAR(255),
    correo VARCHAR(255), telefono VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS bd_venta.venta (
    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    numero_comprobante VARCHAR(64) NOT NULL UNIQUE,
    fecha DATE, medicamento_id BIGINT, cantidad INT, proveedor_id BIGINT,
    precio_unitario DECIMAL(14,2), subtotal DECIMAL(24,2), total DECIMAL(24,2),
    estado ENUM('ANULADA','PAGADA','REGISTRADA')
);
