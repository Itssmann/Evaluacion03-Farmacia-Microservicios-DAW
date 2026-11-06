package pe.edu.tecsup.farmacia.proveedor.model;

import jakarta.persistence.*;
import pe.edu.tecsup.farmacia.proveedor.dto.ProveedorDatos;

@Entity
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String razonSocial;
    @Column(unique = true, length = 11)
    private String ruc;
    private String direccion;
    private String correo;
    private String telefono;

    public Proveedor() {}

    public void actualizar(ProveedorDatos datos) {
        razonSocial = datos.razonSocial();
        ruc = datos.ruc();
        direccion = datos.direccion();
        correo = datos.correo();
        telefono = datos.telefono();
    }

    public Long getId() { return id; }
    public String getRazonSocial() { return razonSocial; }
    public String getRuc() { return ruc; }
    public String getDireccion() { return direccion; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
}
