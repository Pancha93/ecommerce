package SistemadeGestiondeOrdenes.entity.controllers;

import SistemadeGestiondeOrdenes.entity.dto.OrdenDTO;
import SistemadeGestiondeOrdenes.entity.dto.CrearOrdenRequest;
import SistemadeGestiondeOrdenes.entity.services.OrdenService;
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
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenService ordenService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<OrdenDTO>> obtenerMisOrdenes(HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.ok(ordenService.obtenerOrdenesUsuario(usuarioId));
    }

    @GetMapping("/todas")
    public ResponseEntity<List<OrdenDTO>> obtenerTodas() {
        return ResponseEntity.ok(ordenService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerPorId(id));
    }

    @GetMapping("/numero/{numeroOrden}")
    public ResponseEntity<OrdenDTO> obtenerPorNumero(@PathVariable String numeroOrden) {
        return ResponseEntity.ok(ordenService.obtenerPorNumero(numeroOrden));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<OrdenDTO>> obtenerPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(ordenService.obtenerPorEstado(estado));
    }

    @PostMapping
    public ResponseEntity<OrdenDTO> crearOrden(
            @RequestBody CrearOrdenRequest requestBody,
            HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenService.crearDesdeCarrito(usuarioId, requestBody));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<OrdenDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        return ResponseEntity.ok(ordenService.actualizarEstado(id, estado));
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
