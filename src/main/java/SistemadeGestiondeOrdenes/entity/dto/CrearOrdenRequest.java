package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearOrdenRequest {
    private Long direccionEnvioId;
    private String metodoPago;
    private String notas;
}
