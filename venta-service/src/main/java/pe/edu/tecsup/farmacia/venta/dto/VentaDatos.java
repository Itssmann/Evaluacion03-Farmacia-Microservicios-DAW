package pe.edu.tecsup.farmacia.venta.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record VentaDatos(
        @NotBlank @Size(max = 64) String numeroComprobante,
        @NotNull LocalDate fecha,
        @NotNull @Positive Long medicamentoId,
        @NotNull @Positive Integer cantidad,
        @Positive Long proveedorId
) {}
