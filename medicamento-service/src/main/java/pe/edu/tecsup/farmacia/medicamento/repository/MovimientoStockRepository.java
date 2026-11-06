package pe.edu.tecsup.farmacia.medicamento.repository;

import pe.edu.tecsup.farmacia.medicamento.model.MovimientoStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long> {
    boolean existsByMedicamentoIdAndRepuestoFalse(long medicamentoId);
}
