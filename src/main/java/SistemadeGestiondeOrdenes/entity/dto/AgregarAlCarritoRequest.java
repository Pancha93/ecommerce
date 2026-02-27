package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgregarAlCarritoRequest {
    private Long productoId;
    private Integer cantidad;
}
