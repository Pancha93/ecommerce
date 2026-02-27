package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una orden de compra.
 */
@Table(name="orden")
@Entity
@Getter
@Setter
@ToString(exclude = {"usuario", "items", "direccionEnvio"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Orden implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="orden_id", nullable=false)
    private Long id;

    @Column(name="numero_orden", unique=true, nullable=false, length=50)
    private String numeroOrden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable=false)
    @JsonIgnore
    private Usuario usuario;

    @Column(name="estado", nullable=false, length=50)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado = EstadoOrden.PENDIENTE;

    @Column(name="subtotal", nullable=false, precision=10, scale=2)
    private BigDecimal subtotal;

    @Column(name="costo_envio", nullable=false, precision=10, scale=2)
    private BigDecimal costoEnvio = BigDecimal.ZERO;

    @Column(name="impuestos", precision=10, scale=2)
    private BigDecimal impuestos = BigDecimal.ZERO;

    @Column(name="total", nullable=false, precision=10, scale=2)
    private BigDecimal total;

    @Column(name="metodo_pago", length=50)
    private String metodoPago;

    @Column(name="notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name="fecha_creacion", nullable=false)
    private LocalDateTime fechaCreacion;

    @Column(name="fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_envio_id")
    private Direccion direccionEnvio;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrdenItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (numeroOrden == null) {
            numeroOrden = "ORD-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public enum EstadoOrden {
        PENDIENTE,
        CONFIRMADA,
        PROCESANDO,
        ENVIADA,
        ENTREGADA,
        CANCELADA,
        DEVUELTA
    }
}
