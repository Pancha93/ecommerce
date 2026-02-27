package SistemadeGestiondeOrdenes.entity.services.impl;

import SistemadeGestiondeOrdenes.entity.dto.CategoriaDTO;
import SistemadeGestiondeOrdenes.entity.entities.Categoria;
import SistemadeGestiondeOrdenes.entity.repositories.CategoriaRepository;
import SistemadeGestiondeOrdenes.entity.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaDTO> obtenerActivas() {
        return categoriaRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaDTO obtenerPorId(Long id) {
        Categoria categoria = obtenerEntidadPorId(id);
        return convertirADTO(categoria);
    }

    @Override
    public CategoriaDTO crear(CategoriaDTO categoriaDTO) {
        Categoria categoria = new Categoria();
        categoria.setNombre(categoriaDTO.getNombre());
        categoria.setDescripcion(categoriaDTO.getDescripcion());
        categoria.setImagenUrl(categoriaDTO.getImagenUrl());
        categoria.setActivo(categoriaDTO.getActivo() != null ? categoriaDTO.getActivo() : true);
        
        categoria = categoriaRepository.save(categoria);
        return convertirADTO(categoria);
    }

    @Override
    public CategoriaDTO actualizar(Long id, CategoriaDTO categoriaDTO) {
        Categoria categoria = obtenerEntidadPorId(id);
        
        if (categoriaDTO.getNombre() != null) {
            categoria.setNombre(categoriaDTO.getNombre());
        }
        if (categoriaDTO.getDescripcion() != null) {
            categoria.setDescripcion(categoriaDTO.getDescripcion());
        }
        if (categoriaDTO.getImagenUrl() != null) {
            categoria.setImagenUrl(categoriaDTO.getImagenUrl());
        }
        if (categoriaDTO.getActivo() != null) {
            categoria.setActivo(categoriaDTO.getActivo());
        }
        
        categoria = categoriaRepository.save(categoria);
        return convertirADTO(categoria);
    }

    @Override
    public void eliminar(Long id) {
        Categoria categoria = obtenerEntidadPorId(id);
        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria obtenerEntidadPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
    }

    private CategoriaDTO convertirADTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .imagenUrl(categoria.getImagenUrl())
                .activo(categoria.getActivo())
                .fechaCreacion(categoria.getFechaCreacion())
                .cantidadProductos(categoria.getProductos() != null ? categoria.getProductos().size() : 0)
                .build();
    }
}
