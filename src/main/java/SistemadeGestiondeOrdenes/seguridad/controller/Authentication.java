package SistemadeGestiondeOrdenes.seguridad.controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import SistemadeGestiondeOrdenes.seguridad.dto.*;
import SistemadeGestiondeOrdenes.seguridad.exception.ObjectNotFoundException;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Rol;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.service.RoleService;
import SistemadeGestiondeOrdenes.seguridad.service.UserService;
import SistemadeGestiondeOrdenes.seguridad.service.impl.AuthenticationService;
import SistemadeGestiondeOrdenes.seguridad.service.impl.DynamicEntityService;
import SistemadeGestiondeOrdenes.seguridad.service.impl.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = {"http://localhost:*", "http://3.135.124.123:*"})
@RequestMapping("/auth")
public class Authentication {
    private static final Logger log = LoggerFactory.getLogger(Authentication.class);

    @Autowired
    private DynamicEntityService dynamicEntityService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenService jwtTokenService;
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        Usuario usuario = authenticationService.authenticate(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        );

        if (usuario != null) {
            PermisosDTO dto = new PermisosDTO();
            String token = jwtTokenService.generateToken(usuario);
            dto.setToken(token);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String>logout(HttpServletRequest request){
        return ResponseEntity.ok("logout exitoso");
    }
    @PostMapping("/crearUsuario")
    public ResponseEntity<Boolean> crearUsuario(@RequestBody @Valid SaveUser newUser){
        boolean resultado =  authenticationService.crearUsuario(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PostMapping("/prueba")
    public ResponseEntity<String> prueba(@RequestBody @Valid SaveUser newUser){
        return ResponseEntity.status(HttpStatus.CREATED).body("esto es una prueba");
    }

    @PostMapping("/crearRol")
    public ResponseEntity<Rol> crearRol(@RequestBody Rol newRol){
        Rol rolNuevo = roleService.crearRol(newRol);
        return ResponseEntity.status(HttpStatus.CREATED).body(rolNuevo);
    }

    @GetMapping("/getRoles")
    public ResponseEntity<List<RolDTO>> getRoles() {
        List<RolDTO> roles = roleService.getRoles();
        if (!roles.isEmpty()) {
            return ResponseEntity.ok(roles);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<Usuario>> getUsers(){

        List<Usuario> listUsers = userService.getUsers();
        if(!listUsers.isEmpty()){
            return ResponseEntity.ok(listUsers);
        }

        return ResponseEntity.notFound().build();
    }

    // asignar un rol a un usuario
    @PostMapping("/asignarRol")
    public ResponseEntity<Boolean> asignarRol(@RequestBody @Valid SaveUsuarioRol saveUsuarioRol){
        boolean resultado = userService.asignarRol(saveUsuarioRol);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @DeleteMapping("/quitarRol")
    public ResponseEntity<Boolean> quitarRol(@RequestParam Long usuarioId, @RequestParam Long rolId) {
        try {
            if (usuarioId == 1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(false);
            }

            boolean resultado = userService.quitarRol(usuarioId, rolId);
            return ResponseEntity.ok(resultado);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } catch (Exception e) {
            log.error("Error al quitar rol", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @DeleteMapping("/quitarRolHijo")
    public ResponseEntity<Boolean> quitarRolHijo(@RequestParam Long rolPadreId, @RequestParam Long rolHijoId) {
        try {
            boolean resultado = roleService.quitarRolHijo(rolPadreId, rolHijoId);
            return ResponseEntity.ok(resultado);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    //agregar un rol a otro rol
    @PostMapping("/agregarRol")
    public ResponseEntity<Boolean> agregarRol(@RequestBody @Valid SaveRolRol saveRolRol){
        boolean resultado = roleService.agregarRol(saveRolRol);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @GetMapping("/buscarObjeto")
    public List<?> buscarObjeto(@RequestParam String tipoObjeto, HttpServletRequest request) {
        try {
            // Extraer el token JWT desde el encabezado
            String token = jwtTokenService.extractJwtFromRequest(request);

            Usuario usuario = authenticationService.buscarUsuario(jwtTokenService.obtenerUsername(token));
            PermisosDTO permisos = authenticationService.obtenerPermisos(usuario);

            // Filtrar los permisos por tipoObjeto y extraer el campo "objeto"
            List<String> nombreObjetos = permisos.getPermisos().stream()
                    .filter(permiso -> tipoObjeto.equals(permiso.getTipoObjeto())) // Filtrar por tipoObjeto
                    .map(permiso -> permiso.getObjeto()) // Extraer el campo "objeto"
                    .collect(Collectors.toList());

            // Pasar el resultado filtrado al servicio dinámico
            return dynamicEntityService.findByDescripcionAndNombreObjetos(tipoObjeto, nombreObjetos);
        } catch (Exception e) {
            log.error("Error al buscar objetos", e);
            throw new RuntimeException("Error al buscar objetos");
        }
    }

    //Obtiene los permisos de un rol
    @GetMapping("/permisos")
    public ResponseEntity<List<AccionObjetoDTO>> permisosPorRolOUsuario(@RequestParam(defaultValue = "-1") Long rolID,@RequestParam(defaultValue = "-1") Long usuarioID) {
        try {
            List<AccionObjetoDTO> permisos = roleService.getPermisosPorRolOUsuario(rolID, usuarioID);
            return ResponseEntity.ok(permisos);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/actualizarPrivilegio")
    public ResponseEntity<Boolean> actualizarPrivilegio(@RequestBody AccionObjetoDTO permiso){
        boolean estado = userService.actualizarPrivilegio(permiso);
        return ResponseEntity.status(HttpStatus.OK).body(estado);
    }

    @DeleteMapping("/eliminarUsuario")
    public ResponseEntity<Boolean> eliminarUsuario(@RequestParam Long usuarioId) {
        try {
            if (usuarioId == 1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(false);
            }

            boolean resultado = userService.eliminarUsuario(usuarioId);
            return ResponseEntity.ok(resultado);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } catch (Exception e) {
            log.error("Error al eliminar usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @DeleteMapping("/eliminarRolPadre")
    public ResponseEntity<Boolean> eliminarRolPadre(@RequestParam Long rolPadreId) {
        try {
            if (rolPadreId == 1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(false);
            }
            boolean resultado = roleService.eliminarRolPadre(rolPadreId);
            return ResponseEntity.ok(resultado);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } catch (Exception e) {
            log.error("Error al eliminar rol padre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }


    @PostMapping("/verificar-correo")
    public ResponseEntity<?> verificarCorreo(@RequestBody Map<String, String> body) {
        try {
            String correo = body.get("correo");
            if (correo == null || correo.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "El correo es requerido"));
            }

            Optional<Usuario> usuarioOptional = userService.findByCorreo(correo);

            if (usuarioOptional.isPresent()) {
                // Si el usuario existe, generamos el token y enviamos los permisos
                Usuario usuario = usuarioOptional.get();
                String token = jwtTokenService.generateToken(usuario);
                PermisosDTO permisosDTO = authenticationService.obtenerPermisos(usuario);

                Map<String, Object> response = Map.of(
                        "existe", true,
                        "token", token,
                        "permisos", permisosDTO
                );

                return ResponseEntity.ok(response);
            } else {
                // Si el usuario no existe
                return ResponseEntity.ok(Map.of("existe", false));
            }

        } catch (Exception e) {
            log.error("Error al verificar el correo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar el correo"));
        }
    }
}