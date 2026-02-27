package SistemadeGestiondeOrdenes.entity.services.impl;

import SistemadeGestiondeOrdenes.entity.dto.CarritoDTO;
import SistemadeGestiondeOrdenes.entity.dto.CarritoItemDTO;
import SistemadeGestiondeOrdenes.entity.dto.AgregarAlCarritoRequest;
import SistemadeGestiondeOrdenes.entity.entities.*;
import SistemadeGestiondeOrdenes.entity.repositories.CarritoRepository;
import SistemadeGestiondeOrdenes.entity.repositories.CarritoItemRepository;
import SistemadeGestiondeOrdenes.entity.services.CarritoService;
import SistemadeGestiondeOrdenes.entity.services.ProductoService;
import SistemadeGestiondeOrdenes.seguridad.persistence.entities.Usuario;
import SistemadeGestiondeOrdenes.seguridad.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoService productoService;
    private final UserRepository usuarioRepository;

    @Override
    public CarritoDTO obtenerCarritoUsuario(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        return convertirADTO(carrito);
    }

    @Override
    public CarritoDTO agregarItem(Long usuarioId, AgregarAlCarritoRequest request) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        Producto producto = productoService.obtenerEntidadPorId(request.getProductoId());

        if (!producto.getActivo()) {
            throw new RuntimeException("El producto no está disponible");
        }

        if (producto.getStock() < request.getCantidad()) {
            throw new RuntimeException("No hay suficiente stock disponible");
        }

        CarritoItem itemExistente = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), producto.getId())
                .orElse(null);

        if (itemExistente != null) {
            int nuevaCantidad = itemExistente.getCantidad() + request.getCantidad();
            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("No hay suficiente stock disponible");
            }
            itemExistente.setCantidad(nuevaCantidad);
            carritoItemRepository.save(itemExistente);
        } else {
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(request.getCantidad());
            nuevoItem.setPrecioUnitario(producto.getPrecioFinal());
            carritoItemRepository.save(nuevoItem);
        }

        carrito = carritoRepository.findById(carrito.getId()).orElseThrow();
        return convertirADTO(carrito);
    }

    @Override
    public CarritoDTO actualizarCantidad(Long usuarioId, Long productoId, Integer cantidad) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        CarritoItem item = carritoItemRepository
                .findByCarritoIdAndProductoId(carrito.getId(), productoId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado en el carrito"));

        if (cantidad <= 0) {
            carritoItemRepository.delete(item);
        } else {
            Producto producto = item.getProducto();
            if (producto.getStock() < cantidad) {
                throw new RuntimeException("No hay suficiente stock disponible");
            }
            item.setCantidad(cantidad);
            carritoItemRepository.save(item);
        }

        carrito = carritoRepository.findById(carrito.getId()).orElseThrow();
        return convertirADTO(carrito);
    }

    @Override
    public CarritoDTO eliminarItem(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        carritoItemRepository.deleteByCarritoIdAndProductoId(carrito.getId(), productoId);
        carrito = carritoRepository.findById(carrito.getId()).orElseThrow();
        return convertirADTO(carrito);
    }

    @Override
    public void limpiarCarrito(Long usuarioId) {
        Carrito carrito = obtenerOCrearCarrito(usuarioId);
        carritoItemRepository.deleteByCarritoId(carrito.getId());
    }

    private Carrito obtenerOCrearCarrito(Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    Carrito nuevoCarrito = new Carrito();
                    nuevoCarrito.setUsuario(usuario);
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    private CarritoDTO convertirADTO(Carrito carrito) {
        List<CarritoItemDTO> items = carrito.getItems().stream()
                .map(this::convertirItemADTO)
                .collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CarritoItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int cantidadItems = items.stream()
                .mapToInt(CarritoItemDTO::getCantidad)
                .sum();

        return CarritoDTO.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuario().getId())
                .items(items)
                .total(total)
                .cantidadItems(cantidadItems)
                .fechaActualizacion(carrito.getFechaActualizacion())
                .build();
    }

    private CarritoItemDTO convertirItemADTO(CarritoItem item) {
        Producto producto = item.getProducto();
        String imagenUrl = producto.getImagenes().stream()
                .filter(ProductoImagen::getEsPrincipal)
                .findFirst()
                .map(ProductoImagen::getUrl)
                .orElse(producto.getImagenes().isEmpty() ? null : producto.getImagenes().get(0).getUrl());

        return CarritoItemDTO.builder()
                .id(item.getId())
                .productoId(producto.getId())
                .productoNombre(producto.getNombre())
                .productoImagen(imagenUrl)
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getSubtotal())
                .stockDisponible(producto.getStock())
                .build();
    }
}
