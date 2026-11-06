package pe.edu.tecsup.farmacia.venta;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import pe.edu.tecsup.farmacia.venta.dto.VentaDatos;
import pe.edu.tecsup.farmacia.venta.model.EstadoVenta;
import pe.edu.tecsup.farmacia.venta.model.Venta;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VentaImportesTest {
    @Test
    void calculaImportesSinRedondeoBinario() {
        Venta venta = new Venta(new VentaDatos("B-001", LocalDate.of(2026, 11, 6), 1L, 3, null));
        venta.registrar(new BigDecimal("0.10"));
        assertEquals(new BigDecimal("0.30"), venta.getSubtotal());
        assertEquals(new BigDecimal("0.30"), venta.getTotal());
        assertEquals(EstadoVenta.REGISTRADA, venta.getEstado());
    }
}
