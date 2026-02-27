package SistemadeGestiondeOrdenes.entity.services;

import SistemadeGestiondeOrdenes.entity.dto.DireccionDTO;
import java.util.List;

public interface DireccionService {
    List<DireccionDTO> obtenerDireccionesUsuario(Long usuarioId);
    DireccionDTO obtenerPorId(Long id);
    DireccionDTO obtenerPredeterminada(Long usuarioId);
    DireccionDTO crear(Long usuarioId, DireccionDTO direccionDTO);
    DireccionDTO actualizar(Long id, DireccionDTO direccionDTO);
    DireccionDTO establecerComoPredeterminada(Long id, Long usuarioId);
    void eliminar(Long id);
}
