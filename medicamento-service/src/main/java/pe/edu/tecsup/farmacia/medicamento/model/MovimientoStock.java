package pe.edu.tecsup.farmacia.medicamento.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class MovimientoStock {
    @Id
    private Long ventaId;
    private long medicamentoId;
    private int cantidad;
    @Column(precision = 14, scale = 2)
    private BigDecimal precioUnitario;
    private boolean repuesto;

    protected MovimientoStock() {}

    public MovimientoStock(long ventaId, long medicamentoId, int cantidad, BigDecimal precioUnitario) {
        this.ventaId = ventaId;
        this.medicamentoId = medicamentoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public boolean coincide(long medicamentoId, int cantidad) {
        return this.medicamentoId == medicamentoId && this.cantidad == cantidad;
    }

    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public boolean isRepuesto() { return repuesto; }
    public void marcarRepuesto() { repuesto = true; }
}
