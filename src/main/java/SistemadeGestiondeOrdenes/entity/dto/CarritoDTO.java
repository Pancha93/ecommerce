package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoDTO {
    private Long id;
    private Long usuarioId;
    private List<CarritoItemDTO> items;
    private BigDecimal total;
    private Integer cantidadItems;
    private LocalDateTime fechaActualizacion;
}
