package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DireccionDTO {
    private Long id;
    private Long usuarioId;
    private String nombreCompleto;
    private String telefono;
    private String direccionLinea1;
    private String direccionLinea2;
    private String ciudad;
    private String estadoProvincia;
    private String codigoPostal;
    private String pais;
    private Boolean esPredeterminada;
    private LocalDateTime fechaCreacion;
}
