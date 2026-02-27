package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.io.Serializable;

/**
 * Entidad que representa las imágenes de un producto.
 */
@Table(name="producto_imagen")
@Entity
@Getter
@Setter
@ToString(exclude = "producto")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductoImagen implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="imagen_id", nullable=false)
    private Long id;

    @Column(name="url", nullable=false, length=500)
    private String url;

    @Column(name="es_principal", nullable=false)
    private Boolean esPrincipal = false;

    @Column(name="orden", nullable=false)
    private Integer orden = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable=false)
    @JsonIgnore
    private Producto producto;
}
