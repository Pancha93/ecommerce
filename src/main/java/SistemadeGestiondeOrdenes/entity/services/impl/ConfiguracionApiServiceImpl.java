package SistemadeGestiondeOrdenes.entity.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collection;
import java.util.*;
import java.util.Optional;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionApi;
import SistemadeGestiondeOrdenes.entity.repositories.ConfiguracionApiRepository;
import SistemadeGestiondeOrdenes.entity.services.ApiAuthInitializationService;
import SistemadeGestiondeOrdenes.entity.services.ConfiguracionApiService;
import SistemadeGestiondeOrdenes.seguridad.Interceptor.HibernateFilterActivator;

/**
 * Implementación del servicio para la gestión de configuraciones de APIs externas.
 * Proporciona la lógica de negocio para acceder y manipular las configuraciones de APIs.
 *
 * @author ServiceWriter
 * @version 1.0
 */
@Service
@Transactional
public class ConfiguracionApiServiceImpl implements ConfiguracionApiService {

    @Autowired
    private ConfiguracionApiRepository repository;

    @Autowired
     private ApiAuthInitializationService authService;

    @Override
    public List<ConfiguracionApi> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ConfiguracionApi> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ConfiguracionApi save(ConfiguracionApi configuracion) {
        return repository.save(configuracion);
    }

    @Override
    public ConfiguracionApi update(ConfiguracionApi configuracion) {
        if (configuracion.getId() == null) {
            throw new IllegalArgumentException("El ID de la configuración no puede ser null");
        }
        ConfiguracionApi existingConfig = repository.findById(configuracion.getId())
            .orElseThrow(() -> new IllegalArgumentException("Configuración no encontrada con id: " + configuracion.getId()));
        boolean credencialesCambiadas = hanCambiadoCredenciales(existingConfig, configuracion);
        ConfiguracionApi updatedConfig = repository.save(configuracion);
        if (credencialesCambiadas) {
            authService.removeToken(existingConfig.getUrlBase());
        }
        return updatedConfig;
    }

    /**
     * Verifica si los campos sensibles de autenticación han cambiado
     */
    private boolean hanCambiadoCredenciales(ConfiguracionApi vieja, ConfiguracionApi nueva) {
        return !Objects.equals(vieja.getUsuario(), nueva.getUsuario()) ||
               !Objects.equals(vieja.getPassword(), nueva.getPassword()) ||
               !Objects.equals(vieja.getCampoUsuario(), nueva.getCampoUsuario()) ||
               !Objects.equals(vieja.getEndpointLogin(), nueva.getEndpointLogin());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<ConfiguracionApi> findByUrlBase(String urlBase) {
        return repository.findByUrlBase(urlBase);
    }

    @Override
    public List<ConfiguracionApi> findByActivoTrue() {
        return repository.findByActivoTrue();
    }

    @Override
    public List<ConfiguracionApi> findByEntidadAsociada(String entidadAsociada) {
        return repository.findByEntidadAsociada(entidadAsociada);
    }

    @Override
    public List<ConfiguracionApi> findByEntidadAsociadaAndActivoTrue(String entidadAsociada) {
        return repository.findByEntidadAsociadaAndActivoTrue(entidadAsociada);
    }

    @Override
    public boolean existsByUrlBase(String urlBase) {
        return repository.existsByUrlBase(urlBase);
    }

    @Override
    public List<ConfiguracionApi> findConfiguracionesConAutenticacion() {
        return repository.findConfiguracionesConAutenticacion();
    }

    @Override
    public List<ConfiguracionApi> findByNombreApiContainingIgnoreCase(String nombreApi) {
        return repository.findByNombreApiContainingIgnoreCase(nombreApi);
    }

    @Override
    public long countByActivoTrue() {
        return repository.countByActivoTrue();
    }
}
