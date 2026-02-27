package SistemadeGestiondeOrdenes.entity.services;

import SistemadeGestiondeOrdenes.entity.dto.CategoriaDTO;
import SistemadeGestiondeOrdenes.entity.entities.Categoria;
import java.util.List;

public interface CategoriaService {
    List<CategoriaDTO> obtenerTodas();
    List<CategoriaDTO> obtenerActivas();
    CategoriaDTO obtenerPorId(Long id);
    CategoriaDTO crear(CategoriaDTO categoriaDTO);
    CategoriaDTO actualizar(Long id, CategoriaDTO categoriaDTO);
    void eliminar(Long id);
    Categoria obtenerEntidadPorId(Long id);
}
