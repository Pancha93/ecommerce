package SistemadeGestiondeOrdenes.seguridad.service;

import SistemadeGestiondeOrdenes.seguridad.dto.AccionObjetoDTO;
import SistemadeGestiondeOrdenes.seguridad.dto.SaveUser;
import SistemadeGestiondeOrdenes.seguridad.dto.SaveUsuarioRol;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> getUsers();

    Usuario crearUsuario(SaveUser newUser);

    Boolean asignarRol(SaveUsuarioRol saveUsuarioRol);

    Boolean actualizarPrivilegio(AccionObjetoDTO permiso);

    boolean quitarRol(Long usuarioId, Long rolId);

    boolean eliminarUsuario(Long usuarioId);
}

