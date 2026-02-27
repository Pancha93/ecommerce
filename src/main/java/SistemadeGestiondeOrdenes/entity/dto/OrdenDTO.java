package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenDTO {
    private Long id;
    private String numeroOrden;
    private Long usuarioId;
    private String usuarioNombre;
    private String estado;
    private BigDecimal subtotal;
    private BigDecimal costoEnvio;
    private BigDecimal impuestos;
    private BigDecimal total;
    private String metodoPago;
    private String notas;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private DireccionDTO direccionEnvio;
    private List<OrdenItemDTO> items;
}
