-- Ejecutar una vez desde una sesión administrativa de MySQL Workbench.
-- Cada CREATE USER devuelve una contraseña distinta. Consérvalas localmente.
CREATE USER 'farmacia_medicamento'@'localhost' IDENTIFIED BY RANDOM PASSWORD;
CREATE USER 'farmacia_proveedor'@'localhost' IDENTIFIED BY RANDOM PASSWORD;
CREATE USER 'farmacia_venta'@'localhost' IDENTIFIED BY RANDOM PASSWORD;

-- Se escapa el guion bajo para que no se interprete como comodín en GRANT.
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON `bd\_medicamento`.* TO 'farmacia_medicamento'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON `bd\_proveedor`.* TO 'farmacia_proveedor'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, ALTER, INDEX, REFERENCES
    ON `bd\_venta`.* TO 'farmacia_venta'@'localhost';
