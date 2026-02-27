package SistemadeGestiondeOrdenes.entity.services.impl;

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
 * Implementación del servicio {@link OrdenlaboratorioService} que proporciona
 * la lógica de negocio para gestionar entidades {@link Ordenlaboratorio}.
 * <p>Esta implementación se encarga de la gestión completa del ciclo de vida de las entidades,
 * incluyendo operaciones CRUD y métodos de negocio específicos.</p>
 */
@Service
@Transactional
public class OrdenlaboratorioServiceImpl implements OrdenlaboratorioService {

    private static final Logger log = LoggerFactory.getLogger(OrdenlaboratorioServiceImpl.class);

@Autowired
private HibernateFilterActivator filterActivator;     /** Repositorio para acceder a los datos de la entidad */
    private final OrdenlaboratorioRepository repository;

    /**
     * Constructor que inicializa el servicio con su repositorio correspondiente.
     * @param repository Repositorio para la entidad Ordenlaboratorio
     */
    @Autowired
    public OrdenlaboratorioServiceImpl(OrdenlaboratorioRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ordenlaboratorio> findAll() {
        filterActivator.activarFiltro(Ordenlaboratorio.class);
        return repository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Ordenlaboratorio> findById(Long id) {
        return repository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ordenlaboratorio save(OrdenlaboratorioDTO dto) {
        Ordenlaboratorio entity = new Ordenlaboratorio();
        for (java.lang.reflect.Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                java.lang.reflect.Field entityField = entity.getClass().getDeclaredField(field.getName());
                entityField.setAccessible(true);
                entityField.set(entity, field.get(dto));
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("Error al copiar la propiedad '{}' del DTO a la entidad", field.getName(), e);
            }
        }
        return repository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ordenlaboratorio update(long id, OrdenlaboratorioDTO dto) {
        Optional<Ordenlaboratorio> optionalEntity = repository.findById(id);
        if (optionalEntity.isPresent()) {
            Ordenlaboratorio entity = optionalEntity.get();
            for (java.lang.reflect.Field field : dto.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    java.lang.reflect.Field entityField = entity.getClass().getDeclaredField(field.getName());
                    entityField.setAccessible(true);
                    entityField.set(entity, field.get(dto));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    log.error("Error al copiar la propiedad '{}' del DTO a la entidad", field.getName(), e);
                }
            }
            return repository.save(entity);
        } else {
            throw new RuntimeException("Entity not found");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
