package SistemadeGestiondeOrdenes.entity.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.util.Optional;
import jakarta.persistence.EntityNotFoundException;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionApi;
import SistemadeGestiondeOrdenes.entity.services.ConfiguracionApiService;
import SistemadeGestiondeOrdenes.entity.services.ApiAuthInitializationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controlador REST para la gestión de configuraciones de APIs externas.
 * Proporciona endpoints para realizar operaciones CRUD sobre las configuraciones
 * de APIs, incluyendo autenticación y gestión de credenciales.
 *
 * @RestController Marca esta clase como un controlador REST
 * @RequestMapping Define la ruta base para todos los endpoints
 */
@RestController
@RequestMapping("/api/configuracionapis")
public class ConfiguracionApiController {

    /**
     * Servicio que gestiona la lógica de negocio para ConfiguracionApi.
     * Servicio que gestiona la lógica de negocio para ApiAuthInitialization.
     */
    private final ConfiguracionApiService configuracionApiService;
    private final ApiAuthInitializationService authService;

    /**
     * Constructor que inyecta el servicio necesario para la gestión de ConfiguracionApi.
     *
     * @param configuracionApiService Servicio que implementa la lógica de negocio
     */
    @Autowired
    public ConfiguracionApiController(ConfiguracionApiService configuracionApiService, ApiAuthInitializationService authService) {
        this.configuracionApiService = configuracionApiService;
        this.authService = authService;
    }

    /**
     * Obtiene todas las configuraciones de API.
     *
     * @return Lista de todas las configuraciones de API
     */
    @GetMapping
    public ResponseEntity<List<ConfiguracionApi>> findAll() {
        List<ConfiguracionApi> configuraciones = configuracionApiService.findAll();
        return ResponseEntity.ok(configuraciones);
    }

    /**
     * Obtiene una configuración de API por su ID.
     *
     * @param id ID de la configuración de API
     * @return Configuración de API encontrada
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracionApi> findById(@PathVariable Long id) {
        Optional<ConfiguracionApi> configuracion = configuracionApiService.findById(id);
        return configuracion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva configuración de API.
     *
     * @param configuracion Configuración de API a crear
     * @return Configuración de API creada
     */
    @PostMapping
    public ResponseEntity<ConfiguracionApi> save(@RequestBody ConfiguracionApi configuracion) {
        try {
            ConfiguracionApi saved = configuracionApiService.save(configuracion);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza una configuración de API existente.
     *
     * @param id ID de la configuración de API
     * @param configuracion Configuración de API actualizada
     * @return Configuración de API actualizada
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracionApi> update(@PathVariable Long id, @RequestBody ConfiguracionApi configuracion) {
        try {
            configuracion.setId(id);
            ConfiguracionApi existing = configuracionApiService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Configuración no encontrada"));
            ConfiguracionApi updated = configuracionApiService.update(configuracion);
            if (authService.hanCambiadoCredenciales(existing, updated)) {
                authService.removeToken(existing.getUrlBase());
            }
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina una configuración de API por su ID.
     *
     * @param id ID de la configuración de API
     * @return Respuesta de confirmación
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            configuracionApiService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene configuraciones de API activas.
     *
     * @return Lista de configuraciones activas
     */
    @GetMapping("/activas")
    public ResponseEntity<List<ConfiguracionApi>> findActivas() {
        List<ConfiguracionApi> configuraciones = configuracionApiService.findByActivoTrue();
        return ResponseEntity.ok(configuraciones);
    }

    /**
     * Obtiene configuraciones de API por entidad asociada.
     *
     * @param entidadAsociada Nombre de la entidad asociada
     * @return Lista de configuraciones para la entidad
     */
    @GetMapping("/entidad/{entidadAsociada}")
    public ResponseEntity<List<ConfiguracionApi>> findByEntidadAsociada(@PathVariable String entidadAsociada) {
        List<ConfiguracionApi> configuraciones = configuracionApiService.findByEntidadAsociada(entidadAsociada);
        return ResponseEntity.ok(configuraciones);
    }

    /**
     * Obtiene configuraciones de API que requieren autenticación.
     *
     * @return Lista de configuraciones con autenticación
     */
    @GetMapping("/con-autenticacion")
    public ResponseEntity<List<ConfiguracionApi>> findConfiguracionesConAutenticacion() {
        List<ConfiguracionApi> configuraciones = configuracionApiService.findConfiguracionesConAutenticacion();
        return ResponseEntity.ok(configuraciones);
    }

    /**
     * Busca configuraciones de API por nombre.
     *
     * @param nombre Nombre de la API a buscar
     * @return Lista de configuraciones encontradas
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ConfiguracionApi>> findByNombre(@RequestParam String nombre) {
        List<ConfiguracionApi> configuraciones = configuracionApiService.findByNombreApiContainingIgnoreCase(nombre);
        return ResponseEntity.ok(configuraciones);
    }

    /**
     * Verifica si existe una configuración de API con la URL base especificada.
     *
     * @param urlBase URL base de la API
     * @return true si existe, false en caso contrario
     */
    @GetMapping("/existe-url")
    public ResponseEntity<Boolean> existsByUrlBase(@RequestParam String urlBase) {
        boolean existe = configuracionApiService.existsByUrlBase(urlBase);
        return ResponseEntity.ok(existe);
    }

    /**
     * Obtiene el número de configuraciones activas.
     *
     * @return Número de configuraciones activas
     */
    @GetMapping("/contar-activas")
    public ResponseEntity<Long> countActivas() {
        long count = configuracionApiService.countByActivoTrue();
        return ResponseEntity.ok(count);
    }
}
