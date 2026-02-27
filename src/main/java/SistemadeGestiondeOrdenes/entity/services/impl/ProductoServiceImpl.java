package SistemadeGestiondeOrdenes.entity.services.impl;

import SistemadeGestiondeOrdenes.entity.dto.ProductoDTO;
import SistemadeGestiondeOrdenes.entity.dto.ProductoImagenDTO;
import SistemadeGestiondeOrdenes.entity.entities.Producto;
import SistemadeGestiondeOrdenes.entity.entities.ProductoImagen;
import SistemadeGestiondeOrdenes.entity.entities.Categoria;
import SistemadeGestiondeOrdenes.entity.repositories.ProductoRepository;
import SistemadeGestiondeOrdenes.entity.repositories.ProductoImagenRepository;
import SistemadeGestiondeOrdenes.entity.services.ProductoService;
import SistemadeGestiondeOrdenes.entity.services.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoImagenRepository productoImagenRepository;
    private final CategoriaService categoriaService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerDestacados() {
        return productoRepository.findByDestacadoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> buscar(String termino) {
        return productoRepository.buscarProductos(termino).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerNuevos() {
        return productoRepository.findByActivoTrueOrderByFechaCreacionDesc().stream()
                .limit(10)
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerOfertas() {
        return productoRepository.findByActivoTrueAndPrecioOfertaIsNotNullOrderByPrecioOfertaAsc().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = obtenerEntidadPorId(id);
        return convertirADTO(producto);
    }

    @Override
    public ProductoDTO crear(ProductoDTO productoDTO) {
        Producto producto = new Producto();
        actualizarEntidad(producto, productoDTO);
        producto = productoRepository.save(producto);
        return convertirADTO(producto);
    }

    @Override
    public ProductoDTO actualizar(Long id, ProductoDTO productoDTO) {
        Producto producto = obtenerEntidadPorId(id);
        actualizarEntidad(producto, productoDTO);
        producto = productoRepository.save(producto);
        return convertirADTO(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = obtenerEntidadPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerEntidadPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    private void actualizarEntidad(Producto producto, ProductoDTO dto) {
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setPrecioOferta(dto.getPrecioOferta());
        producto.setStock(dto.getStock() != null ? dto.getStock() : 0);
        producto.setSku(dto.getSku());
        producto.setPeso(dto.getPeso());
        producto.setMarca(dto.getMarca());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        producto.setDestacado(dto.getDestacado() != null ? dto.getDestacado() : false);

        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaService.obtenerEntidadPorId(dto.getCategoriaId());
            producto.setCategoria(categoria);
        }
    }

    private ProductoDTO convertirADTO(Producto producto) {
        List<ProductoImagenDTO> imagenes = producto.getImagenes().stream()
                .map(img -> ProductoImagenDTO.builder()
                        .id(img.getId())
                        .url(img.getUrl())
                        .esPrincipal(img.getEsPrincipal())
                        .orden(img.getOrden())
                        .build())
                .collect(Collectors.toList());

        String imagenPrincipal = producto.getImagenes().stream()
                .filter(ProductoImagen::getEsPrincipal)
                .findFirst()
                .map(ProductoImagen::getUrl)
                .orElse(imagenes.isEmpty() ? null : imagenes.get(0).getUrl());

        return ProductoDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .precioOferta(producto.getPrecioOferta())
                .stock(producto.getStock())
                .sku(producto.getSku())
                .peso(producto.getPeso())
                .marca(producto.getMarca())
                .activo(producto.getActivo())
                .destacado(producto.getDestacado())
                .fechaCreacion(producto.getFechaCreacion())
                .categoriaId(producto.getCategoria() != null ? producto.getCategoria().getId() : null)
                .categoriaNombre(producto.getCategoria() != null ? producto.getCategoria().getNombre() : null)
                .imagenes(imagenes)
                .imagenPrincipal(imagenPrincipal)
                .build();
    }
}
