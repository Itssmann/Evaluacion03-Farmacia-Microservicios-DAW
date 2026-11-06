package pe.edu.tecsup.farmacia.proveedor.dto;

import jakarta.validation.constraints.*;

public record ProveedorDatos(
        @NotBlank @Size(max = 160) String razonSocial,
        @NotBlank @Pattern(regexp = "[0-9]{11}") String ruc,
        @NotBlank @Size(max = 255) String direccion,
        @NotBlank @Email @Size(max = 160) String correo,
        @NotBlank @Size(max = 30) String telefono
) {}
