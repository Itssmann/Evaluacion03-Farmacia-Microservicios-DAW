-- Identificadores de la ejecución registrada en evidencia-rest.json.
SELECT id, numero_comprobante, medicamento_id, proveedor_id, cantidad,
       precio_unitario, subtotal, total, estado
FROM bd_venta.venta
WHERE id = 4;

SELECT id, stock
FROM bd_medicamento.medicamento
WHERE id = 3;

SELECT venta_id, medicamento_id, cantidad, repuesto
FROM bd_medicamento.movimiento_stock
WHERE venta_id = 4;

SELECT id, razon_social
FROM bd_proveedor.proveedor
WHERE id = 3;
