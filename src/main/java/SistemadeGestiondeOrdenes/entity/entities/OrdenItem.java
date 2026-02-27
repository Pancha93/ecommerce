package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad que representa un item en una orden de compra.
 */
@Table(name="orden_item")
@Entity
@Getter
@Setter
@ToString(exclude = {"orden", "producto"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrdenItem implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="orden_item_id", nullable=false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id", nullable=false)
    @JsonIgnore
    private Orden orden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable=false)
    private Producto producto;

    @Column(name="cantidad", nullable=false)
    private Integer cantidad;

    @Column(name="precio_unitario", nullable=false, precision=10, scale=2)
    private BigDecimal precioUnitario;

    @Column(name="subtotal", nullable=false, precision=10, scale=2)
    private BigDecimal subtotal;

    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            subtotal = precioUnitario.multiply(new BigDecimal(cantidad));
        }
    }
}
