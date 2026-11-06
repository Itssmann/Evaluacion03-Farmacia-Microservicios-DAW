package pe.edu.tecsup.farmacia.venta.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class CatalogosClient {
    private final RestClient medicamentos;
    private final RestClient proveedores;

    public CatalogosClient(
            @Value("${farmacia.medicamentos-url:http://localhost:8081}") String medicamentosUrl,
            @Value("${farmacia.proveedores-url:http://localhost:8082}") String proveedoresUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(4000);
        medicamentos = RestClient.builder().baseUrl(medicamentosUrl).requestFactory(factory).build();
        proveedores = RestClient.builder().baseUrl(proveedoresUrl).requestFactory(factory).build();
    }

    public void verificarProveedor(long id) {
        consultar(() -> proveedores.get().uri("/api/proveedores/{id}", id).retrieve().toBodilessEntity(),
                "Proveedor inexistente");
    }

    public BigDecimal descontar(long id, long ventaId, int cantidad) {
        Precio respuesta = consultar(() -> medicamentos.put()
                .uri("/interno/medicamentos/{id}/movimientos/{ventaId}", id, ventaId)
                .body(Map.of("cantidad", cantidad)).retrieve().body(Precio.class), "Medicamento inexistente");
        if (respuesta == null || respuesta.precioUnitario() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "El catalogo no devolvio el precio");
        }
        return respuesta.precioUnitario();
    }

    public void reponer(long id, long ventaId, int cantidad) {
        consultar(() -> medicamentos.put()
                .uri("/interno/medicamentos/{id}/movimientos/{ventaId}/reposicion", id, ventaId)
                .body(Map.of("cantidad", cantidad)).retrieve().toBodilessEntity(),
                "Medicamento o movimiento inexistente");
    }

    private <T> T consultar(Supplier<T> consulta, String inexistente) {
        try {
            return consulta.get();
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, inexistente, e);
        } catch (HttpClientErrorException.Conflict e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente o movimiento incompatible", e);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Catalogo no disponible", e);
        }
    }

    private record Precio(BigDecimal precioUnitario) {}
}
