package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal precioOferta;
    private Integer stock;
    private String sku;
    private BigDecimal peso;
    private String marca;
    private Boolean activo;
    private Boolean destacado;
    private LocalDateTime fechaCreacion;
    private Long categoriaId;
    private String categoriaNombre;
    private List<ProductoImagenDTO> imagenes;
    private String imagenPrincipal;
}
