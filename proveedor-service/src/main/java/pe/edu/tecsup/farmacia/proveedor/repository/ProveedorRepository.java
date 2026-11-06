package pe.edu.tecsup.farmacia.proveedor.repository;

import pe.edu.tecsup.farmacia.proveedor.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}
