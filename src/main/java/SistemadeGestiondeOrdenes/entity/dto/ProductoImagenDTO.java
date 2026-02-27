package SistemadeGestiondeOrdenes.entity.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoImagenDTO {
    private Long id;
    private String url;
    private Boolean esPrincipal;
    private Integer orden;
}
