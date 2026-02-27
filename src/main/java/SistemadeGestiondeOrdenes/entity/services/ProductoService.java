package SistemadeGestiondeOrdenes.entity.services;

import SistemadeGestiondeOrdenes.entity.dto.ProductoDTO;
import SistemadeGestiondeOrdenes.entity.entities.Producto;
import java.util.List;

public interface ProductoService {
    List<ProductoDTO> obtenerTodos();
    List<ProductoDTO> obtenerActivos();
    List<ProductoDTO> obtenerDestacados();
    List<ProductoDTO> obtenerPorCategoria(Long categoriaId);
    List<ProductoDTO> buscar(String termino);
    List<ProductoDTO> obtenerNuevos();
    List<ProductoDTO> obtenerOfertas();
    ProductoDTO obtenerPorId(Long id);
    ProductoDTO crear(ProductoDTO productoDTO);
    ProductoDTO actualizar(Long id, ProductoDTO productoDTO);
    void eliminar(Long id);
    Producto obtenerEntidadPorId(Long id);
}
