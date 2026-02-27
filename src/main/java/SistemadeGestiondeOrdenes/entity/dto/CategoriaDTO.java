package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private Integer cantidadProductos;
}
