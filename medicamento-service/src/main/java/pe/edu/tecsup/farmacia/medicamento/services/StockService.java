package pe.edu.tecsup.farmacia.medicamento.services;

import pe.edu.tecsup.farmacia.medicamento.model.*;
import pe.edu.tecsup.farmacia.medicamento.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;

@Service
public class StockService {
    private final MedicamentoRepository medicamentos;
    private final MovimientoStockRepository movimientos;

    public StockService(MedicamentoRepository medicamentos, MovimientoStockRepository movimientos) {
        this.medicamentos = medicamentos;
        this.movimientos = movimientos;
    }

    @Transactional
    public BigDecimal descontar(long id, long ventaId, int cantidad) {
        Medicamento medicamento = bloquear(id);
        MovimientoStock previo = movimientos.findById(ventaId).orElse(null);
        if (previo != null) {
            if (!previo.coincide(id, cantidad) || previo.isRepuesto()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Movimiento de stock incompatible");
            }
            return previo.getPrecioUnitario();
        }
        if (medicamento.getStock() < cantidad) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente");
        }
        medicamento.descontar(cantidad);
        movimientos.save(new MovimientoStock(ventaId, id, cantidad, medicamento.getPrecioUnitario()));
        return medicamento.getPrecioUnitario();
    }

    @Transactional
    public void reponer(long id, long ventaId, int cantidad) {
        Medicamento medicamento = bloquear(id);
        MovimientoStock movimiento = movimientos.findById(ventaId)
                .filter(m -> m.coincide(id, cantidad))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movimiento inexistente"));
        if (!movimiento.isRepuesto()) {
            medicamento.reponer(movimiento.getCantidad());
            movimiento.marcarRepuesto();
        }
    }

    private Medicamento bloquear(long id) {
        return medicamentos.bloquear(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Medicamento inexistente"));
    }
}
