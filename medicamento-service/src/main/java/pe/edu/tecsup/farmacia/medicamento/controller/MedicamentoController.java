package pe.edu.tecsup.farmacia.medicamento.controller;

import pe.edu.tecsup.farmacia.medicamento.dto.MedicamentoDatos;
import pe.edu.tecsup.farmacia.medicamento.model.Medicamento;
import pe.edu.tecsup.farmacia.medicamento.services.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
public class MedicamentoController {
    private final MedicamentoService service;

    public MedicamentoController(MedicamentoService service) { this.service = service; }

    @GetMapping
    public List<Medicamento> listar() { return service.listar(); }

    @GetMapping("/{id}")
    public Medicamento buscar(@PathVariable long id) { return service.buscar(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medicamento crear(@Valid @RequestBody MedicamentoDatos datos) { return service.crear(datos); }

    @PutMapping("/{id}")
    public Medicamento actualizar(@PathVariable long id, @Valid @RequestBody MedicamentoDatos datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable long id) { service.eliminar(id); }
}
