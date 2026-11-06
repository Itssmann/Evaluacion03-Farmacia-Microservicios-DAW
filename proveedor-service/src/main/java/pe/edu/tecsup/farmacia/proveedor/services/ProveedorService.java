package pe.edu.tecsup.farmacia.proveedor.services;

import pe.edu.tecsup.farmacia.proveedor.dto.ProveedorDatos;
import pe.edu.tecsup.farmacia.proveedor.model.Proveedor;
import pe.edu.tecsup.farmacia.proveedor.repository.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ProveedorService {
    private final ProveedorRepository repository;

    public ProveedorService(ProveedorRepository repository) { this.repository = repository; }

    public List<Proveedor> listar() { return repository.findAll(); }

    public Proveedor buscar(long id) {
        return repository.findById(id).orElseThrow(() -> inexistente());
    }

    public Proveedor crear(ProveedorDatos datos) {
        Proveedor entidad = new Proveedor();
        entidad.actualizar(datos);
        return repository.save(entidad);
    }

    @Transactional
    public Proveedor actualizar(long id, ProveedorDatos datos) {
        Proveedor entidad = buscar(id);
        entidad.actualizar(datos);
        return entidad;
    }

    @Transactional
    public void eliminar(long id) { repository.delete(buscar(id)); }

    private ResponseStatusException inexistente() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor inexistente");
    }
}
