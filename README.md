# Sistema de gestión de farmacia

Evaluación 03 de Tecsup · Laboratorio 01 · Luis Pablo Abad Anchiraico · Sección D

## Preparación

### Desde un clon nuevo en Windows

1. Instala [Git para Windows](https://git-scm.com/download/win), [Temurin JDK 17](https://adoptium.net/temurin/releases/?version=17), [IntelliJ IDEA](https://www.jetbrains.com/idea/download/), [MySQL Community Server 8.4 LTS](https://dev.mysql.com/downloads/mysql/8.4.html?os=3) y [Postman Desktop](https://www.postman.com/downloads/). Selecciona el instalador de Windows para tu arquitectura. En el MSI de Java habilita PATH y JAVA_HOME. En MySQL Configurator habilita TCP/IP en 3306 y configura el servicio y su contraseña administrativa local.
2. Abre una PowerShell nueva. Comprueba `java -version` y `javac -version`. Ambos deben mostrar 17. Ejecuta:

   ```powershell
   git clone https://github.com/Itssmann/Evaluacion03-Farmacia-Microservicios-DAW.git
   cd Evaluacion03-Farmacia-Microservicios-DAW
   ```

3. Conéctate con un administrador en Workbench o en el cliente MySQL y ejecuta `crear-bases.sql`. Luego, solo en una instalación nueva, ejecuta las sentencias de `crear-usuarios-dev.sql`. Guarda localmente las contraseñas nuevas que devuelve cada CREATE USER. Puedes usar `crear-tablas.sql` para preparar el esquema explícitamente. El perfil dev también puede preparar sus tablas al arrancar.
4. Desde la raíz compila con el Wrapper. La primera ejecución descarga Maven y las dependencias de Maven Central.

   ```powershell
   foreach ($servicio in 'medicamento-service', 'proveedor-service', 'venta-service') {
       Push-Location $servicio
       try {
           .\mvnw.cmd package
           if ($LASTEXITCODE -ne 0) { throw "Falló $servicio" }
       } finally { Pop-Location }
   }
   .\probar-mysql.ps1
   .\arrancar-local.ps1
   ```

5. Introduce usuarios y contraseñas solamente en los avisos locales. `.env.example` muestra las variables. Para ejecutar desde IntelliJ abre cada `pom.xml` con **Open as Project**, selecciona el JDK 17 en **Project Structure** y en **Maven → Runner**, y usa **Run** en la clase principal. Define las variables localmente para cada ejecución.
6. Espera el mensaje de inicio en los registros de cada servicio. Desde la raíz ejecuta `.\probar-rest.ps1` o importa la colección y sigue `Guia-Postman.md`. La colección genera datos de demostración nuevos y enlaza los IDs automáticamente.

El repositorio contiene una sola copia del código. Para reproducir la carpeta de entrega, extrae el archivo que genera `git archive --format=zip --prefix=ENTREGA_FINAL/ -o entrega-final.zip HEAD`.

Se requiere JDK 17, Maven, MySQL Server 8.4 y Postman. En IntelliJ IDEA abre el `pom.xml` de cada servicio como proyecto Maven y selecciona Java 17 como SDK y JDK de Maven.

| Servicio | Puerto | Esquema | Cuenta de desarrollo |
|---|---:|---|---|
| medicamento-service | 8081 | bd_medicamento | farmacia_medicamento |
| proveedor-service | 8082 | bd_proveedor | farmacia_proveedor |
| venta-service | 8083 | bd_venta | farmacia_venta |

Ejecuta `crear-bases.sql` con una sesión administrativa. En una instalación nueva ejecuta también `crear-usuarios-dev.sql` y conserva localmente las contraseñas devueltas por MySQL. Para preparar tablas explícitamente usa `crear-tablas.sql`. Cada aplicación lee `DB_USERNAME` y `DB_PASSWORD` de su proceso. `DB_HOST` y `DB_PORT` valen por defecto `localhost` y `3306`.

El perfil `dev` es el predeterminado y permite a Hibernate crear o actualizar tablas. En `prod`, Hibernate valida el esquema existente. Venta permite configurar `FARMACIA_MEDICAMENTOS_URL` y `FARMACIA_PROVEEDORES_URL`. Los endpoints internos de stock están destinados a una red privada de laboratorio.

Desde cada proyecto ejecuta `mvnw.cmd package`. Para levantar los tres JAR desde PowerShell en la carpeta raíz, ejecuta `.\arrancar-local.ps1` e introduce las tres contraseñas en los avisos ocultos. Los registros quedan en `target/arranque.log` de cada servicio. Alternativamente, ejecuta la clase principal en IntelliJ con sus variables de entorno locales.

## API

| Método | Ruta | Resultado esperado |
|---|---|---|
| GET | /api/medicamentos y /api/proveedores | 200, lista |
| GET | /api/medicamentos/{id} y /api/proveedores/{id} | 200 o 404 |
| POST | /api/medicamentos y /api/proveedores | 201 o 400 |
| PUT | /api/medicamentos/{id} y /api/proveedores/{id} | 200, 400 o 404 |
| DELETE | /api/medicamentos/{id} y /api/proveedores/{id} | 204, 404 o 409 |
| GET | /api/ventas y /api/ventas/{id} | 200 o 404 |
| POST | /api/ventas | 201, 400, 404, 409 o 502 |
| PATCH | /api/ventas/{id}/estado | 200, 400, 404, 409 o 502 |

Una venta contiene un medicamento y su cantidad. El proveedor es opcional. El precio se toma del catálogo al descontar el stock y se conserva en la venta. El subtotal es precio por cantidad y el total es igual al subtotal. Se puede pagar una venta registrada y anular una venta registrada o pagada. Anular repone el stock una vez.

Venta guarda primero una fila pendiente con un ID estable. Medicamento bloquea la fila de inventario y guarda el movimiento en la misma transacción que el descuento. Si se pierde la respuesta o falla la confirmación de la venta, repite el mismo comprobante y cuerpo para completar el registro sin volver a descontar. Las filas pendientes no aparecen en la API de consulta. Usa otro comprobante si cambias los datos de una solicitud rechazada. La recuperación de una operación interrumpida requiere el reintento explícito.

## Pruebas

- Ejecuta `.\probar-mysql.ps1` para las pruebas de integración de contexto con las tres cuentas. Revisa `target/surefire-reports`.
- Importa `Farmacia.postman_collection.json` y ejecuta la colección en orden con los tres servicios activos. Incluye una venta de 25.00, errores 400, 404 y 409, y comprobaciones de descuento y reposición.
- Ejecuta `.\probar-rest.ps1` para repetir desde PowerShell el flujo de venta, reintento, rechazo, pago y anulación.
- En Workbench, consulta `bd_venta.venta`, `bd_medicamento.medicamento` y `bd_medicamento.movimiento_stock` por los IDs de la ejecución para verificar la persistencia.

## Conclusiones técnicas

1. Una cuenta por esquema permite que MySQL aplique la separación entre servicios.
2. Los DTO validan los datos al entrar por HTTP y permiten responder con errores por campo.
3. `BigDecimal` conserva la precisión decimal en los importes.
4. El bloqueo de la fila de inventario serializa las operaciones sobre el mismo medicamento.
5. El ID de venta hace que los reintentos del movimiento de stock tengan un efecto único.

La dificultad de conexión fue el rechazo MySQL 1045. Hibernate mostraba después un error de dialecto porque no podía leer metadatos. Se corrigió usando la credencial correspondiente a cada cuenta y se verificaron los tres contextos iniciales en Surefire.

## Sustentación de seis minutos

1. Minuto 1. Mostrar el diagrama, los tres puertos y los esquemas.
2. Minuto 2. Explicar un DTO, una entidad JPA y el repositorio.
3. Minutos 3 y 4. Crear proveedor, medicamento y venta en Postman. Mostrar el total y el stock.
4. Minuto 5. Probar stock insuficiente y explicar el bloqueo y el reintento.
5. Minuto 6. Pagar y anular. Mostrar la reposición y la consulta en Workbench.
