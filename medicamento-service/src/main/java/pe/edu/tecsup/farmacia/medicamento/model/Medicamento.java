package pe.edu.tecsup.farmacia.medicamento.model;

import jakarta.persistence.*;
import pe.edu.tecsup.farmacia.medicamento.dto.MedicamentoDatos;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Medicamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private String laboratorio;
    private String presentacion;
    @Column(precision = 14, scale = 2)
    private BigDecimal precioUnitario;
    private Integer stock;
    private LocalDate fechaVencimiento;

    public Medicamento() {}

    public void actualizar(MedicamentoDatos datos) {
        nombre = datos.nombre();
        descripcion = datos.descripcion();
        laboratorio = datos.laboratorio();
        presentacion = datos.presentacion();
        precioUnitario = datos.precioUnitario();
        stock = datos.stock();
        fechaVencimiento = datos.fechaVencimiento();
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getLaboratorio() { return laboratorio; }
    public String getPresentacion() { return presentacion; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public Integer getStock() { return stock; }
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void descontar(int cantidad) { stock -= cantidad; }
    public void reponer(int cantidad) { stock = Math.addExact(stock, cantidad); }
}
