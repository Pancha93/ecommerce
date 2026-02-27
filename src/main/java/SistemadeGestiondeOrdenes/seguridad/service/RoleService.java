package SistemadeGestiondeOrdenes.seguridad.service;

import SistemadeGestiondeOrdenes.seguridad.dto.AccionObjetoDTO;
import SistemadeGestiondeOrdenes.seguridad.dto.RolDTO;
import SistemadeGestiondeOrdenes.seguridad.dto.SaveRolRol;
import SistemadeGestiondeOrdenes.seguridad.dto.SaveUsuarioRol;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Rol;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface RoleService {
    Optional<Rol> findDefaultRole();

    Optional<Rol> findByNombre(String name);

    Rol crearRol(Rol rol);


    List<RolDTO> getRoles();

    Boolean agregarRol(SaveRolRol saveRolRol);

    List<AccionObjetoDTO> getPermisosPorRolOUsuario(Long rolId, Long userId);

    Optional<Rol> findById(Long id);

    boolean quitarRolHijo(Long rolPadreId, Long rolHijoId);

    boolean eliminarRolPadre(Long rolPadreId);
}
