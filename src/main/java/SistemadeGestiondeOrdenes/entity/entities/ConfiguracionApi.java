/**
 * ConfiguracionApi.java
 * Generado automáticamente el 19/02/2026 09:55:16
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
 * Entidad que representa la configuración de APIs externas en el sistema.
 * Esta clase es una entidad JPA que se mapea a la tabla configuracion_api en la base de datos.
 * Permite almacenar la configuración necesaria para conectarse a APIs externas,
 * incluyendo credenciales, endpoints y parámetros de autenticación.
 *
 * @author EntityWriter
 * @version 1.0
 */
@Entity
@Table(name = "configuracion_api")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracionApi implements Serializable {

    /**
     * Identificador único de la configuración de API.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre descriptivo de la API.
     */
    @Column(name = "nombre_api")
    private String nombreApi;

    /**
     * URL base de la API externa.
     */
    @Column(name = "url_base", unique = true, nullable = false)
    private String urlBase;

    /**
     * Endpoint para el proceso de login/autenticación.
     */
    @Column(name = "endpoint_login")
    private String endpointLogin;

    /**
     * Nombre del campo de usuario en la API externa.
     */
    @Column(name = "campo_usuario")
    private String campoUsuario;

    /**
     * Usuario para autenticación en la API externa.
     */
    @Column(name = "usuario")
    private String usuario;

    /**
     * Contraseña para autenticación en la API externa.
     */
    @Column(name = "password")
    private String password;

    /**
     * Indica si la configuración está activa.
     */
    @Column(name = "activo")
    private Boolean activo;

    /**
     * Descripción de la configuración de API.
     */
    @Column(name = "descripcion")
    private String descripcion;

    /**
     * Entidad asociada a esta configuración de API.
     */
    @Column(name = "entidad_asociada")
    private String entidadAsociada;

    /**
     * Métodos HTTP soportados por la API (JSON).
     */
   @FilePath(type = "text")
    @Column(name = "metodos_soportados", columnDefinition = "TEXT")
    private String metodosSoportados;

    /**
     * Campos de interfaz para cada método (JSON).
     */
   @FilePath(type = "text")
    @Column(name = "campos_interfaz", columnDefinition = "TEXT")
    private String camposInterfaz;

    /**
     * Columna que representa el creador de la entidad.
     */
    @Column(name = "creador")
    private String creador;

}
