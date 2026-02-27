package SistemadeGestiondeOrdenes.seguridad.service.impl;

import SistemadeGestiondeOrdenes.seguridad.persistence.entities.OpcionMenu;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.persistence.repository.OpcionMenuRepository;
import SistemadeGestiondeOrdenes.seguridad.service.OpcionMenuService;
import SistemadeGestiondeOrdenes.seguridad.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OpcionMenuServiceImpl implements OpcionMenuService {

    private final OpcionMenuRepository opcionMenuRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Override
    public List<OpcionMenu> obtenerOpcionesMenu(String username) {
        // Obtener todas las opciones de menú
        List<OpcionMenu> todasLasOpciones = opcionMenuRepository.findAllByOrderByOrdenAsc();
        
        // Obtener el usuario
        Usuario usuario = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Obtener roles del usuario
        List<String> roles = usuario.getRoles().stream()
                .map(rol -> rol.getNombre())
                .collect(Collectors.toList());
        
        // Filtrar las opciones de menú según los permisos del usuario
        return todasLasOpciones.stream()
                .filter(opcion -> authenticationService.verificarPermiso(
                        username, 
                        roles, 
                        "ver", 
                        opcion.getNombreObjeto()
                ))
                .collect(Collectors.toList());
    }
}
