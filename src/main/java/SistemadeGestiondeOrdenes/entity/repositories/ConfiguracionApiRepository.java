package SistemadeGestiondeOrdenes.entity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionApi;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de configuraciones de APIs externas.
 * Proporciona métodos para acceder y manipular las configuraciones de APIs
 * almacenadas en la base de datos.
 *
 * @author RepositoryWriter
 * @version 1.0
 */
@Repository
public interface ConfiguracionApiRepository extends JpaRepository<ConfiguracionApi, Long> {

    /**
     * Busca configuraciones de API por URL base.
     *
     * @param urlBase URL base de la API
     * @return Configuración de API encontrada
     */
    Optional<ConfiguracionApi> findByUrlBase(String urlBase);

    /**
     * Busca configuraciones de API activas.
     *
     * @return Lista de configuraciones activas
     */
    List<ConfiguracionApi> findByActivoTrue();

    /**
     * Busca configuraciones de API por entidad asociada.
     *
     * @param entidadAsociada Nombre de la entidad asociada
     * @return Lista de configuraciones para la entidad
     */
    List<ConfiguracionApi> findByEntidadAsociada(String entidadAsociada);

    /**
     * Busca configuraciones de API activas por entidad asociada.
     *
     * @param entidadAsociada Nombre de la entidad asociada
     * @return Lista de configuraciones activas para la entidad
     */
    List<ConfiguracionApi> findByEntidadAsociadaAndActivoTrue(String entidadAsociada);

    /**
     * Verifica si existe una configuración de API con la URL base especificada.
     *
     * @param urlBase URL base de la API
     * @return true si existe, false en caso contrario
     */
    boolean existsByUrlBase(String urlBase);

    /**
     * Busca configuraciones de API que requieren autenticación.
     *
     * @return Lista de configuraciones con endpoint de login
     */
    @Query("SELECT c FROM ConfiguracionApi c WHERE c.endpointLogin IS NOT NULL AND c.endpointLogin != '' AND c.activo = true")
    List<ConfiguracionApi> findConfiguracionesConAutenticacion();

    /**
     * Busca configuraciones de API por nombre.
     *
     * @param nombreApi Nombre de la API
     * @return Lista de configuraciones con el nombre especificado
     */
    List<ConfiguracionApi> findByNombreApiContainingIgnoreCase(String nombreApi);

    /**
     * Cuenta el número de configuraciones activas.
     *
     * @return Número de configuraciones activas
     */
    long countByActivoTrue();
}
