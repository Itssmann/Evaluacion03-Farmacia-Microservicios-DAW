package pe.edu.tecsup.farmacia.medicamento.services;

import pe.edu.tecsup.farmacia.medicamento.dto.MedicamentoDatos;
import pe.edu.tecsup.farmacia.medicamento.model.Medicamento;
import pe.edu.tecsup.farmacia.medicamento.repository.MedicamentoRepository;
import pe.edu.tecsup.farmacia.medicamento.repository.MovimientoStockRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class MedicamentoService {
    private final MedicamentoRepository repository;
    private final MovimientoStockRepository movimientos;

    public MedicamentoService(MedicamentoRepository repository, MovimientoStockRepository movimientos) {
        this.repository = repository;
        this.movimientos = movimientos;
    }

    public List<Medicamento> listar() { return repository.findAll(); }

    public Medicamento buscar(long id) {
        return repository.findById(id).orElseThrow(() -> inexistente());
    }

    public Medicamento crear(MedicamentoDatos datos) {
        Medicamento entidad = new Medicamento();
        entidad.actualizar(datos);
        return repository.save(entidad);
    }

    @Transactional
    public Medicamento actualizar(long id, MedicamentoDatos datos) {
        Medicamento entidad = repository.bloquear(id).orElseThrow(this::inexistente);
        entidad.actualizar(datos);
        return entidad;
    }

    @Transactional
    public void eliminar(long id) {
        Medicamento medicamento = repository.bloquear(id).orElseThrow(this::inexistente);
        if (movimientos.existsByMedicamentoIdAndRepuestoFalse(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El medicamento tiene ventas activas");
        }
        repository.delete(medicamento);
    }

    private ResponseStatusException inexistente() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento inexistente");
    }
}
