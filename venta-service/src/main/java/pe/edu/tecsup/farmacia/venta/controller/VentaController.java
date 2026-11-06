package pe.edu.tecsup.farmacia.venta.controller;

import pe.edu.tecsup.farmacia.venta.dto.VentaDatos;
import pe.edu.tecsup.farmacia.venta.model.*;
import pe.edu.tecsup.farmacia.venta.services.VentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {
    private final VentaService service;

    public VentaController(VentaService service) { this.service = service; }

    @GetMapping
    public List<Venta> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Venta buscar(@PathVariable long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Venta registrar(@Valid @RequestBody VentaDatos datos) { return service.registrar(datos); }

    @PatchMapping("/{id}/estado")
    public Venta cambiarEstado(@PathVariable long id, @Valid @RequestBody EstadoDatos datos) {
        return service.cambiarEstado(id, datos.estado());
    }

    public record EstadoDatos(@NotNull EstadoVenta estado) {}
}
