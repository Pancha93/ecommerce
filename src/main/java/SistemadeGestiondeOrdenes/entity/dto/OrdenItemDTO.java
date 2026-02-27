package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenItemDTO {
    private Long id;
    private Long productoId;  
    private String productoNombre;
    private String productoImagen;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
