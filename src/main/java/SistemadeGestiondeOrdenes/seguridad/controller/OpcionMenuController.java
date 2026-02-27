package SistemadeGestiondeOrdenes.seguridad.controller;

import SistemadeGestiondeOrdenes.seguridad.persistence.entities.OpcionMenu;
import SistemadeGestiondeOrdenes.seguridad.service.OpcionMenuService;
import SistemadeGestiondeOrdenes.seguridad.service.impl.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
public class OpcionMenuController {

    private final OpcionMenuService opcionMenuService;
    private final JwtTokenService jwtTokenService;

    @GetMapping("/opciones")
    public ResponseEntity<List<OpcionMenu>> obtenerOpcionesMenu(HttpServletRequest request) {
        String token = jwtTokenService.extractJwtFromRequest(request);
        String username = jwtTokenService.obtenerUsername(token);
        
        List<OpcionMenu> opciones = opcionMenuService.obtenerOpcionesMenu(username);
        return ResponseEntity.ok(opciones);
    }
}
