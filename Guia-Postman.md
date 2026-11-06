# Evidencias en Postman Desktop

1. Abre un espacio personal en Postman Desktop y selecciona **Import**. Selecciona `Farmacia.postman_collection.json`.
2. Selecciona **Tecsup Laboratorio 01 Farmacia → Run → Functional → Local**. Usa una iteración y mantén el orden original. Activa **Persist responses for a session** y **Keep variable values**.
3. Pulsa **Start run**. En los resultados abre cada solicitud para ver método, URL, estado HTTP y cuerpo. Usa `Win+Shift+S` y guarda los PNG en `ENTREGA_FINAL/evidencias`.

| Orden | Solicitud | HTTP y evidencia |
|---:|---|---|
| 1 | Crear proveedor | 201. Guarda el ID devuelto. |
| 2 | Crear medicamento | 201. Guarda el ID y el stock inicial 5. |
| 3 | Consultar proveedor | 200. Muestra el mismo ID. |
| 4 | Consultar medicamento | 200. Muestra el mismo ID. |
| 5 | Registrar venta | 201. Captura comprobante, IDs, cantidad 2, subtotal y total 25.00. |
| 6 | Rechazar stock insuficiente | 409. Captura la cantidad solicitada y el mensaje. |
| 7 | Rechazar medicamento inexistente | 404. Captura el ID inexistente y el mensaje. |
| 8 | Rechazar cantidad inválida | 400. Captura cantidad 0 y el error del campo. |
| 9 | Verificar descuento | 200. Captura stock 3. |
| 10 | Pagar venta | 200. Captura estado PAGADA. |
| 11 | Anular venta | 200. Captura estado ANULADA. |
| 12 | Verificar reposición | 200. Captura stock 5 para el mismo medicamento. |

## CRUD completo

Para cada catálogo crea un registro desechable antes de actualizar o eliminar. Usa una copia del cuerpo del POST de la colección. Para Proveedor introduce otro RUC de 11 dígitos que aún no exista. Guarda el nuevo ID y usa solo ese ID en la secuencia siguiente.

| Operación | Medicamento | Proveedor | Captura |
|---|---|---|---|
| Crear | POST http://127.0.0.1:8081/api/medicamentos | POST http://127.0.0.1:8082/api/proveedores | 201 y nuevo ID |
| Listar | GET en la misma URL | GET en la misma URL | 200 y registro incluido |
| Consultar | GET en la misma URL con /ID | GET en la misma URL con /ID | 200 y registro |
| Actualizar | PUT con /ID y cuerpo completo, cambia nombre | PUT con /ID y cuerpo completo, cambia razonSocial | 200 y valor actualizado |
| Eliminar | DELETE con /ID | DELETE con /ID | 204 |
| Verificar eliminación | GET con /ID | GET con /ID | 404 |

## Identificadores y repeticiones

La evidencia incluida corresponde a medicamento 3, proveedor 3 y venta 4. En la comprobación actual, la venta 4 estaba ANULADA, el total era 25.00 y el medicamento 3 tenía stock 5. Volver a enviar el cuerpo idéntico de esa venta devolvió 201 con el mismo ID y estado, y el stock siguió en 5.

Para otra demostración inicia siempre desde **Crear proveedor**. La colección genera otro RUC y comprobante, crea otro medicamento y encadena los IDs devueltos. Cada corrida completa agrega registros de demostración. Para una captura aislada usa el resultado original del Runner. Volver a enviar un POST con el mismo RUC puede producir un conflicto, y pagar una venta ya anulada devuelve 409. El SQL de verificación incluido usa los IDs de la evidencia original. Si deseas verificar una nueva corrida, sustituye esos IDs por los que devolvió Postman.
