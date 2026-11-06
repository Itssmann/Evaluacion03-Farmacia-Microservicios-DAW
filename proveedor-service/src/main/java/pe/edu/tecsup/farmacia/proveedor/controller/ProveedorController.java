package pe.edu.tecsup.farmacia.proveedor.controller;

import pe.edu.tecsup.farmacia.proveedor.dto.ProveedorDatos;
import pe.edu.tecsup.farmacia.proveedor.model.Proveedor;
import pe.edu.tecsup.farmacia.proveedor.services.ProveedorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {
    private final ProveedorService service;

    public ProveedorController(ProveedorService service) { this.service = service; }

    @GetMapping
    public List<Proveedor> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Proveedor buscar(@PathVariable long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Proveedor crear(@Valid @RequestBody ProveedorDatos datos) { return service.crear(datos); }

    @PutMapping("/{id}")
    public Proveedor actualizar(@PathVariable long id, @Valid @RequestBody ProveedorDatos datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable long id) { service.eliminar(id); }
}
