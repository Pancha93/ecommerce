package SistemadeGestiondeOrdenes.entity.controllers;

import SistemadeGestiondeOrdenes.entity.dto.DireccionDTO;
import SistemadeGestiondeOrdenes.entity.services.DireccionService;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.service.UserService;
import SistemadeGestiondeOrdenes.seguridad.service.impl.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<DireccionDTO>> obtenerMisDirecciones(HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.ok(direccionService.obtenerDireccionesUsuario(usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(direccionService.obtenerPorId(id));
    }

    @GetMapping("/predeterminada")
    public ResponseEntity<DireccionDTO> obtenerPredeterminada(HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        DireccionDTO direccion = direccionService.obtenerPredeterminada(usuarioId);
        if (direccion != null) {
            return ResponseEntity.ok(direccion);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<DireccionDTO> crear(
            @RequestBody DireccionDTO direccionDTO,
            HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(direccionService.crear(usuarioId, direccionDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizar(
            @PathVariable Long id,
            @RequestBody DireccionDTO direccionDTO) {
        return ResponseEntity.ok(direccionService.actualizar(id, direccionDTO));
    }

    @PatchMapping("/{id}/predeterminada")
    public ResponseEntity<DireccionDTO> establecerComoPredeterminada(
            @PathVariable Long id,
            HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.ok(direccionService.establecerComoPredeterminada(id, usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        direccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Long obtenerUsuarioId(HttpServletRequest request) {
        String token = jwtTokenService.extractJwtFromRequest(request);
        if (token == null) {
            throw new RuntimeException("Token no encontrado");
        }
        String username = jwtTokenService.obtenerUsername(token);
        Usuario usuario = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }
}
