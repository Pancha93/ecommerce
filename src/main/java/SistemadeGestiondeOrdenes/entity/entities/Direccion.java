package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidad que representa una dirección de envío.
 */
@Table(name="direccion")
@Entity
@Getter
@Setter
@ToString(exclude = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Direccion implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="direccion_id", nullable=false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable=false)
    @JsonIgnore
    private Usuario usuario;

    @Column(name="nombre_completo", nullable=false, length=200)
    private String nombreCompleto;

    @Column(name="telefono", nullable=false, length=20)
    private String telefono;

    @Column(name="direccion_linea1", nullable=false, length=255)
    private String direccionLinea1;

    @Column(name="direccion_linea2", length=255)
    private String direccionLinea2;

    @Column(name="ciudad", nullable=false, length=100)
    private String ciudad;

    @Column(name="estado_provincia", nullable=false, length=100)
    private String estadoProvincia;

    @Column(name="codigo_postal", nullable=false, length=20)
    private String codigoPostal;

    @Column(name="pais", nullable=false, length=100)
    private String pais;

    @Column(name="es_predeterminada", nullable=false)
    private Boolean esPredeterminada = false;

    @Column(name="fecha_creacion", nullable=false)
    private LocalDateTime fechaCreacion;

    @Column(name="fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
