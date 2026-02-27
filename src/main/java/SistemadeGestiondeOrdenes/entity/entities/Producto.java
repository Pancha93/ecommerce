package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un producto en el ecommerce.
 */
@Table(name="producto")
@Entity
@Getter
@Setter
@ToString(exclude = {"categoria", "imagenes"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Producto implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="producto_id", nullable=false)
    private Long id;

    @Column(name="nombre", nullable=false, length=200)
    private String nombre;

    @Column(name="descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name="precio", nullable=false, precision=10, scale=2)
    private BigDecimal precio;

    @Column(name="precio_oferta", precision=10, scale=2)
    private BigDecimal precioOferta;

    @Column(name="stock", nullable=false)
    private Integer stock = 0;

    @Column(name="sku", unique=true, length=100)
    private String sku;

    @Column(name="peso", precision=8, scale=2)
    private BigDecimal peso;

    @Column(name="marca", length=100)
    private String marca;

    @Column(name="activo", nullable=false)
    private Boolean activo = true;

    @Column(name="destacado", nullable=false)
    private Boolean destacado = false;

    @Column(name="fecha_creacion", nullable=false)
    private LocalDateTime fechaCreacion;

    @Column(name="fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    @JsonIgnore
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ProductoImagen> imagenes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }

    public BigDecimal getPrecioFinal() {
        return precioOferta != null && precioOferta.compareTo(BigDecimal.ZERO) > 0 ? precioOferta : precio;
    }
}
