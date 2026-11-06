$ErrorActionPreference = 'Stop'
$env:DB_HOST = '127.0.0.1'
$env:DB_PORT = '3306'
$env:SPRING_PROFILES_ACTIVE = 'dev'
$env:RUN_MYSQL_TESTS = 'true'
$failed = @()

try {
    foreach ($service in 'medicamento-service', 'proveedor-service', 'venta-service') {
        Write-Host "Prueba de contexto para $service"
        $env:DB_USERNAME = Read-Host 'Usuario MySQL de este servicio'
        $secret = Read-Host 'Contraseña MySQL' -AsSecureString
        try {
            $env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $secret).Password
            Push-Location (Join-Path $PSScriptRoot $service)
            try {
                & .\mvnw.cmd -B -ntp test
                if ($LASTEXITCODE -ne 0) { $failed += $service }
            } finally {
                Pop-Location
            }
        } finally {
            Remove-Item Env:DB_PASSWORD, Env:DB_USERNAME -ErrorAction SilentlyContinue
            $secret.Dispose()
        }
    }
} finally {
    Remove-Item Env:RUN_MYSQL_TESTS -ErrorAction SilentlyContinue
}

if ($failed.Count -gt 0) { throw "Fallaron las pruebas de: $($failed -join ', ')" }
