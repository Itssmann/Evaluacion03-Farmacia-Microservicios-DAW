package pe.edu.tecsup.farmacia.venta.services;

import pe.edu.tecsup.farmacia.venta.dto.VentaDatos;
import pe.edu.tecsup.farmacia.venta.model.*;
import pe.edu.tecsup.farmacia.venta.repository.VentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class VentaService {
    private final VentaRepository ventas;
    private final CatalogosClient catalogos;
    private final TransactionTemplate transaccion;

    public VentaService(VentaRepository ventas, CatalogosClient catalogos, TransactionTemplate transaccion) {
        this.ventas = ventas;
        this.catalogos = catalogos;
        this.transaccion = transaccion;
    }

    public List<Venta> listar() { return ventas.findAllByEstadoIsNotNull(); }

    public Venta buscar(long id) {
        return ventas.findById(id).filter(v -> v.getEstado() != null).orElseThrow(this::inexistente);
    }

    public Venta registrar(VentaDatos datos) {
        // La fila pendiente conserva el ID con el que Medicamento deduplica reintentos.
        Venta pendiente = ventas.findByNumeroComprobante(datos.numeroComprobante())
                .orElseGet(() -> ventas.saveAndFlush(new Venta(datos)));
        return transaccion.execute(status -> {
            Venta venta = ventas.bloquear(pendiente.getId()).orElseThrow(this::inexistente);
            if (!venta.coincide(datos)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El comprobante ya tiene otros datos");
            }
            if (venta.getEstado() == null) {
                if (venta.getProveedorId() != null) {
                    catalogos.verificarProveedor(venta.getProveedorId());
                }
                venta.registrar(catalogos.descontar(venta.getMedicamentoId(), venta.getId(), venta.getCantidad()));
            }
            return venta;
        });
    }

    public Venta cambiarEstado(long id, EstadoVenta nuevo) {
        return transaccion.execute(status -> {
            Venta venta = ventas.bloquear(id).filter(v -> v.getEstado() != null).orElseThrow(this::inexistente);
            EstadoVenta actual = venta.getEstado();
            if (actual == nuevo) {
                return venta;
            }
            if (actual == EstadoVenta.ANULADA || nuevo == EstadoVenta.REGISTRADA) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Transicion de estado invalida");
            }
            if (nuevo == EstadoVenta.ANULADA) {
                catalogos.reponer(venta.getMedicamentoId(), id, venta.getCantidad());
            }
            venta.cambiarEstado(nuevo);
            return venta;
        });
    }

    private ResponseStatusException inexistente() {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta inexistente");
    }
}
