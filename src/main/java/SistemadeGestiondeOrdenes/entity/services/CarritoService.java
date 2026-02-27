package SistemadeGestiondeOrdenes.entity.services;

import SistemadeGestiondeOrdenes.entity.dto.CarritoDTO;
import SistemadeGestiondeOrdenes.entity.dto.AgregarAlCarritoRequest;

public interface CarritoService {
    CarritoDTO obtenerCarritoUsuario(Long usuarioId);
    CarritoDTO agregarItem(Long usuarioId, AgregarAlCarritoRequest request);
    CarritoDTO actualizarCantidad(Long usuarioId, Long productoId, Integer cantidad);
    CarritoDTO eliminarItem(Long usuarioId, Long productoId);
    void limpiarCarrito(Long usuarioId);
}
