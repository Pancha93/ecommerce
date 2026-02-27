package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entidad que representa un item en el carrito de compras.
 */
@Table(name="carrito_item")
@Entity
@Getter
@Setter
@ToString(exclude = {"carrito", "producto"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CarritoItem implements Serializable {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="carrito_item_id", nullable=false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id", nullable=false)
    @JsonIgnore
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable=false)
    private Producto producto;

    @Column(name="cantidad", nullable=false)
    private Integer cantidad;

    @Column(name="precio_unitario", nullable=false, precision=10, scale=2)
    private BigDecimal precioUnitario;

    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(new BigDecimal(cantidad));
    }
}
