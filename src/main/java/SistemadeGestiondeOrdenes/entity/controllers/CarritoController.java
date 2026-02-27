package SistemadeGestiondeOrdenes.entity.controllers;

import SistemadeGestiondeOrdenes.entity.dto.CarritoDTO;
import SistemadeGestiondeOrdenes.entity.dto.AgregarAlCarritoRequest;
import SistemadeGestiondeOrdenes.entity.services.CarritoService;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.service.UserService;
import SistemadeGestiondeOrdenes.seguridad.service.impl.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito(HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.ok(carritoService.obtenerCarritoUsuario(usuarioId));
    }

    @PostMapping("/items")
    public ResponseEntity<CarritoDTO> agregarItem(
            @RequestBody AgregarAlCarritoRequest requestBody,
            HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.ok(carritoService.agregarItem(usuarioId, requestBody));
    }

    @PutMapping("/items/{productoId}")
    public ResponseEntity<CarritoDTO> actualizarCantidad(
            @PathVariable Long productoId,
            @RequestParam Integer cantidad,
            HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.ok(carritoService.actualizarCantidad(usuarioId, productoId, cantidad));
    }

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<CarritoDTO> eliminarItem(
            @PathVariable Long productoId,
            HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        return ResponseEntity.ok(carritoService.eliminarItem(usuarioId, productoId));
    }

    @DeleteMapping
    public ResponseEntity<Void> limpiarCarrito(HttpServletRequest request) {
        Long usuarioId = obtenerUsuarioId(request);
        carritoService.limpiarCarrito(usuarioId);
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
