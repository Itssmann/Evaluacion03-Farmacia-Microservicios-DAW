package pe.edu.tecsup.farmacia.medicamento.repository;

import pe.edu.tecsup.farmacia.medicamento.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Medicamento m where m.id = :id")
    Optional<Medicamento> bloquear(long id);
}
