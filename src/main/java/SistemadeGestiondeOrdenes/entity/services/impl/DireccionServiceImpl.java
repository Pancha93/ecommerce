package SistemadeGestiondeOrdenes.entity.services.impl;

import SistemadeGestiondeOrdenes.entity.dto.DireccionDTO;
import SistemadeGestiondeOrdenes.entity.entities.Direccion;
import SistemadeGestiondeOrdenes.entity.repositories.DireccionRepository;
import SistemadeGestiondeOrdenes.entity.services.DireccionService;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DireccionServiceImpl implements DireccionService {

    private final DireccionRepository direccionRepository;
    private final UserRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<DireccionDTO> obtenerDireccionesUsuario(Long usuarioId) {
        return direccionRepository.findByUsuarioId(usuarioId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DireccionDTO obtenerPorId(Long id) {
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + id));
        return convertirADTO(direccion);
    }

    @Override
    @Transactional(readOnly = true)
    public DireccionDTO obtenerPredeterminada(Long usuarioId) {
        Direccion direccion = direccionRepository.findByUsuarioIdAndEsPredeterminadaTrue(usuarioId)
                .orElse(null);
        return direccion != null ? convertirADTO(direccion) : null;
    }

    @Override
    public DireccionDTO crear(Long usuarioId, DireccionDTO direccionDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Direccion direccion = new Direccion();
        direccion.setUsuario(usuario);
        actualizarEntidad(direccion, direccionDTO);

        // Si es la primera dirección o se marca como predeterminada
        if (direccionDTO.getEsPredeterminada() != null && direccionDTO.getEsPredeterminada()) {
            desmarcarPredeterminadas(usuarioId);
            direccion.setEsPredeterminada(true);
        } else if (direccionRepository.findByUsuarioId(usuarioId).isEmpty()) {
            direccion.setEsPredeterminada(true);
        } else {
            direccion.setEsPredeterminada(false);
        }

        direccion = direccionRepository.save(direccion);
        return convertirADTO(direccion);
    }

    @Override
    public DireccionDTO actualizar(Long id, DireccionDTO direccionDTO) {
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + id));

        actualizarEntidad(direccion, direccionDTO);

        if (direccionDTO.getEsPredeterminada() != null && direccionDTO.getEsPredeterminada()) {
            desmarcarPredeterminadas(direccion.getUsuario().getId());
            direccion.setEsPredeterminada(true);
        }

        direccion = direccionRepository.save(direccion);
        return convertirADTO(direccion);
    }

    @Override
    public DireccionDTO establecerComoPredeterminada(Long id, Long usuarioId) {
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + id));

        if (!direccion.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("La dirección no pertenece al usuario");
        }

        desmarcarPredeterminadas(usuarioId);
        direccion.setEsPredeterminada(true);
        direccion = direccionRepository.save(direccion);
        return convertirADTO(direccion);
    }

    @Override
    public void eliminar(Long id) {
        Direccion direccion = direccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada con ID: " + id));

        Long usuarioId = direccion.getUsuario().getId();
        boolean eraPredeterminada = direccion.getEsPredeterminada();

        direccionRepository.delete(direccion);

        // Si era predeterminada, establecer otra como predeterminada
        if (eraPredeterminada) {
            List<Direccion> direccionesRestantes = direccionRepository.findByUsuarioId(usuarioId);
            if (!direccionesRestantes.isEmpty()) {
                Direccion primera = direccionesRestantes.get(0);
                primera.setEsPredeterminada(true);
                direccionRepository.save(primera);
            }
        }
    }

    private void desmarcarPredeterminadas(Long usuarioId) {
        List<Direccion> direcciones = direccionRepository.findByUsuarioId(usuarioId);
        direcciones.forEach(d -> {
            d.setEsPredeterminada(false);
            direccionRepository.save(d);
        });
    }

    private void actualizarEntidad(Direccion direccion, DireccionDTO dto) {
        direccion.setNombreCompleto(dto.getNombreCompleto());
        direccion.setTelefono(dto.getTelefono());
        direccion.setDireccionLinea1(dto.getDireccionLinea1());
        direccion.setDireccionLinea2(dto.getDireccionLinea2());
        direccion.setCiudad(dto.getCiudad());
        direccion.setEstadoProvincia(dto.getEstadoProvincia());
        direccion.setCodigoPostal(dto.getCodigoPostal());
        direccion.setPais(dto.getPais());
    }

    private DireccionDTO convertirADTO(Direccion direccion) {
        return DireccionDTO.builder()
                .id(direccion.getId())
                .usuarioId(direccion.getUsuario().getId())
                .nombreCompleto(direccion.getNombreCompleto())
                .telefono(direccion.getTelefono())
                .direccionLinea1(direccion.getDireccionLinea1())
                .direccionLinea2(direccion.getDireccionLinea2())
                .ciudad(direccion.getCiudad())
                .estadoProvincia(direccion.getEstadoProvincia())
                .codigoPostal(direccion.getCodigoPostal())
                .pais(direccion.getPais())
                .esPredeterminada(direccion.getEsPredeterminada())
                .fechaCreacion(direccion.getFechaCreacion())
                .build();
    }
}
