package pe.edu.tecsup.farmacia.venta.repository;

import pe.edu.tecsup.farmacia.venta.model.Venta;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findByNumeroComprobante(String numeroComprobante);
    List<Venta> findAllByEstadoIsNotNull();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select v from Venta v where v.id = :id")
    Optional<Venta> bloquear(long id);
}
