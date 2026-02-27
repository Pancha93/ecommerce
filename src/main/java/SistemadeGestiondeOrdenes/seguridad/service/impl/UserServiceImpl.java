package SistemadeGestiondeOrdenes.seguridad.service.impl;

import SistemadeGestiondeOrdenes.seguridad.dto.AccionObjetoDTO;
import SistemadeGestiondeOrdenes.seguridad.dto.SaveUser;
import SistemadeGestiondeOrdenes.seguridad.dto.SaveUsuarioRol;
import SistemadeGestiondeOrdenes.seguridad.exception.ObjectNotFoundException;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Privilegio;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Rol;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.persistence.repository.PrivilegioRepository;
import SistemadeGestiondeOrdenes.seguridad.persistence.repository.UserRepository;
import SistemadeGestiondeOrdenes.seguridad.service.RoleService;
import SistemadeGestiondeOrdenes.seguridad.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PrivilegioRepository privilegioRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public Optional<Usuario> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return userRepository.findByCorreo(correo);
    }


    @Override
    public List<Usuario> getUsers() {
        return userRepository.listarTodo();
    }

    @Override
    public Usuario crearUsuario(SaveUser newUser) {
        Usuario user = new Usuario();
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setCorreo(newUser.getCorreo());
        user.setPassword(newUser.getPassword());

        // Obtener el rol predeterminado y crear el conjunto de roles
        Rol defaultRole = roleService.findDefaultRole()
                .orElseThrow(() -> new RuntimeException("Default role not found."));
        Set<Rol> roles = new HashSet<>();
        roles.add(defaultRole);

        // Agregar roles adicionales si fueron seleccionados
        if (newUser.getRoles() != null && !newUser.getRoles().isEmpty()) {
            for (Long rolId : newUser.getRoles()) {
                roleService.findById(rolId)
                        .ifPresent(roles::add);
            }
        }

        // Asignar roles al usuario
        user.setRoles(roles);

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }

    @Override
    public Boolean asignarRol(SaveUsuarioRol saveUsuarioRol) throws IllegalArgumentException, ObjectNotFoundException {
        // Buscar el usuario por su nombre de usuario
        Usuario usuario = userRepository.findByUsername(saveUsuarioRol.getUsername())
                .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado: " + saveUsuarioRol.getUsername()));

        // Iterar sobre los nombres de los roles proporcionados
        for (String nombreRol : saveUsuarioRol.getRoles()) {
            // Buscar el rol por su nombre
            Rol rol = roleService.findByNombre(nombreRol)
                    .orElseThrow(() -> new ObjectNotFoundException("Rol no encontrado: " + nombreRol));

            // Agregar el rol al usuario
            usuario.getRoles().add(rol);
        }

        // Guardar los cambios en la base de datos
        userRepository.save(usuario);

        return true;
    }

    @Override
    public Boolean actualizarPrivilegio(AccionObjetoDTO permiso) {
        Optional<Privilegio> privilegio = privilegioRepository.findById(permiso.getIdPrivilegio());
        if (!privilegio.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El permiso no existe");
        }

        Privilegio p = privilegio.get();
        p.setAutorizado(permiso.isAutorizado());

        privilegioRepository.save(p);
        return true;
    }

    @Override
    public boolean quitarRol(Long usuarioId, Long rolId) {
        // Buscar al usuario por su ID
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // Buscar el rol por su ID
        Rol rol = roleService.findById(rolId)
                .orElseThrow(() -> new ObjectNotFoundException("Rol no encontrado con ID: " + rolId));

        // Eliminar el rol del conjunto de roles del usuario
        boolean removed = usuario.getRoles().remove(rol);

        if (removed) {
            // Guardar los cambios en la base de datos
            userRepository.save(usuario);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El rol no está asignado al usuario");
        }
    }

    @Override
    public boolean eliminarUsuario(Long usuarioId) {
        Usuario usuario = userRepository.findById(usuarioId)
                .orElseThrow(() -> new ObjectNotFoundException("Usuario no encontrado"));

        userRepository.delete(usuario);
        return true;
    }

}