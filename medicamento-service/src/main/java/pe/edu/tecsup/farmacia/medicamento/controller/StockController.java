package pe.edu.tecsup.farmacia.medicamento.controller;

import pe.edu.tecsup.farmacia.medicamento.services.StockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/interno/medicamentos/{id}/movimientos/{ventaId}")
public class StockController {
    private final StockService service;

    public StockController(StockService service) { this.service = service; }

    @PutMapping
    public Precio descontar(@PathVariable long id, @PathVariable long ventaId,
            @Valid @RequestBody Cantidad datos) {
        return new Precio(service.descontar(id, ventaId, datos.cantidad()));
    }

    @PutMapping("/reposicion")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reponer(@PathVariable long id, @PathVariable long ventaId,
            @Valid @RequestBody Cantidad datos) {
        service.reponer(id, ventaId, datos.cantidad());
    }

    public record Cantidad(@NotNull @Positive Integer cantidad) {}
    public record Precio(BigDecimal precioUnitario) {}
}
