/**
 * Ordenlaboratorio.java
 * Generado automáticamente el 19/02/2026 09:55:15
 */

package SistemadeGestiondeOrdenes.entity.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import SistemadeGestiondeOrdenes.entity.annotations.FilePath;
import lombok.*;
import java.util.*;
import java.math.*;
import java.time.*;
import java.io.Serializable;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Objeto;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

/**
 * Entidad que representa ordenlaboratorio en el sistema.
 * Esta clase es una entidad JPA que se mapea a la tabla correspondiente en la base de datos.
 *
 * @author EntityWriter
 * @version 1.0
 */
@Table(name="ordenlaboratorio")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FilterDef(name = "filtroCreador", parameters = @ParamDef(name = "creador", type = String.class))
@Filter(name = "filtroCreador", condition = "creador = :creador")
public class Ordenlaboratorio implements Serializable {

    /**
     * Identificador unico de la entidad. Este campo representa la clave primaria de la tabla en la base de datos.
     * 
     * Restricciones:
     */
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ordenlaboratorio_id", nullable=false)
    private long id;

    /**
     * Atributo nombre de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="nombre", unique=false, length=100, nullable=false)
    private String nombre;

    /**
     * Atributo record de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="record", unique=true, length=100, nullable=false)
    private String record;

    /**
     * Atributo rx de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="rx", unique=false, length=200, nullable=false)
    private String rx;

    /**
     * Atributo piezas de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Min(0)
    @Max(100000000)
    @Column(name="piezas", unique=false, nullable=false)
    private Integer piezas;

    /**
     * Atributo shade de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="shade", unique=false, length=200, nullable=false)
    private String shade;

    /**
     * Atributo lab de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="lab", unique=false, length=200, nullable=false)
    private String lab;

    /**
     * Atributo fechaEnvio de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Temporal(TemporalType.DATE)
    @Column(name="fechaEnvio", unique=false, nullable=false)
    private LocalDate fechaEnvio;

    /**
     * Atributo fechaEntrega de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Temporal(TemporalType.DATE)
    @Column(name="fechaEntrega", unique=false, nullable=false)
    private LocalDate fechaEntrega;

    /**
     * Atributo factura de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="factura", unique=false, length=200, nullable=false)
    private String factura;

    /**
     * Atributo monto de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="monto", unique=false, nullable=false)
    private Double monto;

    /**
     * Atributo pay de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="pay", unique=false, length=200, nullable=false)
    private String pay;

    /**
     * Atributo cheque de la entidad OrdenLaboratorio
     * 
     * Restricciones:
     */
    @Column(name="cheque", unique=false, length=200, nullable=false)
    private String cheque;

    /**
     * Columna que representa el creador de la entidad.
     */
    @Column(name = "creador")
    private String creador;

    /**
     * Constructor con parámetros.
     * Inicializa una nueva instancia de Ordenlaboratorio con los valores especificados.
     *
     * @param nombre Atributo nombre de la entidad OrdenLaboratorio
     * @param record Atributo record de la entidad OrdenLaboratorio
     * @param rx Atributo rx de la entidad OrdenLaboratorio
     * @param piezas Atributo piezas de la entidad OrdenLaboratorio
     * @param shade Atributo shade de la entidad OrdenLaboratorio
     * @param lab Atributo lab de la entidad OrdenLaboratorio
     * @param fechaEnvio Atributo fechaEnvio de la entidad OrdenLaboratorio
     * @param fechaEntrega Atributo fechaEntrega de la entidad OrdenLaboratorio
     * @param factura Atributo factura de la entidad OrdenLaboratorio
     * @param monto Atributo monto de la entidad OrdenLaboratorio
     * @param pay Atributo pay de la entidad OrdenLaboratorio
     * @param cheque Atributo cheque de la entidad OrdenLaboratorio
     * @param creador Columna que representa el creador de la entidad.
     */
    public Ordenlaboratorio(String nombre, String record, String rx, Integer piezas, String shade, String lab, LocalDate fechaEnvio, LocalDate fechaEntrega, String factura, Double monto, String pay, String cheque, String creador) {
        this.nombre = nombre;
        this.record = record;
        this.rx = rx;
        this.piezas = piezas;
        this.shade = shade;
        this.lab = lab;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntrega = fechaEntrega;
        this.factura = factura;
        this.monto = monto;
        this.pay = pay;
        this.cheque = cheque;
        this.creador = creador;
    }

}
