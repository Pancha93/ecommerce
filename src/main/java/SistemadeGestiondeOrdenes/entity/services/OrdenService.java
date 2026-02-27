package SistemadeGestiondeOrdenes.entity.services;

import SistemadeGestiondeOrdenes.entity.dto.OrdenDTO;
import SistemadeGestiondeOrdenes.entity.dto.CrearOrdenRequest;
import SistemadeGestiondeOrdenes.entity.entities.Orden;
import java.util.List;

public interface OrdenService {
    List<OrdenDTO> obtenerOrdenesUsuario(Long usuarioId);
    OrdenDTO obtenerPorId(Long id);
    OrdenDTO obtenerPorNumero(String numeroOrden);
    OrdenDTO crearDesdeCarrito(Long usuarioId, CrearOrdenRequest request);
    OrdenDTO actualizarEstado(Long id, String nuevoEstado);
    List<OrdenDTO> obtenerPorEstado(String estado);
    List<OrdenDTO> obtenerTodas();
}
