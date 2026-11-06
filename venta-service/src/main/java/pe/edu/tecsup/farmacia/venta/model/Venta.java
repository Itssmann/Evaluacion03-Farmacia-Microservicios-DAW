package pe.edu.tecsup.farmacia.venta.model;

import jakarta.persistence.*;
import pe.edu.tecsup.farmacia.venta.dto.VentaDatos;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 64)
    private String numeroComprobante;
    private LocalDate fecha;
    private Long medicamentoId;
    private Integer cantidad;
    private Long proveedorId;
    @Column(precision = 14, scale = 2)
    private BigDecimal precioUnitario;
    @Column(precision = 24, scale = 2)
    private BigDecimal subtotal;
    @Column(precision = 24, scale = 2)
    private BigDecimal total;
    @Enumerated(EnumType.STRING)
    private EstadoVenta estado;

    protected Venta() {}

    public Venta(VentaDatos datos) {
        numeroComprobante = datos.numeroComprobante();
        fecha = datos.fecha();
        medicamentoId = datos.medicamentoId();
        cantidad = datos.cantidad();
        proveedorId = datos.proveedorId();
    }

    public boolean coincide(VentaDatos datos) {
        return fecha.equals(datos.fecha()) && medicamentoId.equals(datos.medicamentoId())
                && cantidad.equals(datos.cantidad()) && Objects.equals(proveedorId, datos.proveedorId());
    }

    public void registrar(BigDecimal precio) {
        precioUnitario = precio;
        subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
        total = subtotal;
        estado = EstadoVenta.REGISTRADA;
    }

    public void cambiarEstado(EstadoVenta nuevo) { estado = nuevo; }

    public Long getId() { return id; }
    public String getNumeroComprobante() { return numeroComprobante; }
    public LocalDate getFecha() { return fecha; }
    public Long getMedicamentoId() { return medicamentoId; }
    public Integer getCantidad() { return cantidad; }
    public Long getProveedorId() { return proveedorId; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getTotal() { return total; }
    public EstadoVenta getEstado() { return estado; }
}
