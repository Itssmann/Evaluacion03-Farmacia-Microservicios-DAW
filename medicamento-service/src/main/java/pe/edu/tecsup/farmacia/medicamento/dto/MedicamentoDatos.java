package pe.edu.tecsup.farmacia.medicamento.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MedicamentoDatos(
        @NotBlank @Size(max = 120) String nombre,
        @Size(max = 255) String descripcion,
        @NotBlank @Size(max = 120) String laboratorio,
        @NotBlank @Size(max = 120) String presentacion,
        @NotNull @Positive @Digits(integer = 12, fraction = 2) BigDecimal precioUnitario,
        @NotNull @PositiveOrZero Integer stock,
        @NotNull LocalDate fechaVencimiento
) {}
