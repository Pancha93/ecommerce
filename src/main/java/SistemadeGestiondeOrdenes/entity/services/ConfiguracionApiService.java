package SistemadeGestiondeOrdenes.entity.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collection;
import java.util.*;
import java.util.Optional;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionApi;
import SistemadeGestiondeOrdenes.entity.repositories.ConfiguracionApiRepository;
import SistemadeGestiondeOrdenes.seguridad.Interceptor.HibernateFilterActivator;

/**
 * Servicio para la gestión de configuraciones de APIs externas.
 * Proporciona métodos para acceder y manipular las configuraciones de APIs
 * almacenadas en la base de datos.
 *
 * @author ServiceWriter
 * @version 1.0
 */
@Service
@Transactional
public interface ConfiguracionApiService {

    /**
     * Obtiene todas las configuraciones de API.
     *
     * @return Lista de todas las configuraciones de API
     */
    List<ConfiguracionApi> findAll();

    /**
     * Obtiene una configuración de API por su ID.
     *
     * @param id ID de la configuración de API
     * @return Configuración de API encontrada
     */
    Optional<ConfiguracionApi> findById(Long id);

    /**
     * Guarda una nueva configuración de API.
     *
     * @param configuracion Configuración de API a guardar
     * @return Configuración de API guardada
     */
    ConfiguracionApi save(ConfiguracionApi configuracion);

    /**
     * Actualiza una configuración de API existente.
     *
     * @param configuracion Configuración de API a actualizar
     * @return Configuración de API actualizada
     */
    ConfiguracionApi update(ConfiguracionApi configuracion);

    /**
     * Elimina una configuración de API por su ID.
     *
     * @param id ID de la configuración de API
     */
    void deleteById(Long id);

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
