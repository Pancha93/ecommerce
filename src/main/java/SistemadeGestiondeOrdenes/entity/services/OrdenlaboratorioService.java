package SistemadeGestiondeOrdenes.entity.services;

import java.util.*;
import java.util.Collection;
import java.util.Optional;
import SistemadeGestiondeOrdenes.entity.dto.OrdenlaboratorioDTO;
import SistemadeGestiondeOrdenes.entity.entities.Ordenlaboratorio;
import SistemadeGestiondeOrdenes.entity.repositories.OrdenlaboratorioRepository;
import SistemadeGestiondeOrdenes.entity.services.OrdenlaboratorioService;
import SistemadeGestiondeOrdenes.seguridad.Interceptor.HibernateFilterActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que gestiona las operaciones de negocio para la entidad {@link Ordenlaboratorio}.
 * <p>Este servicio proporciona una capa de abstracción entre la capa de repositorio y la capa de controlador,
 * implementando la lógica de negocio necesaria para el manejo de entidades Ordenlaboratorio.</p>
 */
public interface OrdenlaboratorioService {

    /**
     * Recupera todas las entidades Ordenlaboratorio almacenadas.
     * @return Lista de todas las entidades Ordenlaboratorio encontradas
     */
    List<Ordenlaboratorio> findAll();

    /**
     * Busca una entidad Ordenlaboratorio por su identificador.
     * @param id Identificador único de la entidad
     * @return Optional que contiene la entidad si existe, vacío si no se encuentra
     */
    Optional<Ordenlaboratorio> findById(Long id);

    /**
     * Guarda una nueva entidad Ordenlaboratorio en la base de datos.
     * @param dto DTO con los datos de la entidad a crear
     * @return La entidad Ordenlaboratorio creada y persistida
     */
    Ordenlaboratorio save(OrdenlaboratorioDTO dto);

    /**
     * Actualiza una entidad Ordenlaboratorio existente.
     * @param id Identificador de la entidad a actualizar
     * @param updateDTO DTO con los nuevos datos de la entidad
     * @return La entidad Ordenlaboratorio actualizada
     * @throws RuntimeException si no se encuentra la entidad
     */
    Ordenlaboratorio update(long id, OrdenlaboratorioDTO updateDTO);

    /**
     * Elimina una entidad Ordenlaboratorio por su identificador.
     * @param id Identificador de la entidad a eliminar
     */
    void deleteById(Long id);

}
