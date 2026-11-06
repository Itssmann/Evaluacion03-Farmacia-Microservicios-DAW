$ErrorActionPreference = 'Stop'
$java = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME 'bin/java.exe' } else { 'java.exe' }
$env:DB_HOST = '127.0.0.1'
$env:DB_PORT = '3306'
$env:SPRING_PROFILES_ACTIVE = 'dev'

foreach ($domain in 'medicamento', 'proveedor', 'venta') {
    $service = "$domain-service"
    $jar = Join-Path $PSScriptRoot "$service/target/$service-0.0.1-SNAPSHOT.jar"
    if (!(Test-Path -LiteralPath $jar)) { throw "Falta compilar $service" }
    $env:DB_USERNAME = "farmacia_$domain"
    $password = Read-Host "Contraseña de $env:DB_USERNAME" -AsSecureString
    try {
        $env:DB_PASSWORD = [System.Net.NetworkCredential]::new('', $password).Password
        $log = Join-Path $PSScriptRoot "$service/target/arranque.log"
        $errors = Join-Path $PSScriptRoot "$service/target/arranque-error.log"
        $process = Start-Process -FilePath $java -ArgumentList @('-jar', ('"' + $jar + '"')) -PassThru -WindowStyle Hidden -RedirectStandardOutput $log -RedirectStandardError $errors
        Write-Host "$service PID $($process.Id). Registro en $log"
    } finally {
        Remove-Item Env:DB_PASSWORD, Env:DB_USERNAME -ErrorAction SilentlyContinue
        $password.Dispose()
    }
}
