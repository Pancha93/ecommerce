package SistemadeGestiondeOrdenes.entity.dto;

import java.time.LocalDate;
import lombok.*;
import SistemadeGestiondeOrdenes.entity.entities.*;

/**
 * DTO (Data Transfer Object) para la entidad Ordenlaboratorio.
 * Esta clase se utiliza para transferir datos de Ordenlaboratorio entre diferentes capas de la aplicación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenlaboratorioDTO {

    /**
     * Campo que representa nombre
     */
    private String nombre;

    /**
     * Campo que representa record
     */
    private String record;

    /**
     * Campo que representa rx
     */
    private String rx;

    /**
     * Campo que representa piezas
     */
    private Integer piezas;

    /**
     * Campo que representa shade
     */
    private String shade;

    /**
     * Campo que representa lab
     */
    private String lab;

    /**
     * Campo que representa fechaEnvio
     */
    private LocalDate fechaEnvio;

    /**
     * Campo que representa fechaEntrega
     */
    private LocalDate fechaEntrega;

    /**
     * Campo que representa factura
     */
    private String factura;

    /**
     * Campo que representa monto
     */
    private Double monto;

    /**
     * Campo que representa pay
     */
    private String pay;

    /**
     * Campo que representa cheque
     */
    private String cheque;

    /**
     * Campo que representa el creador del registro.
     */
    private String creador;

}
