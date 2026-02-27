package SistemadeGestiondeOrdenes.entity.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import SistemadeGestiondeOrdenes.entity.services.OrdenlaboratorioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import SistemadeGestiondeOrdenes.entity.services.impl.NotificacionEmailServiceImpl;
import java.lang.reflect.Field;
import SistemadeGestiondeOrdenes.entity.services.ConfiguracionApiService;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import SistemadeGestiondeOrdenes.entity.entities.Ordenlaboratorio;
import java.util.*;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.web.bind.annotation.*;
import SistemadeGestiondeOrdenes.entity.dto.OrdenlaboratorioDTO;
import SistemadeGestiondeOrdenes.entity.entities.ConfiguracionApi;

/**
 * Controlador REST para la gestión de entidades Ordenlaboratorio.
 * Proporciona endpoints para realizar operaciones CRUD sobre Ordenlaboratorio.
 *
 * @RestController Marca esta clase como un controlador REST
 * @RequestMapping Define la ruta base para todos los endpoints
 */
@RestController
@RequestMapping("/api/ordenlaboratorios")
public class OrdenlaboratorioController {

    private static final Logger log = LoggerFactory.getLogger(OrdenlaboratorioController.class);

    /**
     * Servicio que gestiona la lógica de negocio para Ordenlaboratorio.
     * Servicio que gestiona la lógica de negocio para ConfiguracionApi.
     */
    private final OrdenlaboratorioService service;
    private final ConfiguracionApiService configuracionApiService;

    /**
     * Constructor que inyecta el servicio necesario para la gestión de Ordenlaboratorio.
     *
     * @param service Servicio que implementa la lógica de negocio
     */
    @Autowired
    public OrdenlaboratorioController(OrdenlaboratorioService service, ConfiguracionApiService configuracionApiService) {
        this.service = service;
        this.configuracionApiService = configuracionApiService;
    }

    /**
     * Obtiene todas las entidades Ordenlaboratorio disponibles.
     *
     * @return ResponseEntity con la lista de entidades si existen,
     *         o una lista vacía si no hay registros
     */
    @GetMapping
    public ResponseEntity<List<Ordenlaboratorio>> findAll() {
        List<Ordenlaboratorio> entities = service.findAll();
        return ResponseEntity.ok(entities);
    }

    /**
     * Busca una entidad Ordenlaboratorio por su identificador.
     *
     * @param id Identificador único de la entidad
     * @return ResponseEntity con la entidad si existe,
     *         o ResponseEntity.notFound si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ordenlaboratorio> findById(@PathVariable Long id) {
        Optional<Ordenlaboratorio> entity = service.findById(id);
        return entity.map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Crea una nueva entidad Ordenlaboratorio.
     *
     * @param dto DTO con los datos de la entidad a crear
     * @return ResponseEntity con la entidad creada y estado HTTP 201 (Created)
     */
    @PostMapping
    public ResponseEntity<Ordenlaboratorio> save(@RequestBody OrdenlaboratorioDTO dto) {
        Ordenlaboratorio savedEntity = service.save(dto);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }

    /**
     * Actualiza una entidad Ordenlaboratorio existente.
     *
     * @param id Identificador de la entidad a actualizar
     * @param dto DTO con los nuevos datos de la entidad
     * @return ResponseEntity con la entidad actualizada,
     *         o ResponseEntity.notFound si la entidad no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Ordenlaboratorio> update(@PathVariable Long id, @RequestBody OrdenlaboratorioDTO dto) {
        if (service.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Ordenlaboratorio updatedEntity = service.update(id, dto);
        return ResponseEntity.ok(updatedEntity);
    }

    /**
     * Elimina una entidad Ordenlaboratorio por su identificador.
     *
     * @param id Identificador de la entidad a eliminar
     * @return ResponseEntity con estado HTTP 204 (No Content) si se eliminó correctamente,
     *         o ResponseEntity.notFound si la entidad no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
