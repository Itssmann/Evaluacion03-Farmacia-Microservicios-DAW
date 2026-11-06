$ErrorActionPreference = 'Stop'
$medicamentos = 'http://127.0.0.1:8081/api/medicamentos'
$proveedores = 'http://127.0.0.1:8082/api/proveedores'
$ventas = 'http://127.0.0.1:8083/api/ventas'
$marca = [DateTimeOffset]::UtcNow.ToUnixTimeMilliseconds()

function Enviar($metodo, $url, $datos) {
    Invoke-RestMethod -Method $metodo -Uri $url -ContentType 'application/json' -Body ($datos | ConvertTo-Json)
}

function VerificarRechazo($solicitud, $esperado) {
    try {
        Enviar Post $ventas $solicitud | Out-Null
        throw "Se esperaba HTTP $esperado"
    } catch {
        if (!$_.Exception.Response -or [int]$_.Exception.Response.StatusCode -ne $esperado) { throw }
    }
}

$proveedor = Enviar Post $proveedores @{
    razonSocial = "Proveedor demo $marca"; ruc = [string]($marca % 90000000000 + 10000000000)
    direccion = 'Lima'; correo = 'demo@example.com'; telefono = '999000111'
}
$medicamento = Enviar Post $medicamentos @{
    nombre = "Medicamento demo $marca"; descripcion = 'Prueba'; laboratorio = 'Demo'
    presentacion = 'Caja'; precioUnitario = 12.50; stock = 5; fechaVencimiento = '2030-12-31'
}
$solicitud = @{
    numeroComprobante = "DEMO-$marca"; fecha = (Get-Date -Format 'yyyy-MM-dd')
    medicamentoId = $medicamento.id; proveedorId = $proveedor.id; cantidad = 2
}
$venta = Enviar Post $ventas $solicitud
$reintento = Enviar Post $ventas $solicitud
if ($venta.estado -ne 'REGISTRADA' -or $venta.total -ne 25 -or $venta.subtotal -ne 25 -or $venta.id -ne $reintento.id) {
    throw 'Resultado inesperado en venta o reintento'
}
$sinStock = $solicitud.Clone()
$sinStock.numeroComprobante += '-SIN-STOCK'
$sinStock.cantidad = 4
VerificarRechazo $sinStock 409
$invalida = $solicitud.Clone()
$invalida.numeroComprobante += '-INVALIDA'
$invalida.cantidad = 0
VerificarRechazo $invalida 400
$inexistente = $solicitud.Clone()
$inexistente.numeroComprobante += '-INEXISTENTE'
$inexistente.medicamentoId = [long]::MaxValue
VerificarRechazo $inexistente 404
$persistido = Invoke-RestMethod "$ventas/$($venta.id)"
$stock = (Invoke-RestMethod "$medicamentos/$($medicamento.id)").stock
if ($persistido.total -ne 25 -or $stock -ne 3) { throw "Venta persistida o stock inesperado: $stock" }
$pagada = Enviar Patch "$ventas/$($venta.id)/estado" @{estado = 'PAGADA'}
$anulada = Enviar Patch "$ventas/$($venta.id)/estado" @{estado = 'ANULADA'}
$repetida = Enviar Patch "$ventas/$($venta.id)/estado" @{estado = 'ANULADA'}
if ($pagada.estado -ne 'PAGADA' -or $anulada.estado -ne 'ANULADA' -or $repetida.estado -ne 'ANULADA' -or
    (Invoke-RestMethod "$medicamentos/$($medicamento.id)").stock -ne 5) {
    throw 'Transición de estado o reposición incorrecta'
}
Write-Host "REST OK. Venta $($venta.id), medicamento $($medicamento.id), proveedor $($proveedor.id), total 25.00, rechazos 400/404/409, stock 5 -> 3 -> 5."
