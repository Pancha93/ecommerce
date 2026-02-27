package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una categoría de productos en el ecommerce.
 */
@Table(name="categoria")
@Entity
@Getter
@Setter
@ToString(exclude = "productos")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Categoria implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="categoria_id", nullable=false)
    private Long id;

    @Column(name="nombre", unique=true, length=100, nullable=false)
    private String nombre;

    @Column(name="descripcion", length=500)
    private String descripcion;

    @Column(name="imagen_url", length=255)
    private String imagenUrl;

    @Column(name="activo", nullable=false)
    private Boolean activo = true;

    @Column(name="fecha_creacion", nullable=false)
    private LocalDateTime fechaCreacion;

    @Column(name="fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producto> productos = new ArrayList<>();

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
